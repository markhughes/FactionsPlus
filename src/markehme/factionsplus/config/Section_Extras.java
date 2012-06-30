package markehme.factionsplus.config;


public class Section_Extras {
	//XXX: I may use the terms ID, key, alias, config option   interchangeably to mean the same thing.
	
	@ConfigOption(oldAliases={
		"disableUpdateCheck"
	})
	public boolean disableUpdateCheck=false;
	
	@ConfigSection
	public final SubSection_LWC lwc=new SubSection_LWC();
	
	@ConfigSection
	public final SubSection_Disguise disguise=new SubSection_Disguise();  
	
}
