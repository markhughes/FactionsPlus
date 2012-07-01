package markehme.factionsplus.config;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

import markehme.factionsplus.*;
import markehme.factionsplus.util.*;



public abstract class Typeo {
	/**
	 * we enforce fields that are @Section to have this prefix, so that we can easily use Config._extras for example and when at
	 * Config._ it would code-completion-show only the sections
	 */
	private static final String				SECTION_PREFIX			= "_";
	
	
	/**
	 * all new option names and their old aliases in the same set<br>
	 * used to quickly point to the right Field.class<br>
	 */
	private static final HashMap<String, Field>		dottedAllAliases_to_Fields	= new HashMap<String, Field>();
	
	// this is the order in which the config options will be written in the config.yml
	private static final TypedLinkedList<Field>		orderedListOfFields				= new TypedLinkedList<Field>();
	
	// basically cached the reflection here:
	private static final HashMap<Field, String[]>	fieldToOldAliasesArray				= new HashMap<Field, String[]>();
	
	// field to dotted form of alias
	private static final HashMap<Field, String>		fieldToRealAlias				= new HashMap<Field, String>();
	
	
	// protected static final TypedLinkedList<TYPE>
	
	// this field would be updated by some outside method to be the fully dotted format of the config option ie.
	// "extras.lwc.disableSomething"
	// not just "disableSomething", you dig?
	
	protected static final Field getField_correspondingTo_DottedFormat(String thisDottedFormat) {
		assert isValidAliasFormat( thisDottedFormat );
		return dottedAllAliases_to_Fields.get( thisDottedFormat);
	}
	
	protected static final String[] getListOfOldAliases( Field forField ) {
		assert Q.nn( forField );
		String[] oldAliases = fieldToOldAliasesArray.get( forField );
		assert Q.nn( oldAliases );// cannot be null due to "default {}" inside the {@link @Option} annotation
		return oldAliases;
	}
	
	
	protected static final String getDottedRealAliasOfField( Field field ) {
		assert Q.nn( field );
		String realAlias = fieldToRealAlias.get( field );
		assert isValidAliasFormat( realAlias );
		return realAlias;
	}
	
	
	protected static final boolean isValidAliasFormat( String testAlias ) {
		if ( ( null != testAlias ) && ( !testAlias.isEmpty() ) && ( testAlias.charAt( 0 ) != Config.DOT )
			&& ( testAlias.charAt( testAlias.length() - 1 ) != Config.DOT ) )
		{
			return true;
		}
		return false;
	}
	
	
	protected static final boolean isNONdottedAlias( String testAlias ) {
		if ( ( null != testAlias ) && ( !testAlias.isEmpty() ) && ( testAlias.indexOf( Config.DOT ) < 0 ) ) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * @param realAliasDotted
	 * @param field
	 * @return previous, if existed, else null
	 */
	protected static final Field putAnyAliasDotted_to_Field( String realAliasDotted, Field field ) {
		assert isValidAliasFormat( realAliasDotted );
		assert null != field;
		Field existingField = dottedAllAliases_to_Fields.put( realAliasDotted, field );
		
		return existingField;
	}
	

	protected static void sanitize_AndUpdateClassMapping( Class rootClass ) {
		// since we don't change the annotations on the config options inside the classes on runtime, this will only be called
		// onEnable
		// just in case 'reload' was executed and a new FactionsPlus.jar was loaded (do not use Plugin.onLoad() it's evil)
		synchronized ( Typeo.class ) {
			dottedAllAliases_to_Fields.clear();
			parsify( rootClass, null, rootClass );
		}
	}
	
	
	/**
	 * this works only on the config options present in java code, none from config.yml here<br>
	 * must be inside a synchronized ( ConfigOptionName.dottedClassOptions_To_Fields ) block<br>
	 * 
	 * @param rootClass
	 * @param dottedParentSection
	 * @param parentInstance
	 */
	private static void parsify( Class<?> rootClass, String dottedParentSection, Object parentInstance ) {
		Field[] allFields = rootClass.getDeclaredFields();
		boolean isTopLevelSection = ( null == dottedParentSection ) || dottedParentSection.isEmpty();

		for ( int why = 0; why < allFields.length; why++ ) {
			// System.out.println( "F: "+allFields[i] );
			Field field = allFields[why];
			Annotation[] currentFieldAnnotations = field.getDeclaredAnnotations();
			for ( int jay = 0; jay < currentFieldAnnotations.length; jay++ ) {
				// System.out.println("A: "+ currentFieldAnnotations[j] );
				Annotation fieldAnnotation = currentFieldAnnotations[jay];
				Class<? extends Annotation> annotationType = fieldAnnotation.annotationType();
				if ( ( Section.class.equals( annotationType ) ) || ( Option.class.equals( annotationType ) ) ) {
					
					
					// XXX: @ConfigOption fields must not be static(or private), they would bypass the chain tree ie.
					// Section_Jails.enabled instead of Config.jails.enabled
					// the non-private constraint is mostly because we couldn't access it via Config.jails.enabled if
					// enabled is private
					// but protected is allowed, assuming you know what you're doing and you're using that only in the same
					// package
					int fieldModifiers = field.getModifiers();
					Class<?> typeOfField = field.getType();// get( rootClass );
					
					
					Object fieldInstance;
					try {
						fieldInstance = field.get( parentInstance );
					} catch ( Exception e ) {
						e.printStackTrace();
						throw FactionsPlus.bailOut( "bad coding: the field `" + field + "` doesn't have an instance, "
							+ "did you forget to add `= new ...();`" );
						// this means that you couldn't use Config.thisfield.itschildfield because thisfield would be null and
						// NPE
						// ie. Config.extras.lwc would NPE if `extras` doesn't have an instance, make sure
						// that extras is new-ed like: Classhere extras=new Classhere(); in Config
					}
					
					
					
					
					// must be non-private , but yes Final!
					boolean badMods = !( !Modifier.isPrivate( fieldModifiers ) && ( Modifier.isFinal( fieldModifiers ) ) );
					
					// allowed like this for some clarity:
					if ( isTopLevelSection ) {
						// it's toplevel section, should NOT BE private, should BE FINAL and STATIC
						badMods |= !Modifier.isStatic( fieldModifiers );
					} else {
						badMods |= Modifier.isStatic( fieldModifiers );
						// subsection should, NOT be static, NOT be private, but BE FINAL
					}
					
					if ( badMods ) {
						// means, we're currently examining a subsection, cause we allow toplevel sections to be static. ie.
						// Config.extras
						// but we don't allow Config.extras.lwc to be static, cause it would mean we have to use
						// SubSection_LWC to access lwc's fields
						// do you dig? we basically want to enforce using Config.toplevelsection to every subsection or
						// field
						throw FactionsPlus.bailOut( "bad coding: your @" + annotationType.getSimpleName()
							+ " field must be final+non-private+" + ( isTopLevelSection ? " static" : "non-static" )
							+ " but instead it is: `" + field + "`" );
					}
					
					if ( Section.class == annotationType ) {
						
						String realAlias = ( (Section)fieldAnnotation ).realAlias_neverDotted();
						assert realAlias.indexOf( Config.DOT ) < 0 : "realAlias should never be dotted: `" + realAlias + "`";
						String dotted = ( isTopLevelSection ? realAlias : dottedParentSection + Config.DOT + realAlias );
						
						if ( !field.getName().startsWith( SECTION_PREFIX ) ) {
							throw FactionsPlus.bailOut( "bad coding: by convention any @" + annotationType.getSimpleName()
								+ " aka sections should have their field name start with `" + SECTION_PREFIX
								+ "`. Please correct in source code this field: `"+field+"`" );
						}
						// FactionsPlus.info( "Section: " + allFields[i] + "//" + currentFieldAnnotations[j] );
						parsify( typeOfField, dotted, fieldInstance );// recurse
						
					} else {// it's @ConfigOption
					
						// if ( !Modifier.isStatic( fieldModifiers ) || Modifier.isPrivate( fieldModifiers )
						// || !Modifier.isFinal( fieldModifiers ) )
						// // && ( null != dottedParentSection ) )
						// {
						// // means, we're currently examining a subsection, cause we allow toplevel sections to be static. ie.
						// // Config.extras
						// // but we don't allow Config.extras.lwc to be static, cause it would mean we have to use
						// // SubSection_LWC to access lwc's fields
						// // do you dig? we basically want to enforce using Config.toplevelsection to every subsection or
						// // field
						// throw FactionsPlus.bailOut( "bad coding: your @" + annotationType.getSimpleName()
						// + " config option field must be public final static, but instead it is: `" + field + "`" );
						// }
						
						// we already know it has an instance ie. it's new-ed
						if ( !ConfigOptionName.class.isAssignableFrom( typeOfField ) ) {
							throw FactionsPlus.bailOut( "bad coding: the type of field `" + field + "` is not a subclass of `"
								+ ConfigOptionName.class + "`" );
						}
						
						
						String realAlias = ( (Option)fieldAnnotation ).realAlias_inNonDottedFormat();
						assert realAlias.indexOf( Config.DOT ) < 0 : "realAlias should never be dotted: `" + realAlias + "`";
						String currentDotted = ( isTopLevelSection ? realAlias : dottedParentSection + Config.DOT + realAlias );
						
						
						// must update the dotted form in the instance, because we know it now
						( (ConfigOptionName)fieldInstance )._dottedName_asString = currentDotted;
						
						Option co = (Option)fieldAnnotation;
						String[] aliasesArray = co.oldAliases_alwaysDotted();
						int aliasesCount = aliasesArray.length;
						int current = -1;// from -1 to allow the real (field name) to be added too (first actually, tho it's non
											// ordered)
						while ( true ) {
							// this will merge realAlias with oldAliases in the same destination HashMap
							// FactionsPlus.info( currentDotted + "/" + field );
							Field existingField = Typeo.putAnyAliasDotted_to_Field( currentDotted, field );
							if ( ( null != existingField ) ) {
								FactionsPlus.bailOut( "bad coding: your config option `" + currentDotted + "` in field `"
									+ field + "`\n" + "was already defined in field `" + existingField
									+ "`\nBasically you cannot have the same oldalias for two different config options" );
							}
							// System.out.println(currentDotted);
							// next
							if ( ++current >= aliasesCount ) {
								break;
							}
							currentDotted = aliasesArray[current];
							if ( currentDotted.isEmpty() ) {
								FactionsPlus.bailOut( "bad coding: one of the oldAliases in field `" + field + "`\n"
									+ "should not be empty!!" );
							}
							// detect extra spaces(by mistake?) around the current old alias
							if ( !currentDotted.trim().equals( currentDotted ) ) {
								FactionsPlus.bailOut( "bad coding: the old alias `" + currentDotted + "` in field `" + field
									+ "`\n" + "should not contain any extra whitespaces around it!" );
							}
						}// while
							// FactionsPlus.info( "Option: " + allFields[i] + "//" + currentFieldAnnotations[j] );
					}// else we don't care
				}
			}
		}
	}
}
