package me.markeh.factionsplus.conf.obj;

import java.lang.reflect.Field;
import java.util.HashMap;

import me.markeh.factionsplus.FactionsPlus;

public class FieldMetaData {
	
	// ------------------------------
	// Getter
	// ------------------------------
	
	private static HashMap<Field, FieldMetaData> fieldMetaDatas = new HashMap<Field, FieldMetaData>();
	public static FieldMetaData get(Field field) {
		if ( ! fieldMetaDatas.containsKey(field)) fieldMetaDatas.put(field, new FieldMetaData(field));
		
		return fieldMetaDatas.get(field);
	}
	
	// ------------------------------
	// Constructor
	// ------------------------------
	
	private FieldMetaData(Field field) {
		if (field.isAnnotationPresent(Option.class)) {
			this.configurationField = true;
			
			try {
				String[] rawMetaData = field.getAnnotation(Option.class).value();
				
				this.sectionName = rawMetaData[0];
				this.fieldName = rawMetaData[1];
				this.fieldDescription = rawMetaData[2];

			} catch(Exception e) {
				FactionsPlus.get().logError(e);
			}
		}
	}
	
	// ------------------------------
	// Fields
	// ------------------------------
	
	private String sectionName;
	private String fieldName;
	private String fieldDescription;
	private Boolean configurationField = false;
	
	// ------------------------------
	// Methods
	// ------------------------------
	
	// Fetch the section name
	public String getSectionName() { return this.sectionName; }
	
	// Fetch the field name
	public String getFieldName() { return this.fieldName; }
	
	// Fetch the field description
	public String getFieldDescription() { return this.fieldDescription; }
	
	// Is it a configuration field
	public Boolean isConfigurationField() { return this.configurationField; }
}
