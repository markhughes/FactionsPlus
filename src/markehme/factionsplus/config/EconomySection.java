package markehme.factionsplus.config;


public class EconomySection implements Section {
	private static final String prefixEconomy="economy"+Config.delim;
	
	
	private static final String ectw="economyCostToWarp";
	@ConfigOption(oldAliases={
		prefixEconomy+"economycostToWarp"
		,prefixEconomy+ectw
		,ectw})
	public static double costToWarp=0;
	
	
	
}
