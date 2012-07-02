package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public final class Section_Announce {
	
	// TODO: maybe check to see if we can inherit 'enabled' field from a base abstract class to extend for each of the section
	// that needs it, to avoid dupe code, and set the default value for it in class constructor
	
	@Option(oldAliases_alwaysDotted = {
		"announce.enableAnnounce"
		,"enableAnnounce"
	}, realAlias_inNonDottedFormat = "enabled"  )
	public  final _boolean	enabled	= new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"leadersCanAnnounce"//+(char)9 was for tests
		}, realAlias_inNonDottedFormat = "leadersCanAnnounce" )
	public  final _boolean leadersCanAnnounce=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanAnnounce"
		}, realAlias_inNonDottedFormat = "officersCanAnnounce" )
	public  final _boolean officersCanAnnounce=new _boolean(true);
	
	
	
	@Option(oldAliases_alwaysDotted={
		"showLastAnnounceOnLogin"
		}, realAlias_inNonDottedFormat = "showLastAnnounceOnLogin" )
	public  final _boolean showLastAnnounceOnLogin=new _boolean(true);
	
	
	
	@Option(oldAliases_alwaysDotted={
		"showLastAnnounceOnLandEnter"
		}, realAlias_inNonDottedFormat = "showLastAnnounceOnLandEnter" )
	public  final _boolean showLastAnnounceOnLandEnter=new _boolean(true);
}
