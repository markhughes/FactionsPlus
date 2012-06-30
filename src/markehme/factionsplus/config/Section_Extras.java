package markehme.factionsplus.config;


public class Section_Extras {
	//XXX: I may use the terms ID, key, alias, config option   interchangeably to mean the same thing.
	
	@ConfigOption(oldAliases={
		"leaderCanNotBeBanned"
		,"disableUpdateCheck"
	})
	public boolean disableUpdateCheck=false;
	
	@ConfigOption
	public static final SubSection_LWC lwc=new SubSection_LWC();
	
	@ConfigOption
	public final SubSection_Disguise disguise=new SubSection_Disguise();  
	
}
