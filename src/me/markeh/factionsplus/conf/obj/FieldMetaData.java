package me.markeh.factionsplus.conf.obj;

import java.lang.reflect.Field;
import java.util.HashMap;

import me.markeh.factionsplus.FactionsPlus;

public class FieldMetaData {
	
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
	
	public String getSectionName() {
		return this.sectionName;
	}
	
	public String getFieldName() {
		return this.fieldName;
	}
	
	public String getFieldDescription() {
		return this.fieldDescription;
	}
	
	public Boolean isConfigurationField() {
		return this.configurationField;
	}
}
