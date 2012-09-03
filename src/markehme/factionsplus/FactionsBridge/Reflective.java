package markehme.factionsplus.FactionsBridge;

import java.lang.reflect.*;
import java.util.*;

import markehme.factionsplus.*;
import markehme.factionsplus.util.*;



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
		mapEnums( destinationMap, sourceEnum, destinationEnum, null );
	}
	
	
	public static <K extends Object, V extends Object> void mapEnums( Map<K, V> destinationMap, String sourceEnum,
		Class<V> destinationEnum, Map<String, V> mapOfRenameSourceNameToDestEnum )
	{
		Class<?> sourceClass;
		try {
			sourceClass = Class.forName( sourceEnum );
		} catch ( ClassNotFoundException e1 ) {
			e1.printStackTrace();
			throw FactionsPlusPlugin.bailOut( "Cannot find class " + sourceEnum );
		}
		
		mapEnumsToSome( destinationMap, sourceClass, destinationEnum, mapOfRenameSourceNameToDestEnum );
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
	 */
	public static <K extends Object, V extends Object> void mapEnumsToSome( Map<K, V> destinationMap, Class<?> sourceEnumClass,
		Class<V> destinationEnum, Map<String, V> mapOfRenameSourceNameToDestEnum )
	{
		assert Q.nn( destinationEnum );
		assert Q.nn( destinationMap );
		assert Q.nn( sourceEnumClass );
		
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
			Throwable error=null;
			try {
				if ( ( sourceEnumClass.equals( eachSourceField.getType() ) ) ) {// get only the enums (aka same type as
																				// enumclass)
					
					Field destField = null;
					
					if ( useMap ) {
						@SuppressWarnings( "null" )
						V destEnumInstance = mapOfRenameSourceNameToDestEnum.get( sourceFieldName );
						if ( null != destEnumInstance ) {
//							if ( !skipNotFoundSourceEnums ) {
//								// continue;
//								// }else{
//								throw FactionsPlusPlugin
//									.bailOut( "plugin author forgot add a mapping for field name of source in the "
//										+ "passed map parameter to mapEnums()... unmapped sourceFieldName=" + sourceFieldName );
//								// failed = true;
//								// return;
//							}
//						} else {
							
							for ( int i = 0; i < allFieldOfDestEnum.length; i++ ) {// it's not null!
								if ( destEnumInstance == allFieldOfDestEnum[i].get( destinationEnum ) ) {
									destField = allFieldOfDestEnum[i];
								}
							}
						}
					}
					
					// if using map and it's null (or not using map at all) then try to find real field in destination
					if ( ( !useMap ) || ( null == destField ) ) {
						destField = destinationEnum.getField( sourceFieldName );
						if ( null == destField ) {// but this one can be
//							if ( skipNotFoundSourceEnums ) {
//								continue;// skip to next source field, ignoring sources that have no destination
//							} else {
								throw FactionsPlusPlugin.bailOut( "we cannot find a destination in enum class `"
									+ destinationEnum + "` for the source field `" + eachSourceField + "` "
									+ ( useMap ? "and we did check the map first, then we" : "we only" )
									+ " tried to find a match in the destination enum" );
								// failed = true;
								// return;
//							}
						}
					}
					
					if ( !destField.getType().equals( destinationEnum ) ) {
						// typically this won't be reached
						throw FactionsPlusPlugin.bailOut( "plugin author has set the wrong field type in " + destinationEnum
							+ " for " + eachSourceField + " it should be of the same type as the class" );
						// failed = true;
						// return;
					}
					
					// TODO: some redundant code here(from above 1 of 2 cases) which I can't be bother to compress right now:)
					V ourFieldInstance = (V)( destField.get( destinationEnum ) );
					//FIXME: we should make sure that we're not caching the instance of a field which is a primitive, 
					//else it will fail to see when it gets updated, and thus still point to the old value (ie. imagine boolean 
					//which is wrapped into Boolean instace by the above Field.get())
					
					if ( !ourFieldInstance.getClass().equals( destinationEnum ) ) {
						// oh well technically this will never happen... because the field instances are not changed
						throw FactionsPlusPlugin.bailOut( "there was a clash: `" + destField
							+ "` was already changed by a miracle to type `" + ourFieldInstance.getClass() + "` which value `"
							+ ourFieldInstance );
						// failed = true;
						// return;
					}
					
					K factionsFieldInstance = (K)eachSourceField.get( sourceEnumClass );
					destinationMap.put( factionsFieldInstance, ourFieldInstance );
//					FactionsPlus.warn( destinationMap+" mapped for enum `" + sourceEnumClass + "` `" + sourceFieldName + "` to `"
//						+ ourFieldInstance + "` in destEnum=`" + destinationEnum + "`" );
				}
			} catch ( Throwable t){
				failed=true;
				error=t;
			} finally {
				if ( failed ) {
					// this will likely never happen, unless Factions' authors add new flags in 1.7's Relation enum
					throw FactionsPlusPlugin.bailOut(error, "the plugin author forgot to define some flags in " + destinationEnum
						+ " for " + eachSourceField );
				}
			}
		}
	}
}
