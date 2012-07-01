package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class Section_Jails {
	
	@Option(oldAliases_alwaysDotted={
		"jails.enableJails"
		,"enableJails"
		}, realAlias_inNonDottedFormat = "enabled" )
	public boolean enabled=true;
	
	
	@Option(oldAliases_alwaysDotted={
//		"jails.leadersCanSetJails"
		"leadersCanSetJails"
		}, realAlias_inNonDottedFormat = "leadersCanSetJails" )
	public boolean leadersCanSetJails=true;
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanSetJails"
		}, realAlias_inNonDottedFormat = "officersCanSetJails" )
	public boolean officersCanSetJails=true;
	
	
	@Option(oldAliases_alwaysDotted={
		"membersCanSetJails"
		}, realAlias_inNonDottedFormat = "membersCanSetJails" )
	public boolean membersCanSetJails=false;
	
	
	@Option(oldAliases_alwaysDotted={
		"leadersCanJail"
		}, realAlias_inNonDottedFormat = "leadersCanJail" )
	public boolean leadersCanJail=true;
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanJail"
		}, realAlias_inNonDottedFormat = "officersCanJail" )
	public boolean officersCanJail=true;
	
	
	
}
