package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class Section_Peaceful {
	
	@Option(oldAliases_alwaysDotted={
		"leadersCanToggleState"
		}, realAlias_inNonDottedFormat = "leadersCanToggleState" )
	public _boolean leadersCanToggleState=new _boolean(false);
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanToggleState"
		}, realAlias_inNonDottedFormat = "officersCanToggleState" )
	public _boolean officersCanToggleState=new _boolean(false);
	
	
	@Option(oldAliases_alwaysDotted={
		"membersCanToggleState"
		}, realAlias_inNonDottedFormat = "membersCanToggleState" )
	public _boolean membersCanToggleState=new _boolean(false);
	
	
	@Option(oldAliases_alwaysDotted={
		"enablePeacefulBoosts"
		}, realAlias_inNonDottedFormat = "enablePeacefulBoosts" )
	public _boolean enablePeacefulBoosts=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"powerBoostIfPeaceful"
		}, realAlias_inNonDottedFormat = "powerBoostIfPeaceful" )
	public _int powerBoostIfPeaceful=new _int(20);
	

}
