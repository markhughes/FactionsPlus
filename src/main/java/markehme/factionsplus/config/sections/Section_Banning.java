package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.Option;


public final class Section_Banning {
	
	@Option(oldAliases_alwaysDotted={
		"banning.enableBans"
		,"enableBans"
	}, realAlias_inNonDottedFormat = "enabled" )
	public  final _boolean enabled=new _boolean(true);
	
	@Option(oldAliases_alwaysDotted={
		"leadersCanFactionBan"
		,"banning.leadersCanFactionBan"
	}, realAlias_inNonDottedFormat = "leadersCanFactionBanAndUnban" )
	public  final _boolean leadersCanFactionBanAndUnban=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanFactionBan"
		,"banning.officersCanFactionBan"
	}, realAlias_inNonDottedFormat = "officersCanFactionBanAndUnban" )
	public  final _boolean officersCanFactionBanAndUnban=new _boolean(true);
	
	
//	@Option(oldAliases_alwaysDotted={
//		"leadersCanFactionUnban"
//	}, realAlias_inNonDottedFormat = "leadersCanFactionUnban" )
//	public  final _boolean leadersCanFactionUnban=new _boolean(true);
//	
//	
//	@Option(oldAliases_alwaysDotted={
//		"officersCanFactionUnban"
//	}, realAlias_inNonDottedFormat = "officersCanFactionUnban" )
//	public  final _boolean officersCanFactionUnban=new _boolean(true);
	
	
	public static final String banUnBanPermissionNodeName="factionsplus.banunban";
	@Option(autoComment={
		"If set to true this will automatically restrict the use of ban/unban commands"
		,"to those leaders/officers that have `"+banUnBanPermissionNodeName+"` permission node"
		,"For example if leaders/officers can ban/unban (from the above options)"
		,"then they'll only be allowed to do so only if they have this permission node"
		,"but if this option is false then they'll be allowed (only if their option above is set to true)"
		,"No other players are allowed to use ban/unban (not even console/OPs)"
	},
		oldAliases_alwaysDotted={
	}, realAlias_inNonDottedFormat = "furtherRestrictBanUnBanToThoseThatHavePermission" )
	public  final _boolean furtherRestrictBanUnBanToThoseThatHavePermission=new _boolean(false);


	@Option(autoComment={
		"If true it allows banning of any players(that exist on the server) regardless if they are already in your faction",
		"This is so that you can prevent a certain player from joining your faction even though your faction is open and",
		"doesn't require invitation",
		"If this is false then: You can only ban players that exist in your faction"
	},
		oldAliases_alwaysDotted={
	}
	, realAlias_inNonDottedFormat = "canBanToPreventFutureJoins" )
	public  final _boolean canBanToPreventFutureJoins=new _boolean(true);
}
