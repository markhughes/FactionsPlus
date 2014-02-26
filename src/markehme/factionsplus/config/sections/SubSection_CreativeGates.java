package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.Option;

public class SubSection_CreativeGates {
	
	@Option(autoComment={"Enabling this will destroy CreativeGates when land is claimed/unclaimed!"},
			realAlias_inNonDottedFormat = "destroyCreativeGatesOnClaimUnclaim" )
		public final _boolean		destroyCreativeGatesOnClaimUnclaim	= new _boolean( false );
	
	@Option(autoComment={"Enabling this will allow CreativeGates in owned Territories"},
			realAlias_inNonDottedFormat = "allowCreativeGatesInOwnTerritories" )
		public final _boolean		allowCreativeGatesInOwnTerritories	= new _boolean( true );
	
	@Option(autoComment={"Enabling this will allow CreativeGates in the Wilderness"},
			realAlias_inNonDottedFormat = "allowCreativeGatesInWilderness" )
		public final _boolean		allowCreativeGatesInWilderness	= new _boolean( true );
	
	@Option(autoComment={"Enabling this will allow using CreativeGates in enemy territory"},
			realAlias_inNonDottedFormat = "allowUsingCreativeGatesInEnemyTerritory" )
		public final _boolean		allowUsingCreativeGatesInEnemyTerritory	= new _boolean( false );

}
