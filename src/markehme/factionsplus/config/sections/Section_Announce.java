package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class Section_Announce {
	
	// TODO: maybe check to see if we can inherit 'enabled' field from a base abstract class to extend for each of the section
	// that needs it, to avoid dupe code, and set the default value for it in class constructor
	
	@Option(oldAliases_alwaysDotted = {
		"announce.enableAnnounce"
		,"enableAnnounce"
	}, realAlias_inNonDottedFormat = "enabled"  )
	public boolean	enabled	= true;
	
	
	@Option(oldAliases_alwaysDotted={
		"leadersCanAnnounce"//+(char)9 was for tests
		}, realAlias_inNonDottedFormat = "leadersCanAnnounce" )
	public boolean leadersCanAnnounce=true;
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanAnnounce"
		}, realAlias_inNonDottedFormat = "officersCanAnnounce" )
	public boolean officersCanAnnounce=true;
	
	
	
	@Option(oldAliases_alwaysDotted={
		"showLastAnnounceOnLogin"
		}, realAlias_inNonDottedFormat = "showLastAnnounceOnLogin" )
	public boolean showLastAnnounceOnLogin=true;
	
	
	
	@Option(oldAliases_alwaysDotted={
		"showLastAnnounceOnLandEnter"
		}, realAlias_inNonDottedFormat = "showLastAnnounceOnLandEnter" )
	public boolean showLastAnnounceOnLandEnter=true;
}
