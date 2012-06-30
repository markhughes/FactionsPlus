package markehme.factionsplus.config;

import java.lang.annotation.*;



/**
 * use this only on fields that are of type {@link Section} the fields on which we apply this annotation can be static<br>
 * PLEASE keep the first letter lowercase, unless you're prepared to capitalize the field also while being aware that you'll
 * have to use ie. Config.Teleports.enabled instead of Config.teleports.enabled<br>
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( {
	ElementType.FIELD
} )
public @interface ConfigSection {
	
	/**
	 * comment to be added above of this config option, inside the config file<br>
	 * named value() so you can use @ConfigSection({"comments here","next line"})
	 */
	String[] value() default "";
	
	// this won't need old aliases
}
