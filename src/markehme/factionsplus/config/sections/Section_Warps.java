package markehme.factionsplus.config.sections;

import com.massivecraft.factions.*;

import markehme.factionsplus.*;
import markehme.factionsplus.config.*;


public final class Section_Warps{
	
	@Option(oldAliases_alwaysDotted={
		"warps.enableWarps"
		,"enableWarps"
		}, realAlias_inNonDottedFormat = "enabled" )
	public  final _boolean enabled=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.leadersCanSetWarps"
		"leadersCanSetWarps"
		}, realAlias_inNonDottedFormat = "leadersCanSetWarps" )
	public  final _boolean leadersCanSetWarps=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.officersCanSetWarps"
		"officersCanSetWarps"
		}, realAlias_inNonDottedFormat = "officersCanSetWarps" )
	public  final _boolean officersCanSetWarps=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.membersCanSetWarps"
		"membersCanSetWarps"
		}, realAlias_inNonDottedFormat = "membersCanSetWarps" )
	public  final _boolean membersCanSetWarps=new _boolean(false);
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.mustBeInOwnTerritoryToCreate"
		"mustBeInOwnTerritoryToCreate"
	}, realAlias_inNonDottedFormat = "mustBeInOwnTerritoryToCreate" )
	public  final _boolean mustBeInOwnTerritoryToCreate=new _boolean(true);
	
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.maxWarps"
		"maxWarps"
	}, realAlias_inNonDottedFormat = "maxWarps" )
	public  final _int maxWarps=new _int(5);
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.warpTeleportAllowedFromEnemyTerritory"
		"warpTeleportAllowedFromEnemyTerritory"
	}, realAlias_inNonDottedFormat = "warpTeleportAllowedFromEnemyTerritory" )
	public  final _boolean warpTeleportAllowedFromEnemyTerritory=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.warpTeleportAllowedFromDifferentWorld"
		"warpTeleportAllowedFromDifferentWorld"
	}, realAlias_inNonDottedFormat = "warpTeleportAllowedFromDifferentWorld" )
	public  final _boolean warpTeleportAllowedFromDifferentWorld=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.warpTeleportAllowedEnemyDistance"
		"warpTeleportAllowedEnemyDistance"
	}, realAlias_inNonDottedFormat = "warpTeleportAllowedEnemyDistance" )
	public  final _int warpTeleportAllowedEnemyDistance=new _int(35);
	
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.warpTeleportIgnoreEnemiesIfInOwnTerritory"
		"warpTeleportIgnoreEnemiesIfInOwnTerritory"
	}, realAlias_inNonDottedFormat = "warpTeleportIgnoreEnemiesIfInOwnTerritory" )
	public  final _boolean warpTeleportIgnoreEnemiesIfInOwnTerritory=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.smokeEffectOnWarp"
		"smokeEffectOnWarp"
	}, realAlias_inNonDottedFormat = "smokeEffectOnWarp" )
	public  final _boolean smokeEffectOnWarp=new _boolean(true);
	
	
	
	public static final boolean canSetOrRemoveWarps(FPlayer fplayer) {
		if(Config._warps.membersCanSetWarps._) {
			return true;
		} else {
			if(Config._warps.officersCanSetWarps._ && Utilities.isOfficer(fplayer) ) {
				return true;
			} else if(Config._warps.leadersCanSetWarps._ && Utilities.isLeader(fplayer) ) {
				return true;
			}
		}
		return false;
	}
	
	
}
