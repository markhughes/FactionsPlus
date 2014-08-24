package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.Option;


public final class SubSection_PVP {
	

	@Option(autoComment={
		"will not allow anyone to /f claim  if enemy players are in that chunk"
	},
		oldAliases_alwaysDotted={
	}, realAlias_inNonDottedFormat = "denyClaimWhenEnemyNearBy" )
	public  final _boolean denyClaimWhenEnemyNearBy=new _boolean(true);
	
	@Option(autoComment={
		"will not allow anyone to /f claim  if ally players are in that chunk"
	},
		oldAliases_alwaysDotted={
	}, realAlias_inNonDottedFormat = "denyClaimWhenAllyNearBy" )
	public  final _boolean denyClaimWhenAllyNearBy=new _boolean(false);
	
	@Option(autoComment={
		"will not allow anyone to /f claim  if neutral/TRUCE players are in that chunk"
	},
		oldAliases_alwaysDotted={
	}, realAlias_inNonDottedFormat = "denyClaimWhenNeutralNearBy" )
	public  final _boolean denyClaimWhenNeutralNearBy=new _boolean(false);
	
	
	public final boolean shouldInstallDenyClaimListener() {
		return denyClaimWhenAllyNearBy._ || denyClaimWhenEnemyNearBy._ || denyClaimWhenNeutralNearBy._;
	}
}
