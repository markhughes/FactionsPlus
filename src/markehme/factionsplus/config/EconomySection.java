package markehme.factionsplus.config;


public class EconomySection {
//	private static final String prefixEconomy="economy"+Config.delim;
//	private static final String ectw="economyCostToWarp";
	
	//I was thinking to keep them as non-composed strings for easier to see, but more error prone, however it may just be worth it
	
	@ConfigOption(oldAliases={
		"economy.economyCostToWarp"//newest
		,"economy.economycostToWarp"//newer
		,"economyCostToWarp"//very old one
//		,"jails.enabled" for tests, deleteme, me who? me the comment
//		prefixEconomy+ectw//newest
//		,prefixEconomy+"economycostToWarp"//newer
//		,ectw//very old one
		})
	public static double costToWarp=0;//current one (the field name)
	
	
	
}
