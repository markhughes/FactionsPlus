package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class Section_Banning {
	
	@Option(oldAliases_alwaysDotted={
		"banning.enableBans"
		,"enableBans"
	}, realAlias_inNonDottedFormat = "enabled" )
	public boolean enabled=true;
	
	
	@Option(oldAliases_alwaysDotted={
		"leaderCanNotBeBanned"
	}, realAlias_inNonDottedFormat = "leaderCanNotBeBanned" )
	public boolean leaderCanNotBeBanned=true;
	
	
	@Option(oldAliases_alwaysDotted={
		"leadersCanFactionBan"
	}, realAlias_inNonDottedFormat = "leadersCanFactionBan" )
	public boolean leadersCanFactionBan=true;
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanFactionBan"
	}, realAlias_inNonDottedFormat = "officersCanFactionBan" )
	public boolean officersCanFactionBan=true;
	
	
	@Option(oldAliases_alwaysDotted={
		"leadersCanFactionUnban"
	}, realAlias_inNonDottedFormat = "leadersCanFactionUnban" )
	public boolean leadersCanFactionUnban=true;
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanFactionUnban"
	}, realAlias_inNonDottedFormat = "officersCanFactionUnban" )
	public boolean officersCanFactionUnban=true;
}
