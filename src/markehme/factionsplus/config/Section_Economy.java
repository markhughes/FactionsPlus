package markehme.factionsplus.config;


public class Section_Economy {
	
	
	@ConfigOption(oldAliases={
		"economy.enableEconomy"
		,"enableEconomy"
		,"economy_enable"
	})
	public boolean enabled=false;
	
	
	
	@ConfigOption(oldAliases={
		"economy.economyCostToWarp"//newest
		,"economy.economycostToWarp"//newer
		,"economy_costToWarp"//very old one
		})
	public double costToWarp=0.0d;//current one (the field name)
	
	
	@ConfigOption(oldAliases={
		"economy.economyCostToCreateWarp"//newest
		,"economy.economycostToCreateWarp"//newer
		,"economy_costToCreateWarp"//very old one
		})
	public double costToCreateWarp=0.0d;
	
	
	@ConfigOption(oldAliases={
		"economy.economyCostToDeleteWarp"//newest
		,"economy.economycostToDeleteWarp"//newer
		,"economy_costToDeleteWarp"//very old one
		})
	public double costToDeleteWarp=0.0d;
	
	
	@ConfigOption(oldAliases={
		"economy.economyCostToAnnounce"//newest
		,"economy.economycostToAnnounce"//newer
		,"economy_costToAnnounce"//very old one
		})
	public double costToAnnounce=0.0d;
	
	
	@ConfigOption(oldAliases={
		"economy.economyCostToJail"//newest
		,"economy.economycostToJail"//newer
		,"economy_costToJail"//very old one
		})
	public double costToJail=0.0d;
	
	
	@ConfigOption(oldAliases={
		"economy.economyCostToSetJail"//newest
		,"economy.economycostToSetJail"//newer
		,"economy_costToSetJail"//very old one
		})
	public double costToSetJail=0.0d;
	
	
	@ConfigOption(oldAliases={
		"economy.economyCostToUnJail"//newest
		,"economy.economycostToUnJail"//newer
		,"economy_costToUnJail"//very old one
		})
	public double costToUnJail=0.0d;
	
	
	@ConfigOption(oldAliases={
		"economy.economyCostToToggleUpPeaceful"//newest
		,"economy.economycostToToggleUpPeaceful"//newer
		,"economy_costToToggleUpPeaceful"//very old one
		})
	public double costToToggleUpPeaceful=0.0d;
	
	
	@ConfigOption(oldAliases={
		"economy.economyCostToToggleDownPeaceful"//newest
		,"economy.economycostToToggleDownPeaceful"//newer
		,"economy_costToToggleDownPeaceful"//very old one
		})
	public double costToToggleDownPeaceful=0.0d;
}
