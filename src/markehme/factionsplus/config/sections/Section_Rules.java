package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public final class Section_Rules {
	
	@Option(oldAliases_alwaysDotted={
		"rules.enableRules"
		,"enableRules"
		}, realAlias_inNonDottedFormat = "enabled" )
	public static final _boolean enabled=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"leadersCanSetRules"
		}, realAlias_inNonDottedFormat = "leadersCanSetRules" )
	public static final _boolean leadersCanSetRules=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"officersCanSetRules"
		}, realAlias_inNonDottedFormat = "officersCanSetRules" )
	public static final _boolean officersCanSetRules=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"maxRulesPerFaction"
		}, realAlias_inNonDottedFormat = "maxRulesPerFaction" )
	public static final _int maxRulesPerFaction=new _int(12);
}
