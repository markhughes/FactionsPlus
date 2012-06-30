package markehme.factionsplus.config;

import java.lang.reflect.*;
import java.util.*;

public abstract class ConfigOptionName {
	/**
	 * all new option names and their old aliases in the same set<br>
	 * used to quickly point to the right Field.class<br>
	 * using twoway map because, we need to use this map when retrieving the dotted format on a Field<br>
	 */
	protected static final HashMap<String, Field>	dottedClassOptions_To_Fields	= new HashMap<String, Field>();
	
	
	//this field would be updated by some outside method to be the fully dotted format of the config option ie. "extras.lwc.disableSomething"
	//not just "disableSomething", you dig?
	/**
	 * returns the dotted format of this config option's name<br>
	 * ie. "extras.lwc.disableSomething" not just "disableSomething"
	 */
	public String _dottedName_asString=null;
	
	@Override
	public String toString() {
		throw new RuntimeException("possible wrong usage detected, if you were trying to refer to a config option " +
				"then append ._ for example: if (!extras.lwc.disableSomething._) {...}");
	}
}
