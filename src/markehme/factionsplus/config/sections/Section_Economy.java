package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class Section_Economy {
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.enableEconomy"
		,"enableEconomy"
		,"economy_enable"
	}, realAlias_inNonDottedFormat = "enabled" )
	public boolean enabled=false;
	
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToWarp"//newest
		,"economy.economycostToWarp"//newer
		,"economy_costToWarp"//very old one
		}, realAlias_inNonDottedFormat = "costToWarp" )
	public double costToWarp=0.0d;//current one (the field name)
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToCreateWarp"//newest
		,"economy.economycostToCreateWarp"//newer
		,"economy_costToCreateWarp"//very old one
		}, realAlias_inNonDottedFormat = "costToCreateWarp" )
	public double costToCreateWarp=0.0d;
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToDeleteWarp"//newest
		,"economy.economycostToDeleteWarp"//newer
		,"economy_costToDeleteWarp"//very old one
		}, realAlias_inNonDottedFormat = "costToDeleteWarp" )
	public double costToDeleteWarp=0.0d;
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToAnnounce"//newest
		,"economy.economycostToAnnounce"//newer
		,"economy_costToAnnounce"//very old one
		}, realAlias_inNonDottedFormat = "costToAnnounce" )
	public double costToAnnounce=0.0d;
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToJail"//newest
		,"economy.economycostToJail"//newer
		,"economy_costToJail"//very old one
		}, realAlias_inNonDottedFormat = "costToJail" )
	public double costToJail=0.0d;
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToSetJail"//newest
		,"economy.economycostToSetJail"//newer
		,"economy_costToSetJail"//very old one
		}, realAlias_inNonDottedFormat = "costToSetJail" )
	public double costToSetJail=0.0d;
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToUnJail"//newest
		,"economy.economycostToUnJail"//newer
		,"economy_costToUnJail"//very old one
		}, realAlias_inNonDottedFormat = "costToUnJail" )
	public double costToUnJail=0.0d;
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToToggleUpPeaceful"//newest
		,"economy.economycostToToggleUpPeaceful"//newer
		,"economy_costToToggleUpPeaceful"//very old one
		}, realAlias_inNonDottedFormat = "costToToggleUpPeaceful" )
	public double costToToggleUpPeaceful=0.0d;
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToToggleDownPeaceful"//newest
		,"economy.economycostToToggleDownPeaceful"//newer
		,"economy_costToToggleDownPeaceful"//very old one
		}, realAlias_inNonDottedFormat = "costToToggleDownPeaceful" )
	public double costToToggleDownPeaceful=0.0d;
}
