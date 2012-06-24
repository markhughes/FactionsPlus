package markehme.factionsplus.extras;

import java.lang.reflect.*;
import java.util.*;

import markehme.factionsplus.*;



public abstract class Reflective {
	
	public static <K extends Object, V extends Object> void mapEnums( AbstractMap<K, V> destinationMap,
		String sourceEnum, Class destinationEnum )
	{
		
		Class<?> sourceClass;
		try {
			sourceClass = Class.forName( sourceEnum );
		} catch ( ClassNotFoundException e1 ) {
			e1.printStackTrace();
			throw FactionsPlus.bailOut( "Cannot find class " + sourceEnum );
		}
		
		for ( Field eachField : sourceClass.getFields() ) {
			boolean failed = false;
			try {
				if ( ( sourceClass.equals( eachField.getType() ) ) ) {
					V ourFieldInstance = (V)destinationEnum.getField( eachField.getName() ).get( destinationEnum );
					K factionsFieldInstance = (K)eachField.get( sourceClass );
					destinationMap.put( factionsFieldInstance, ourFieldInstance );
				}
			} catch ( IllegalArgumentException e ) {// I didn't want to catch Exception e though
				e.printStackTrace();
				failed = true;
			} catch ( IllegalAccessException e ) {
				e.printStackTrace();
				failed = true;
			} catch ( NoSuchFieldException e ) {
				e.printStackTrace();
				failed = true;
			} catch ( SecurityException e ) {
				e.printStackTrace();
				failed = true;
			} finally {
				if ( failed ) {
					// this will likely never happen, unless Factions' authors add new flags in 1.7's Relation enum
					throw FactionsPlus.bailOut( "the plugin author forgot to define some flags in "
						+ FactionsAny.Relation.class + " for " + eachField );
				}
			}
		}
	}
}
