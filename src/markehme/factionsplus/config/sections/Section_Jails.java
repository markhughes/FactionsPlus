package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public final class Section_Jails {
	
	@Option(oldAliases_alwaysDotted={
		"jails.enableJails"
		,"enableJails"
		}, realAlias_inNonDottedFormat = "enabled" )
	public  final _boolean enabled=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
//		"jails.leadersCanSetJails"
		"leadersCanSetJails"
		}, realAlias_inNonDottedFormat = "leadersCanSetJails" )
	public  final _boolean leadersCanSetJails=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanSetJails"
		}, realAlias_inNonDottedFormat = "officersCanSetJails" )
	public  final _boolean officersCanSetJails=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"membersCanSetJails"
		}, realAlias_inNonDottedFormat = "membersCanSetJails" )
	public  final _boolean membersCanSetJails=new _boolean(false);
	
	
	@Option(oldAliases_alwaysDotted={
		"leadersCanJail"
		}, realAlias_inNonDottedFormat = "leadersCanJail" )
	public  final _boolean leadersCanJail=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanJail"
		}, realAlias_inNonDottedFormat = "officersCanJail" )
	public  final _boolean officersCanJail=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		}, realAlias_inNonDottedFormat = "denyMovementWhileJailed" )
	public  final _boolean denyMovementWhileJailed=new _boolean(true);
	
}
