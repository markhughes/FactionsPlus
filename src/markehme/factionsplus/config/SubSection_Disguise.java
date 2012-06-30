package markehme.factionsplus.config;


public class SubSection_Disguise {
	
	
	@ConfigOption
	public boolean enableDisguiseIntegration=false;
	
	
	@ConfigOption(oldAliases={
		"unDisguiseIfInOwnTerritory"
	})
	public boolean unDisguiseIfInOwnTerritory=false;
	
	
	@ConfigOption(oldAliases={
		"unDisguiseIfInEnemyTerritory"
	})
	public boolean unDisguiseIfInEnemyTerritory=false;
	
	
	
}
