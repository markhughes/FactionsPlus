package markehme.factionsplus.FactionsBridge;

import java.lang.reflect.*;
import java.util.*;

import markehme.factionsplus.*;



public abstract class Reflective {
	
	/**
	 * will map all souce fields to destination<br>
	 * 
	 * @param destinationMap
	 * @param sourceEnum
	 * @param destinationEnum
	 */
	public static <K extends Object, V extends Object> void mapEnums( Map<K, V> destinationMap, String sourceEnum,
		Class<V> destinationEnum )
	{
		mapEnums( destinationMap, sourceEnum, destinationEnum, null, false );
	}
	
	
	public static <K extends Object, V extends Object> void mapEnums( Map<K, V> destinationMap, String sourceEnum,
		Class<V> destinationEnum, Map<String, V> mapOfRenameSourceNameToDestEnum, boolean skipNotFoundSourceEnums )
	{
		Class<?> sourceClass;
		try {
			sourceClass = Class.forName( sourceEnum );
		} catch ( ClassNotFoundException e1 ) {
			e1.printStackTrace();
			throw FactionsPlusPlugin.bailOut( "Cannot find class " + sourceEnum );
		}
		
		mapEnumsToSome( destinationMap, sourceClass, destinationEnum, mapOfRenameSourceNameToDestEnum, skipNotFoundSourceEnums );
	}
	
	
	/**
	 * it will try to map every field of class/enum sourceEnum which is of the same type as sourceEnum (since it is enum)
	 * to their same named counterpart in the destinationEnum class (which is again expected to be enum,
	 * so all its fields must be of the same type as this destinationEnum class) even though the two enum classes are
	 * (of) different (type)
	 * 
	 * @param destinationMap
	 *            maps that holds source->dest tuple
	 * @param sourceEnumClass
	 *            this may or may not exist at compile time, but it's assumed to exist at run time(if you called this)<br>
	 *            this is why its Class type cannot be specified
	 * @param destinationEnum
	 *            exists at compile time; can contain more enums than the source
	 * @param mapOfRenameSourceNameToDestEnum
	 *            if the sourceEnum is not found in the map, then it will try to find the field with the same name as
	 *            destinationEnum<br>
	 *            if null or empty map, it won't be used, meaning source field name must match the destination name, else throw
	 * @param skipNotFoundSourceEnums
	 *            if true, only those enums that are found in destination(or map) will be mapped<br>
	 *            else, if false, all source enums will be mappes and throw if fail<br>
	 */
	public static <K extends Object, V extends Object> void mapEnumsToSome( Map<K, V> destinationMap, Class<?> sourceEnumClass,
		Class<V> destinationEnum, Map<String, V> mapOfRenameSourceNameToDestEnum, boolean skipNotFoundSourceEnums )
	{
		boolean useMap = ( null != mapOfRenameSourceNameToDestEnum ) && ( !mapOfRenameSourceNameToDestEnum.isEmpty() );
		Field[] allFieldOfDestEnum;
		if ( useMap ) {
			allFieldOfDestEnum = destinationEnum.getFields();
		} else {
			allFieldOfDestEnum = null;// won't be used
		}
		
		for ( Field eachSourceField : sourceEnumClass.getFields() ) {
			String sourceFieldName = eachSourceField.getName();
			
			boolean failed = false;
			try {
				if ( ( sourceEnumClass.equals( eachSourceField.getType() ) ) ) {// get only the enums (aka same type as
																				// enumclass)
					
					Field destField = null;
					
					if ( useMap ) {
						V destEnumInstance = mapOfRenameSourceNameToDestEnum.get( sourceFieldName );
						if ( null == destEnumInstance ) {
							if ( skipNotFoundSourceEnums ) {
								continue;
							} else {
								FactionsPlusPlugin
									.severe( "plugin author forgot add a mapping for field name of source in the "
										+ "mapping... unmapped sourceFieldName=" + sourceFieldName );
								failed = true;
								return;
							}
						}
						
						for ( int i = 0; i < allFieldOfDestEnum.length; i++ ) {// it's not null!
							if ( destEnumInstance == allFieldOfDestEnum[i].get( destinationEnum ) ) {
								destField = allFieldOfDestEnum[i];
							}
						}
					}
					
					// if using map and it's null (or not using map at all) then try to find real one
					if ( ( !useMap ) || ( null == destField ) ) {
						destField = destinationEnum.getField( sourceFieldName );
						if ( null == destField ) {// but this one can be
							if ( !skipNotFoundSourceEnums ) {
								FactionsPlusPlugin.severe( "we cannot find a destination in enum class `" + destinationEnum
									+ "` for the source field `" + eachSourceField + "` "
									+ ( useMap ? "and we did check the map first, then we" : "we only" )
									+ " tried to find a match in the destination enum" );
								failed = true;
								return;
							} else {
								continue;// skip to next source field, ignoring sources that have no destination
							}
						}
					}
					
					if ( !destField.getType().equals( destinationEnum ) ) {
						// typically this won't be reached
						FactionsPlusPlugin.severe( "plugin author has set the wrong field type in " + destinationEnum + " for "
							+ eachSourceField + " it should be of the same type as the class" );
						failed = true;
						return;
					}
					
					// TODO: some redundant code here(from above 1 of 2 cases) which I can't be bother to compress right now:)
					V ourFieldInstance = (V)( destField.get( destinationEnum ) );
					K factionsFieldInstance = (K)eachSourceField.get( sourceEnumClass );
					destinationMap.put( factionsFieldInstance, ourFieldInstance );
					FactionsPlus.warn("mapped `"+sourceFieldName+"` to `"+ourFieldInstance+"`");
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
					throw FactionsPlusPlugin.bailOut( "the plugin author forgot to define some flags in " + destinationEnum
						+ " for " + eachSourceField );
				}
			}
		}
	}
}
