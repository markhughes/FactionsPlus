package me.markeh.factionsplus.conf.obj;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.markeh.factionsplus.FactionsPlus;
import me.markeh.factionsplus.conf.types.TLoc;
import me.markeh.factionsplus.conf.types.TMap;

// TODO: WAY better way to do this 

@SuppressWarnings("unchecked")
public abstract class Configuration<T> {
	
	// ------------------------------
	// Fields
	// ------------------------------
	
	private String fileName;
	private String subFolder;
	private List<String> headerLines = new ArrayList<String>();
	
	private File managingFile;
	private PrintWriter writer;
	
	private Timer watchTimer = null;
	
	private Boolean saveable = true;
	
	// ------------------------------
	// Methods
	// ------------------------------
	
	// Required: Set the configuration file name
	public final void setName(String fileName) {
		this.fileName = fileName;
	}
	
	public final String getName() { return this.fileName; }
	
	// Optional: Set saveable
	public final void setSaveable(Boolean saveable) {
		this.saveable = saveable;
	}
	
	public final Boolean isSaveable() { 
		return this.saveable;
	}
	
	// Optional: The folder the file will be stored under
	public final void setSub(String subFolder) {
		this.subFolder = subFolder;
	}
	
	public final String getSub() { return this.subFolder; }
	
	// Optional: Header lines
	private final T setHeader(List<String> header) {
		this.headerLines = header;	
		
		return (T) this;
	}
	
	public final T setHeader(String... lines) {
		headerLines.clear();
		
		for (String line : lines) headerLines.add(line);
		
		return (T) this;
	}
	
	public final List<String> getHeader() { return this.headerLines; }
	
	// Save the configuration file  
	public final T save() {
		if ( ! this.isSaveable()) return (T) this;
		
		if (this.fileName == null) return (T) this;

		if (this.managingFile == null) {
			if (this.subFolder != null) {
				File dir = new File(FactionsPlus.get().getDataFolder(), this.subFolder);
				this.managingFile = new File(dir, this.fileName + ".yml");
			} else {
				this.managingFile = new File(FactionsPlus.get().getDataFolder(), this.fileName + ".yml");
			}
		}
		
		try {
			writer = new PrintWriter(this.managingFile, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			FactionsPlus.get().logError(e);
		}
		
		// Writer header as required
		for (String headerLine : this.headerLines) writer.println("# " + headerLine);
		
		// Write sections and the fields 
		List<String> sectionsCreated = new ArrayList<String>();
		
		for (Field field : this.getClass().getFields()) {
			FieldMetaData metaData = FieldMetaData.get(field);
			
			if ( ! metaData.isConfigurationField()) continue;
					
			if ( ! sectionsCreated.contains(metaData.getSectionName())) {
				sectionsCreated.add(metaData.getSectionName());
				writeSectionLine(metaData.getSectionName());
			}
			
			String value = null;
			
			try {
				value = this.buildValue(field, metaData.getFieldDescription());
			} catch(Exception e) {
				FactionsPlus.get().logError(e);
			}
					
			if (value != null) writeKeyLine(metaData.getFieldName(), value, metaData.getFieldDescription());
		}
		
		writer.close();
		
		return (T) this;
	}
	
	// Load a configuration file
	public final T load() {
		if (this.fileName == null) return (T) this;
				
		// Set the managing file if it is not set 
		if (this.managingFile == null) {
			if (this.subFolder != null) {
				File dir = new File(FactionsPlus.get().getDataFolder(), this.subFolder);
				
				if ( ! dir.exists()) dir.mkdir();
				
				this.managingFile = new File(dir, this.fileName + ".yml");
			} else {
				this.managingFile = new File(FactionsPlus.get().getDataFolder(), this.fileName + ".yml");
			}
		}
		
		// If it doesn't exist we have nothing to load, so we stop here and leave everything at defaults 
		if ( ! this.managingFile.exists()) return (T) this;
		
		// Use the YamlConfiguration options for this, as it is a hell lot more easier 
		FileConfiguration yamlConfig = YamlConfiguration.loadConfiguration(this.managingFile);
		
		// Go through each field, and see if its a configuration field
		for (Field field : this.getClass().getFields()) {
			FieldMetaData metaData = FieldMetaData.get(field);
			
			if ( ! metaData.isConfigurationField()) continue; // Not a configuration field, so ignore
				
			Object value = null;
			
			// Lists will be read using the getList method 
			if (field.getType() == List.class) {
				value = yamlConfig.getList(metaData.getSectionName() + "." + metaData.getFieldName());
			} else {
				// Just get the object raw 
				value = yamlConfig.get(metaData.getSectionName() + "." + metaData.getFieldName());
			}
					
			if (value == null) continue;

			try {
				// Load the field
				this.loadField(field, value);
			} catch (Exception e) {
				// Log the error 
				FactionsPlus.get().logError(e);
			}
		}
		
		return (T) this;
	}
	
	// Start watching the file for changes
	public T watchStart() {
		if (watchTimer == null) watchTimer = new Timer();
				
		// Using a File Watch Task we'll reload the configuration file when its modified 
		watchTimer.schedule(new FileWatchTask(this.managingFile, this) {
			@Override
			protected void onChange(File file, Configuration<?> configuration) {
				preUpdatePing();
				configuration.load();
				postUpdatePing();
			}
		}, new Date(), 3000);
		
		return (T) this;
	}
	
	// Stop watching the file for changes 
	public final T watchStop() {
		if (watchTimer != null) watchTimer.cancel();
		
		return (T) this;
	}
	
	// Build a field value for the configuration file 
	private final String buildValue(Field field, String description) throws IllegalArgumentException, IllegalAccessException {
		Object value = field.get(this);
		Class<?> clazz = field.getType();
		
		// Null is dangerous to work with, just make it a blank string
		if (value == null) return "";
		
		if (clazz == TMap.class) {
			// TTypes handle their own raw
			return ((TMap<?, ?>) value).asRawString();
		} else if (clazz == TLoc.class) {
			return ((TLoc) value).asRawString();
		} else if (clazz == List.class) {
			// Convert the list to a YAML format.
			// We need to print to the writer for this one
			
			writer.println("    # " + description);
			writer.println("    " + field.getName() + ":");
			
			// Make this list raw, and put it on the writer 
			for(String s : this.getRawList((List<?>) value)) writer.println("        - \"" + s + "\"");
			
			// Make sure everything is neat
			writer.println("    ");
			
			// Return null so we don't write anything
			return null;
		} else {
			// Otherwise, assume it's a raw value (Strings, Integers, etc) 
			return value.toString();
		}
	}
	
	// Writes the start of a section line
	private final void writeSectionLine(String section) {
		writer.println("");
		writer.println(section + ":");
	}
	
	// Writes a key line
	private final void writeKeyLine(String key, String value, String comment) {
		// Comment can be null, so we'll check that first 
		if (comment != null) writer.println("    # " + comment);
		
		writer.println("    " + key + ": \"" + value.replaceAll("\"", "\\\\\"") + "\"");
		writer.println("    ");
	}
	
	// Load a field 
	public final void loadField(Field field, Object value) throws IllegalArgumentException, IllegalAccessException {
		
		if (field.getType() == TLoc.class) {
			field.set(this, TLoc.fromRaw(value.toString()));
		} else if(field.getType() == TMap.class) {			
			field.set(this, TMap.fromRaw(value.toString()));
		} else if(field.getType() == List.class) {			
			field.set(this, this.readList(field, (List<String>) value));
		} else if(field.getType() == Double.class) {
			field.set(this, Double.valueOf(value.toString()));
		} else if(field.getType() == Integer.class) {
			field.set(this, Integer.valueOf(value.toString()));
		} else if(field.getType() == Long.class) {
			field.set(this, Long.valueOf(value.toString()));
		} else if(field.getType() == Boolean.class) {
			field.set(this, Boolean.valueOf(value.toString()));
		} else if(field.getType() == String.class) {
			field.set(this, String.valueOf(value));
		} else {
			// Unsafe, so try as a last resort and ignore errors 
			try {
				field.set(this, value);
			} catch(Exception e) {
				FactionsPlus.get().logError(e);
			}
		}
	}
	
	// Reads a field list and returns the list 
	private final List<Object> readList(Field field, List<String> values) throws IllegalArgumentException, IllegalAccessException {		
		List<Object> convertedList = new ArrayList<>();
		
		for(String rawValue : values) {
			if (field.getType() == TLoc.class) {
				convertedList.add(TLoc.fromRaw(rawValue));
			} else if(field.getType() == TMap.class) {
				convertedList.add(TMap.fromRaw(rawValue));
			} else if(field.getType() == Double.class) {
				convertedList.add(Double.valueOf(rawValue));
			} else if(field.getType() == Integer.class) {
				convertedList.add(Integer.valueOf(rawValue));
			} else if(field.getType() == Long.class) {
				convertedList.add(Long.valueOf(rawValue));
			} else if(field.getType() == Boolean.class) {
				convertedList.add(Boolean.valueOf(rawValue));
			} else if(field.getType() == String.class) {
				convertedList.add(String.valueOf(rawValue));
			} else if(field.getType() == TMap.class) {
				convertedList.add(TMap.fromRaw(rawValue));
			} else {
				// Unsafe, so try as a last resort
				try {
					convertedList.add(rawValue);
				} catch(Exception e) {
					FactionsPlus.get().logError(e);
				}
			}
		}
		
		return null;
	}
	
	// Turns a list of objects into a list of "raw" strings
	private final List<String> getRawList(List<?> values) {
		List<String> rawList = new ArrayList<String>();
		
		for (Object s : values) {
			String toAdd;
			
			if (s instanceof TType) {
				toAdd = ((TType<?>) s).asRawString();
			} else {
				toAdd = s.toString();
			}
			
			rawList.add(toAdd.replaceAll("\"", "\\\""));
		}
		
		return rawList;
	}
	 
	public final T safeClone(Object cloneTo) {
		Configuration<T> newClass = (Configuration<T>) cloneTo;
		
		newClass.setSaveable(false); // by default we wont be saveable 
		
		newClass.setName(this.getName());
		newClass.setHeader(this.getHeader());
		newClass.setSub(this.getSub());
		
		for (Field field : this.getClass().getFields()) {
			FieldMetaData meta = FieldMetaData.get(field);
			
			if ( ! meta.isConfigurationField()) continue; // Not a configuration field, so ignore
								
			try {
				newClass.getClass().getField(field.getName()).set(this, field.get(this));
			} catch (Exception e) {
				// Log the error 
				FactionsPlus.get().logError(e);
			}
		}
		
		return (T) cloneTo;
	}
	
	public void preUpdatePing() { /* Override if needed */ }
	public void postUpdatePing() { /* Override if needed */ }
}
