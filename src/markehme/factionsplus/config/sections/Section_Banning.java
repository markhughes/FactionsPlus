package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class Section_Banning {
	
	@Option(oldAliases_alwaysDotted={
		"banning.enableBans"
		,"enableBans"
	}, realAlias_inNonDottedFormat = "enabled" )
	public _boolean enabled=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"leaderCanNotBeBanned"
	}, realAlias_inNonDottedFormat = "leaderCanNotBeBanned" )
	public _boolean leaderCanNotBeBanned=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"leadersCanFactionBan"
	}, realAlias_inNonDottedFormat = "leadersCanFactionBan" )
	public _boolean leadersCanFactionBan=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanFactionBan"
	}, realAlias_inNonDottedFormat = "officersCanFactionBan" )
	public _boolean officersCanFactionBan=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"leadersCanFactionUnban"
	}, realAlias_inNonDottedFormat = "leadersCanFactionUnban" )
	public _boolean leadersCanFactionUnban=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanFactionUnban"
	}, realAlias_inNonDottedFormat = "officersCanFactionUnban" )
	public _boolean officersCanFactionUnban=new _boolean(true);
}
