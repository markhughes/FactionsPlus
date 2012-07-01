package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class Section_Peaceful {
	
	@Option(oldAliases_alwaysDotted={
		"leadersCanToggleState"
		}, realAlias_inNonDottedFormat = "leadersCanToggleState" )
	public boolean leadersCanToggleState=false;
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanToggleState"
		}, realAlias_inNonDottedFormat = "officersCanToggleState" )
	public boolean officersCanToggleState=false;
	
	
	@Option(oldAliases_alwaysDotted={
		"membersCanToggleState"
		}, realAlias_inNonDottedFormat = "membersCanToggleState" )
	public boolean membersCanToggleState=false;
	
	
	@Option(oldAliases_alwaysDotted={
		"enablePeacefulBoosts"
		}, realAlias_inNonDottedFormat = "enablePeacefulBoosts" )
	public boolean enablePeacefulBoosts=true;
	
	
	@Option(oldAliases_alwaysDotted={
		"powerBoostIfPeaceful"
		}, realAlias_inNonDottedFormat = "powerBoostIfPeaceful" )
	public int powerBoostIfPeaceful=20;
	

}
