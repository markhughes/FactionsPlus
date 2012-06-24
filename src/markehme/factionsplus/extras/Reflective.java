package markehme.factionsplus.extras;

import java.lang.reflect.*;
import java.util.*;

import markehme.factionsplus.*;



public abstract class Reflective {
	
	/**
	 * it will try to map every field of class/enum sourceEnum which is of the same type as sourceEnum (since it is enum)
	 * to their same named counterpart in the destinationEnum class (which is again expected to be enum, 
	 * so all its fields must be of the same type as this destinationEnum class) even though the two enum classes are 
	 * (of) different (type)
	 * @param destinationMap
	 *            maps that holds source->dest tuple
	 * @param sourceEnum
	 *            this may or may not exist at compile time, but it's assumed to exist at run time(if you called this)
	 * @param destinationEnum
	 *            exists at compile time
	 */
	public static <K extends Object, V extends Object> void mapEnums( AbstractMap<K, V> destinationMap,
		String sourceEnum, Class<V> destinationEnum )
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
					Field destField = destinationEnum.getField( eachField.getName() );
					if ( !destField.getType().equals( destinationEnum ) ) {
						//tipically this won't be reached
						FactionsPlus.severe( "plugin author has set the wrong field type in " + destinationEnum
							+ " for " + eachField + " it should be of the same type as the class" );
						failed = true;
					}
					V ourFieldInstance = (V)( destField.get( destinationEnum ) );
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
