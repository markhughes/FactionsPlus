package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class Section_Rules {
	
	@Option(oldAliases_alwaysDotted={
		"rules.enableRules"
		,"enableRules"
		}, realAlias_inNonDottedFormat = "enabled" )
	public _boolean enabled=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"leadersCanSetRules"
		}, realAlias_inNonDottedFormat = "leadersCanSetRules" )
	public _boolean leadersCanSetRules=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanSetRules"
		}, realAlias_inNonDottedFormat = "officersCanSetRules" )
	public _boolean officersCanSetRules=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"maxRulesPerFaction"
		}, realAlias_inNonDottedFormat = "maxRulesPerFaction" )
	public _int maxRulesPerFaction=new _int(12);
}
