package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class SubSection_Disguise {
	
	
	@Option(
			realAlias_inNonDottedFormat = "enableDisguiseIntegration" )
	public boolean enableDisguiseIntegration=false;
	
	
	@Option(oldAliases_alwaysDotted={
		"unDisguiseIfInOwnTerritory"
	}, realAlias_inNonDottedFormat = "unDisguiseIfInOwnTerritory" )
	public boolean unDisguiseIfInOwnTerritory=false;
	
	
	@Option(oldAliases_alwaysDotted={
		"unDisguiseIfInEnemyTerritory"
	}, realAlias_inNonDottedFormat = "unDisguiseIfInEnemyTerritory" )
	public boolean unDisguiseIfInEnemyTerritory=false;
	
	
	
}
