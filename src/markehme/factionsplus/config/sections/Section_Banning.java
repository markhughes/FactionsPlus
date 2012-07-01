package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public final class Section_Banning {
	
	@Option(oldAliases_alwaysDotted={
		"banning.enableBans"
		,"enableBans"
	}, realAlias_inNonDottedFormat = "enabled" )
	public static final _boolean enabled=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"leaderCanNotBeBanned"
	}, realAlias_inNonDottedFormat = "leaderCanNotBeBanned" )
	public static final _boolean leaderCanNotBeBanned=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"leadersCanFactionBan"
	}, realAlias_inNonDottedFormat = "leadersCanFactionBan" )
	public static final _boolean leadersCanFactionBan=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanFactionBan"
	}, realAlias_inNonDottedFormat = "officersCanFactionBan" )
	public static final _boolean officersCanFactionBan=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"leadersCanFactionUnban"
	}, realAlias_inNonDottedFormat = "leadersCanFactionUnban" )
	public static final _boolean leadersCanFactionUnban=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanFactionUnban"
	}, realAlias_inNonDottedFormat = "officersCanFactionUnban" )
	public static final _boolean officersCanFactionUnban=new _boolean(true);
}
