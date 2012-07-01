package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class SubSection_Disguise {
	
	
	@Option(
			realAlias_inNonDottedFormat = "enableDisguiseIntegration" )
	public _boolean enableDisguiseIntegration=new _boolean(false);
	
	
	@Option(oldAliases_alwaysDotted={
		"unDisguiseIfInOwnTerritory"
	}, realAlias_inNonDottedFormat = "unDisguiseIfInOwnTerritory" )
	public _boolean unDisguiseIfInOwnTerritory=new _boolean(false);
	
	
	@Option(oldAliases_alwaysDotted={
		"unDisguiseIfInEnemyTerritory"
	}, realAlias_inNonDottedFormat = "unDisguiseIfInEnemyTerritory" )
	public _boolean unDisguiseIfInEnemyTerritory=new _boolean(false);
	
	
	
}
