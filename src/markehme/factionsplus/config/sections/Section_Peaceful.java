package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public final class Section_Peaceful {
	
	@Option(oldAliases_alwaysDotted={
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
