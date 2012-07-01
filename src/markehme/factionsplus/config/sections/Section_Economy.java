package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public final class Section_Economy {
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.enableEconomy"
		,"enableEconomy"
		,"economy_enable"
	}, realAlias_inNonDottedFormat = "enabled" )
	public static final _boolean enabled=new _boolean(false);
	
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToWarp"//newest
		,"economy.economycostToWarp"//newer
		,"economy_costToWarp"//very old one
		}, realAlias_inNonDottedFormat = "costToWarp" )
	public static final _double costToWarp=new _double( 0.0d );//current one (the field name)
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToCreateWarp"//newest
		,"economy.economycostToCreateWarp"//newer
		,"economy_costToCreateWarp"//very old one
		}, realAlias_inNonDottedFormat = "costToCreateWarp" )
	public static final _double costToCreateWarp=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToDeleteWarp"//newest
		,"economy.economycostToDeleteWarp"//newer
		,"economy_costToDeleteWarp"//very old one
		}, realAlias_inNonDottedFormat = "costToDeleteWarp" )
	public static final _double costToDeleteWarp=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToAnnounce"//newest
		,"economy.economycostToAnnounce"//newer
		,"economy_costToAnnounce"//very old one
		}, realAlias_inNonDottedFormat = "costToAnnounce" )
	public static final _double costToAnnounce=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToJail"//newest
		,"economy.economycostToJail"//newer
		,"economy_costToJail"//very old one
		}, realAlias_inNonDottedFormat = "costToJail" )
	public static final _double costToJail=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToSetJail"//newest
		,"economy.economycostToSetJail"//newer
		,"economy_costToSetJail"//very old one
		}, realAlias_inNonDottedFormat = "costToSetJail" )
	public static final _double costToSetJail=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToUnJail"//newest
		,"economy.economycostToUnJail"//newer
		,"economy_costToUnJail"//very old one
		}, realAlias_inNonDottedFormat = "costToUnJail" )
	public static final _double costToUnJail=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToToggleUpPeaceful"//newest
		,"economy.economycostToToggleUpPeaceful"//newer
		,"economy_costToToggleUpPeaceful"//very old one
		}, realAlias_inNonDottedFormat = "costToToggleUpPeaceful" )
	public static final _double costToToggleUpPeaceful=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToToggleDownPeaceful"//newest
		,"economy.economycostToToggleDownPeaceful"//newer
		,"economy_costToToggleDownPeaceful"//very old one
		}, realAlias_inNonDottedFormat = "costToToggleDownPeaceful" )
	public static final _double costToToggleDownPeaceful=new _double( 0.0d );
}
