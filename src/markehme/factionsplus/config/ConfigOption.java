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
	 * will attempt to import values of the old alias if it's found, and the new name/alias doesn't exist already<br>
	 * add these in order: topmost(first) ones will override the ones below(/last)<br>
	 * ie. a,b,c,d<br>
	 * a overrides b, b overrides c, c overrides d<br>
	 * so if d is found and is 10, but b was already found as 4, the value would be 4, and if a is later found as 5, then
	 * the value for this configoption will then be 5<br>
	 * they should be in DOTTED format: "extras.lwc.disableSomething" for<br>
	 * "extras:<br>
	 * __lwc:<br>
	 * ____disableSomething: true"  (where _ is space)<br>
	 */
	String[] oldAliases() default {};
	
}
