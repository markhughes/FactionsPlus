package markehme.factionsplus.config;

import java.lang.annotation.*;


/**
 * use this for fields of any type except {@link Section}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.FIELD} )
public @interface ConfigOption {

	/**
	 * comment to be added above of this config option, inside the config file
	 */
	String[] comment() default "";

	/**
	 * will attempt to import values of the old alias if it's found, and the new name/alias doesn't exist already 
	 */
	String[] oldAliases() default {};
	
}
