package markehme.factionsplus.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 * use this only on fields that you want to be Sections (in config.yml)<br>
 * the fields on which we apply this annotation can be static only if they are in the root class(to prevent eclipse having to
 * suggest you use the class in front of the dot ie. Config.extras.lwc.disableSomething would become
 * Section_LWC.disableSomething which
 * supposedly is not what we want; why? because we want all configs to be seen as starting with Config; though if you don't care
 * about this
 * then it could be done, having subsections's fields as static, NOTE: however that constraint above is enforce by code (it will err)<br>
 * The name of the fields is irrelevant, only the realAlias has any effect in config.yml<br>
 * <br>
 * BEWARE: if you care about importing the old values, which likely are saved in config.yml already, and have them transferred 
 * aka upgraded/transformed carry over their value to the newly named realAlias (assuming you changed it) 
 * you'll have to add the old value of realAlias to top of oldAliases<br>
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( {
	ElementType.FIELD
} )
public @interface Section {
	
	/**
	 * currently unused! (for Sections)
	 * comment to be added above of this config option, inside the config file<br>
	 * named value() so you can use @ConfigSection({"comments here","next line"})
	 */
	String[] comments() default {};
	
	
	// this won't need old aliases, but it will need 1 alias
	/**
	 * this is one name that this (sub)section has in the config, ie. "extras" or "teleports" or "lwc"<br>
	 * if this name changes, then you'll have to add an old alias to each of the configoptions inside this section and insider
	 * its sections etc. to all leaves that area @Option<br>
	 * this also allows the alias to be capitalized, if it were just the field's name considered, it would be against java naming policy
	 */
	String realAlias_neverDotted();
}
