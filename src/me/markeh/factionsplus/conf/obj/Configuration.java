package me.markeh.factionsplus.conf.obj;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.markeh.factionsplus.FactionsPlus;

public abstract class Configuration {
	
	// -----
	// Fields
	// -----
	
	private String fileName;
	private String subFolder;
	private File managingFile;
	private PrintWriter writer;
	
	public final void setName(String fileName) {
		this.fileName = fileName;
	}
	
	public final void setSub(String subFolder) {
		this.subFolder = subFolder;
	}
	
	// saving / writing 
	public final Configuration save() {
		if (this.fileName == null) return this;

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
			e.printStackTrace();
		}
		
		List<String> sectionsCreated = new ArrayList<String>();
		
		for (Field field : this.getClass().getFields()) {			
			if (field.isAnnotationPresent(Option.class)) {
				try {
					String[] metaData = field.getAnnotation(Option.class).value();
					
					String section = metaData[0];
					
					String name = metaData[1];
					String description = metaData[2];
					String value = this.buildValue(field);
					
					if ( ! sectionsCreated.contains(section)) {
						sectionsCreated.add(section);
						writeSectionLine(section);
					}
										
					writeKeyLine(name, value, description);

				} catch(Exception e) {
					FactionsPlus.get().log("Help, something broke: ");
					FactionsPlus.get().log(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		
		writer.close();
		
		return this;
		
	}
		
	private final String buildValue(Field field) throws IllegalArgumentException, IllegalAccessException {
		// Grab any key/value types (if they exist - needed for hashmaps)
        Type keyType = null;
        Type valueType = null;

		try {
	        ParameterizedType pt = (ParameterizedType) field.getGenericType();
	        for (Type type : pt.getActualTypeArguments()) {
	        	if (keyType == null) keyType = type;
	        	else valueType = type;
	        }

		} catch(Exception e) {
			// don't give a fuck
		}
        
        
        // Route to here 
		return this.buildValue(field.getType(), field.get(this), valueType);
	}
	
	@SuppressWarnings("unchecked")
	private String buildValue(Class<?> clazz, Object oValue, Type extraValue) {
		FactionsPlus.get().debug("Building raw value for type " + clazz.toGenericString());
		
		String result = "";
		
		if (clazz == Location.class) { 
			FactionsPlus.get().debug("Is location .. ");
			
			if(oValue == null) {
				result = "";
			} else {
				// Bukkit Location
				Location locationValue = (Location) oValue;
				result = locationValue.getX() + "," +
						 locationValue.getY() + "," +
						 locationValue.getZ() + "," +
						 locationValue.getPitch() + "," +
						 locationValue.getYaw() + "," +
						 locationValue.getWorld().getUID().toString();
			}
		} else if (clazz == List.class) { 
			FactionsPlus.get().debug("Is list .. ");

			// List
			
			result = this.listWrite((List<?>) oValue);
		} else if (clazz == HashMap.class || clazz == Map.class) {
			FactionsPlus.get().debug("Is hashmap .. ");

			// HashMap
			result = this.hashmapWrite((HashMap<String, Object>) oValue, extraValue);
		} else if (clazz == String.class || clazz == Integer.class || clazz == Double.class || clazz == Boolean.class) {
			FactionsPlus.get().debug("Is raw .. ");

			// Types that don't need to be formatted 
			result = oValue.toString();
		}
		
		FactionsPlus.get().debug("result = " + result);

		return result;
	}
	
	private final void writeSectionLine(String section) {
		writer.println("");
		writer.println(section + ":");
	}
	
	private final void writeKeyLine(String key, String value, String comment) {
		writer.println("    # " + comment);
		writer.println("    " + key + ": " + value);
		writer.println("    ");
	}
	
	// loading / reading 
	
	public final Configuration load() {
		if (this.fileName == null) return this;

		if (this.managingFile == null) {
			if (this.subFolder != null) {
				File dir = new File(FactionsPlus.get().getDataFolder(), this.subFolder);
				
				if ( ! dir.exists()) dir.mkdir();
				
				this.managingFile = new File(dir, this.fileName + ".yml");
			} else {
				this.managingFile = new File(FactionsPlus.get().getDataFolder(), this.fileName + ".yml");
			}
		}
		
		if (!this.managingFile.exists()) return this;
		
		FileConfiguration yamlConfig = YamlConfiguration.loadConfiguration(this.managingFile);

		for (Field field : this.getClass().getFields()) {
			if (field.isAnnotationPresent(Option.class)) {
				
				try {
					String[] metaData = field.getAnnotation(Option.class).value();
					
					String section = metaData[0];
					String name = metaData[1];
					
					Object value = yamlConfig.get(section + "." + name);
					
					if (value == null) continue;
					
					this.loadField(field, value);
					
				} catch(Exception e) {
					FactionsPlus.get().log("Help, something broke: ");
					FactionsPlus.get().log(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		
		return this;
	}
	
	public void loadField(Field field, Object value) throws IllegalArgumentException, IllegalAccessException {
		if (field.getType() == Location.class) {
			String[] locData = value.toString().split(",");
			
			Double locX = Double.valueOf(locData[0]);
			Double locY = Double.valueOf(locData[1]);
			Double locZ = Double.valueOf(locData[2]);
			
			Float locPitch = Float.valueOf(locData[3]);
			Float locYaw = Float.valueOf(locData[4]);
			
			World world = Bukkit.getWorld(UUID.fromString(locData[5]));
			
			Location location = new Location(world, locX, locY, locZ);
			
			location.setYaw(locYaw);
			location.setPitch(locPitch);
			
			field.set(this, location);
		} else if(field.getType() == List.class) {
			field.set(this, this.listRead(value));
		} else if(field.getType() == Double.class) {
			field.set(this, Double.valueOf(value.toString()));
		} else if(field.getType() == Integer.class) {
			field.set(this, Integer.valueOf(value.toString()));
		} else if(field.getType() == Boolean.class) {
			field.set(this, Boolean.valueOf(value.toString()));
		} else if(field.getType() == String.class) {
			field.set(this, String.valueOf(value));
		} else if(field.getType() == HashMap.class) {
            ParameterizedType pt = (ParameterizedType) field.getGenericType();
            
            Type keyType = null;
            Type valueType = null;
            
            for(Type type : pt.getActualTypeArguments()) {
            	if(keyType == null) keyType = type;
            	else valueType = type;
            }
            
            // Fetch the raw hashmap values (string values)
			HashMap<String, String> rawHashMap = this.hashMapReadRaw(value.toString());
			
			HashMap<String, Object> newHashMap = new HashMap<String, Object>();
			
			for(String key : rawHashMap.keySet()) {
				String keyValue = rawHashMap.get(key);
				// Using that string value, we now convert it through buildValue 
				newHashMap.put(key, this.buildValue(valueType.getClass(), keyValue, valueType));
			}
			
			field.set(this, newHashMap);
		} else {
			// Unsafe, so try as a last resort and ignore errors 
			try {
				field.set(this, value);
			} catch(Exception e) {
				// ... 
			}
		}
	}
	
	private final Object listRead(Object value) {
		if (value == null) return new ArrayList<Object>();
		
		String rawList = (String) value;
		
		if (rawList == "") return new ArrayList<Object>();
		
		String[] items = rawList.split(", ");
		List<Object> finishList = new ArrayList<Object>();

		try {
			if (items.length == 0) {
				finishList.add(this.buildValue(value.getClass(), URLDecoder.decode(rawList, "UTF-8"), null));
			} else {
				for(String item : items) {
					finishList.add(this.buildValue(value.getClass(), URLDecoder.decode(item, "UTF-8"), null));
				}
			}
		} catch(Exception e) {
			
		}
		
		return finishList;
	}
	
	private final String listWrite(List<?> value) {
		String rawList = "";
		Boolean isFirst = true;
		
		for (Object s : value) {
			if ( ! isFirst) {
				rawList+=", ";
			} else {
				isFirst = false;
			}
			
			if (s.getClass() == List.class) {
				try {
					rawList += "List[ " + URLEncoder.encode(listWrite((List<?>) s), "UTF-8") + " ]";
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				rawList += this.buildValue(s.getClass(), s, null);
			}
		}
		
		return rawList;
	}
	
	private final HashMap<String, String> hashMapReadRaw(String rawData) {
		// urlencoded_key=>urlencoded_value,
		
		String[] rawValues = rawData.split(", ");
		
		HashMap<String, String> finalHashmap = new HashMap<String, String>();
		
		if(rawData == null || rawData == "") return finalHashmap;
		
		try {
			if(rawValues.length == 0) {
				String[] values = rawData.split(" => ");
				
				String thisKey = URLDecoder.decode(values[0], "UTF-8");
				String thisValue = URLDecoder.decode(values[1], "UTF-8");
				
				finalHashmap.put(thisKey, thisValue);
				
			} else {
				Boolean isFirst = true;
				
				for (String rawValue : rawValues) {
					if (isFirst) {
						isFirst = false;
					} else {
						
					}
					
					String[] values = rawValue.split(" => ");
					
					String thisKey = URLDecoder.decode(values[0], "UTF-8");
					String thisValue = URLDecoder.decode(values[1], "UTF-8");
					
					finalHashmap.put(thisKey, thisValue);
				}
			}
		} catch(Exception e) { /* should not happen */ }
		
		return finalHashmap;
	}
	
	private final String hashmapWrite(HashMap<String, Object> value, Type keyType) {
		String result = "";
		
		Boolean isFirst = true;
		
		FactionsPlus.get().debug("Key type = " + keyType.getTypeName());
		
		for(String key : value.keySet()) {
			FactionsPlus.get().debug("HashMap += " + key + ", (tostr) " + value.get(key).toString());

			if (isFirst) {
				isFirst = false;
			} else {
				result+= ", ";
			}
			
			FactionsPlus.get().debug(key + " => " + this.buildValue(keyType.getClass(), value.get(key), keyType));
			
			try {
				result += URLEncoder.encode(key, "UTF-8") + 
						  " => " + 
						  URLEncoder.encode(this.buildValue(keyType.getClass(), value.get(key), keyType), "UTF-8");
			} catch(Exception e) { /* should not happen */ }
		}
		
		return result;
	}
}
