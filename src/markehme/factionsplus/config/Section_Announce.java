package markehme.factionsplus.config;


public class Section_Announce {
	
	// TODO: maybe check to see if we can inherit 'enabled' field from a base abstract class to extend for each of the section
	// that needs it, to avoid dupe code, and set the default value for it in class constructor
	
	@ConfigOption(oldAliases = {
		"announce.enableAnnounce"
		,"enableAnnounce"
	} )
	public boolean	enabled	= true;
	
	
	@ConfigOption(oldAliases={
		"leadersCanAnnounce"//+(char)9 was for tests
		})
	public boolean leadersCanAnnounce=true;
	
	
	@ConfigOption(oldAliases={
		"officersCanAnnounce"
		})
	public boolean officersCanAnnounce=true;
	
	
	
	@ConfigOption(oldAliases={
		"showLastAnnounceOnLogin"
		})
	public boolean showLastAnnounceOnLogin=true;
	
	
	
	@ConfigOption(oldAliases={
		"showLastAnnounceOnLandEnter"
		})
	public boolean showLastAnnounceOnLandEnter=true;
}
