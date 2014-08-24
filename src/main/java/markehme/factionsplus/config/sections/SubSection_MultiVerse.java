package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.Option;

public final class SubSection_MultiVerse {
	@Option(	autoComment={"disallow enemies from using each others portals"},
			realAlias_inNonDottedFormat = "enemyCantUseEnemyPortals"
		)
	public  final _boolean enemyCantUseEnemyPortals=new _boolean(true);
	
	@Option(	autoComment={"will disallow people in other Factions from using others portals"},
			realAlias_inNonDottedFormat = "mustBeInOwnTerritoryToUsePortals"
		)
	public  final _boolean mustBeInOwnTerritoryToUsePortals=new _boolean(false);
	
	@Option(	autoComment={"this allows allies to use each others portals"},
			realAlias_inNonDottedFormat = "cannotBeInEnemyTerritoryToUsePortals"
		)
	public  final _boolean alliesCanUseEachOthersPortals=new _boolean(false);

}
