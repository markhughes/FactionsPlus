package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public final class Section_Banning {
	
	@Option(oldAliases_alwaysDotted={
		"banning.enableBans"
		,"enableBans"
	}, realAlias_inNonDottedFormat = "enabled" )
	public  final _boolean enabled=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"leaderCanNotBeBanned"
	}, realAlias_inNonDottedFormat = "leaderCanNotBeBanned" )
	public  final _boolean leaderCanNotBeBanned=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"leadersCanFactionBan"
	}, realAlias_inNonDottedFormat = "leadersCanFactionBan" )
	public  final _boolean leadersCanFactionBan=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanFactionBan"
	}, realAlias_inNonDottedFormat = "officersCanFactionBan" )
	public  final _boolean officersCanFactionBan=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"leadersCanFactionUnban"
	}, realAlias_inNonDottedFormat = "leadersCanFactionUnban" )
	public  final _boolean leadersCanFactionUnban=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanFactionUnban"
	}, realAlias_inNonDottedFormat = "officersCanFactionUnban" )
	public  final _boolean officersCanFactionUnban=new _boolean(true);
}
