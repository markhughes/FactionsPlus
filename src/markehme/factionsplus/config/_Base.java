package markehme.factionsplus.config;



/**
 * old name: ConfigOptionName
 */
public abstract class _Base {// must be abstract, we can't have it instanced, use subclasses instead!
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

	@Override
	public boolean equals( Object obj ) {
		throw new RuntimeException("do not use this, it's here to trap by mistake if you're trying to compare " +
				"the instance where the value is kept in, instead of the value field");
	}


	/**
	 * set the value from a string
	 * @param value
	 */
	public abstract void setValue( String value );



	public abstract String getValue();

	@Override
	public int hashCode() {
		throw new RuntimeException("do not use this, it's here to trap by mistake if you're trying to add these to a HashMap/HashSet" +
				"because then the .equals() comparison between then is not what you may want");
	}

	public abstract String getDefaultValue();
}
