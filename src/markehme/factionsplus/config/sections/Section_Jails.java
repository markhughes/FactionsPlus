package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public final class Section_Jails {
	
	@Option(oldAliases_alwaysDotted={
		"jails.enableJails"
		,"enableJails"
		}, realAlias_inNonDottedFormat = "enabled" )
	public static final _boolean enabled=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
//		"jails.leadersCanSetJails"
		"leadersCanSetJails"
		}, realAlias_inNonDottedFormat = "leadersCanSetJails" )
	public static final _boolean leadersCanSetJails=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanSetJails"
		}, realAlias_inNonDottedFormat = "officersCanSetJails" )
	public static final _boolean officersCanSetJails=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"membersCanSetJails"
		}, realAlias_inNonDottedFormat = "membersCanSetJails" )
	public static final _boolean membersCanSetJails=new _boolean(false);
	
	
	@Option(oldAliases_alwaysDotted={
		"leadersCanJail"
		}, realAlias_inNonDottedFormat = "leadersCanJail" )
	public static final _boolean leadersCanJail=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanJail"
		}, realAlias_inNonDottedFormat = "officersCanJail" )
	public static final _boolean officersCanJail=new _boolean(true);
	
	
	
}
