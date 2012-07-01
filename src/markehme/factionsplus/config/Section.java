package markehme.factionsplus.config;

import java.lang.annotation.*;



/**
 * use this only on fields that you want to be Sections (in config.yml)<br>
 * the fields on which we apply this annotation can be static only if they are in the root class(to prevent eclipse having to
 * suggest you use the class in front of the dot ie. Config.extras.lwc.disableSomething would become
 * Section_LWC.disableSomething which
 * supposedly is not what we want; why? because we want all configs to be seen as starting with Config; though if you don't care
 * about this
 * then it could be done, having subsections's fields as static <br>
 * PLEASE keep the first letter lowercase, unless you're prepared to capitalize the field also while being aware that you'll
 * have to use ie. Config.Teleports.enabled instead of Config.teleports.enabled<br>
 * <br>
 * BEWARE: do not rename fields annotated with this annotation, you can but if you do, you'll have to add an oldalias for every
 * leaf that is part of this Section, that is, if you care about importing the old values, which likely are saved in config.yml
 * already, and have
 * them transferred to the newly named fields ie. If you rename teleports from Config.telepors.enabled to teleportings then,
 * you'll have to add
 * oldaliases to fields like teleportings.enabled with value like "teleports.enabled" in order to have those imported from
 * config.yml and map to
 * the same field, which now has different dotted format due to the rename you did aka it's "teleportings.enabled" now<br>
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( {
	ElementType.FIELD
} )
public @interface Section {
	
	/**
	 * comment to be added above of this config option, inside the config file<br>
	 * named value() so you can use @ConfigSection({"comments here","next line"})
	 */
	String[] comments() default "";
	
	
	// this won't need old aliases, but it will need 1 alias
	/**
	 * this is one name that this (sub)section has in the config, ie. "extras" or "teleports" or "lwc"<br>
	 * if this name changes, then you'll have to add an old alias to each of the configoptions inside this section and insider
	 * its sections etc. to all leaves that area @ConfigOption<br>
	 * this also allows the alias to be capitalized, if it were just the field's name considered, it would be against java naming policy
	 */
	String realAlias_neverDotted();
}
