package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class Section_Rules {
	
	@Option(oldAliases_alwaysDotted={
		"rules.enableRules"
		,"enableRules"
		}, realAlias_inNonDottedFormat = "enabled" )
	public boolean enabled=true;
	
	
	@Option(oldAliases_alwaysDotted={
		"leadersCanSetRules"
		}, realAlias_inNonDottedFormat = "leadersCanSetRules" )
	public boolean leadersCanSetRules=true;
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanSetRules"
		}, realAlias_inNonDottedFormat = "officersCanSetRules" )
	public boolean officersCanSetRules=true;
	
	
	@Option(oldAliases_alwaysDotted={
		"maxRulesPerFaction"
		}, realAlias_inNonDottedFormat = "maxRulesPerFaction" )
	public int maxRulesPerFaction=12;
}
