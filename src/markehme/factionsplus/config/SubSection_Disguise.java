package markehme.factionsplus.config;


public class SubSection_Disguise {
	
	
	@ConfigOption
	public boolean enableDisguiseIntegration=false;
	
	
	@ConfigOption(oldAliases={
		"unDisguiseIfInOwnTerritory"
	})
	public boolean unDisguiseIfInOwnTerritory=false;
	
	
	@ConfigOption(oldAliases={
		"unDisguiseIfInOwnTerritory"
	})
	public boolean unDisguiseIfInEnemyTerritory=false;
	
	
	
}
