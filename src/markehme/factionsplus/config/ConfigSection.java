package markehme.factionsplus.config;

import java.lang.annotation.*;


/**
 * use this only on fields that are of type {@link Section}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.FIELD} )
public @interface ConfigSection {
	/**
	 * comment to be added above of this config option, inside the config file<br>
	 * named value() so you can use @ConfigSection({"comments here","next line"})
	 */
	String[] value() default "";
	
	//this won't need old aliases
}
