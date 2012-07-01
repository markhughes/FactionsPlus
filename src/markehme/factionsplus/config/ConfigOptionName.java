package markehme.factionsplus.config;

import java.lang.reflect.*;
import java.util.*;

import markehme.factionsplus.util.*;



public abstract class ConfigOptionName {// must be abstract, we can't have it instanced, use subclasses instead!
	/**
	 * returns the dotted format of this config option's name<br>
	 * ie. "extras.lwc.disableSomething" not just "disableSomething"
	 */
	public String									_dottedName_asString			= null;
	
	
	
	
	@Override
	public String toString() {
		throw new RuntimeException( "possible wrong usage detected, if you were trying to refer to a config option "
			+ "then append ._ for example: if (!extras.lwc.disableSomething._) {...}" );
	}
}
