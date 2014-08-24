package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.Option;

public class SubSection_Cannons {
	
	@Option(
			realAlias_inNonDottedFormat = "allowCannonUseInWilderness" )
	public  final _boolean allowCannonUseInWilderness = new _boolean(true);
	
	@Option(
			realAlias_inNonDottedFormat = "allowCannonUseInOwnTerritory" )
	public  final _boolean allowCannonUseInOwnTerritory = new _boolean(true);
	
	@Option(
			realAlias_inNonDottedFormat = "allowCannonUseInEnemyTerritory" )
	public  final _boolean allowCannonUseInEnemyTerritory = new _boolean(false);
	
}
