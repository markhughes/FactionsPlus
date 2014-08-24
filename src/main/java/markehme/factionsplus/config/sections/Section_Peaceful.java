package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.Option;


public final class Section_Peaceful {
	
	@Option(
		autoComment={
			"if all these 3 options below are `false` then `/f togglestate` command won't be available"
			," you won't see it in `/f help`"
			,"if any of these 3 is `true` then `/f toggle` should be in `/f help`"
			,"but to make the command appear or disappear from help after changing these"
			," you have to restart the server"
		}
		,oldAliases_alwaysDotted={
		"leadersCanToggleState"
		}, realAlias_inNonDottedFormat = "leadersCanToggleState" )
	public  final _boolean leadersCanToggleState=new _boolean(false);
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanToggleState"
		}, realAlias_inNonDottedFormat = "officersCanToggleState" )
	public  final _boolean officersCanToggleState=new _boolean(false);
	
	
	@Option(oldAliases_alwaysDotted={
		"membersCanToggleState"
		}, realAlias_inNonDottedFormat = "membersCanToggleState" )
	public  final _boolean membersCanToggleState=new _boolean(false);
	
	
	@Option(oldAliases_alwaysDotted={
		"enablePeacefulBoosts"
		}, realAlias_inNonDottedFormat = "enablePeacefulBoosts" )
	public  final _boolean enablePeacefulBoosts=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"powerBoostIfPeaceful"
		}, realAlias_inNonDottedFormat = "powerBoostIfPeaceful" )
	public  final _int powerBoostIfPeaceful=new _int(20);
	

}
