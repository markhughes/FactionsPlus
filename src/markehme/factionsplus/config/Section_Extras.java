package markehme.factionsplus.config;


public class Section_Extras {
	
	
	@ConfigOption(oldAliases={
		"leaderCanNotBeBanned"
		,"disableUpdateCheck"
	})
	public boolean disableUpdateCheck=false;
	
	
	public static final SubSection_LWC lwc=new SubSection_LWC();
	
	
	public final SubSection_Disguise disguise=new SubSection_Disguise();  
	
}
