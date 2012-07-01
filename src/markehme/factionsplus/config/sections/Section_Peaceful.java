package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public final class Section_Peaceful {
	
	@Option(oldAliases_alwaysDotted={
		"leadersCanToggleState"
		}, realAlias_inNonDottedFormat = "leadersCanToggleState" )
	public static final _boolean leadersCanToggleState=new _boolean(false);
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanToggleState"
		}, realAlias_inNonDottedFormat = "officersCanToggleState" )
	public static final _boolean officersCanToggleState=new _boolean(false);
	
	
	@Option(oldAliases_alwaysDotted={
		"membersCanToggleState"
		}, realAlias_inNonDottedFormat = "membersCanToggleState" )
	public static final _boolean membersCanToggleState=new _boolean(false);
	
	
	@Option(oldAliases_alwaysDotted={
		"enablePeacefulBoosts"
		}, realAlias_inNonDottedFormat = "enablePeacefulBoosts" )
	public static final _boolean enablePeacefulBoosts=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"powerBoostIfPeaceful"
		}, realAlias_inNonDottedFormat = "powerBoostIfPeaceful" )
	public static final _int powerBoostIfPeaceful=new _int(20);
	

}
