package markehme.factionsplus.config;

import java.lang.annotation.*;



/**
 * - use this for fields of any type but they should probably be of a subclass of {@link _Base}<br>
 * - the fields on which we apply this annotation CANNOT BE STATIC (well in
 * all honestly it's possible but we want to enforce Config.jails.enable instead of Section_Jails.enable (which, the latter, may
 * be forced by Eclipse or you'd get a warning on first one as to use the latter - if fields are static like that)<br>
 * - you can rename all the fields to anything you like, it won't affect the way they are name in config.yml because for that,
 * the realAlias is used<br> 
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( {
	ElementType.FIELD
} )
public @interface Option {
	
	// the ID that would be used in the config file
	// String realID();
	/**
	 * comment to be added above of this config option, inside the config file
	 */
	String[] comment() default {};
	
	
	/**
	 * this is the current alias which will be found in config.yml upon writing it<br>
	 * it should be in simple (non-dotted) format, and should thus not include it's parents,
	 * should be in non-dotted format and when computed it will have its parents added on it as prefix composing
	 * it's dotted format
	 * but this is done inside the program ie. first one should always be just the name of the option(usually it's the same name
	 * as the name of the field)
	 * so, you would type "disableUpdates" instead of "extras.disableUpdates" assuming it's a field in the extras field which is
	 * a @ConfigSection<br>
	 */
	String realAlias_inNonDottedFormat();
	
	
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
	 * ____disableSomething: true" (where _ is space)<br>
	 * <br>
	 * all aliases are case sensitive!<br>
	 */
	String[] oldAliases_alwaysDotted() default {};
	
}
