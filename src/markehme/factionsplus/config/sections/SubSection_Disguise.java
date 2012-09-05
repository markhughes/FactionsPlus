package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.Option;


public final class SubSection_Disguise {
	
	
	@Option(
			realAlias_inNonDottedFormat = "enableDisguiseIntegration" )
	public  final _boolean enableDisguiseIntegration=new _boolean(false);
	
	
	@Option(oldAliases_alwaysDotted={
		"unDisguiseIfInOwnTerritory"
	}, realAlias_inNonDottedFormat = "unDisguiseIfInOwnTerritory" )
	public  final _boolean unDisguiseIfInOwnTerritory=new _boolean(false);
	
	
	@Option(oldAliases_alwaysDotted={
		"unDisguiseIfInEnemyTerritory"
	}, realAlias_inNonDottedFormat = "unDisguiseIfInEnemyTerritory" )
	public  final _boolean unDisguiseIfInEnemyTerritory=new _boolean(false);
	
	
	
}
