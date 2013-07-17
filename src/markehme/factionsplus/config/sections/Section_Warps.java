package markehme.factionsplus.config.sections;

import com.massivecraft.factions.entity.UPlayer;

import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;
import markehme.factionsplus.config.Option;

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
	
	
	@Option(
		autoComment={
			"changing this from true to false does NOT remove all warps that are outside territories",//don't do;
			"when false, allows you to create and teleport to warps in any places even if they are in enemy land"
		},
		oldAliases_alwaysDotted={
//		"warps.mustBeInOwnTerritoryToCreate"
		"mustBeInOwnTerritoryToCreate"
	}, realAlias_inNonDottedFormat = "mustBeInOwnTerritoryToCreate" )
	public  final _boolean mustBeInOwnTerritoryToCreate=new _boolean(true);
	
	@Option(
		autoComment={
			"only has effect when `mustBeInOwnTerritoryToCreate` is false,",
			"if true, you cannot teleport to a warp residing inside enemy land"
		},
		oldAliases_alwaysDotted={
	}, realAlias_inNonDottedFormat = "denyWarpToEnemyLand" )
	public  final _boolean denyWarpToEnemyLand=new _boolean(true);
	
	@Option(
		autoComment={
			"only has effect when `mustBeInOwnTerritoryToCreate` is false,",
			"if true, you cannot teleport to a warp residing inside ally land"
		},
		oldAliases_alwaysDotted={
	}, realAlias_inNonDottedFormat = "denyWarpToAllyLand" )
	public  final _boolean denyWarpToAllyLand=new _boolean(false);
	
	@Option(
		autoComment={
			"only has effect when `mustBeInOwnTerritoryToCreate` is false,",
			"if true, you cannot teleport to a warp residing inside neutral(and truce in 1.7) land"
		},
		oldAliases_alwaysDotted={
	}, realAlias_inNonDottedFormat = "denyWarpToNeutralOrTruceLand" )
	public  final _boolean denyWarpToNeutralOrTruceLand=new _boolean(false);
	
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
	
	@Option(oldAliases_alwaysDotted={
//			"warps.removeWarpIfDeniedAccess"
			"removeWarpIfDeniedAccess"
		}, realAlias_inNonDottedFormat = "removeWarpIfDeniedAccess" )
		public  final _boolean removeWarpIfDeniedAccess=new _boolean(true);
	
	
	public static final boolean canSetOrRemoveWarps(UPlayer uPlayer) {
		if(Config._warps.membersCanSetWarps._) {
			return true;
		} else {
			if(Config._warps.officersCanSetWarps._ && Utilities.isOfficer(uPlayer) ) {
				return true;
			} else if(Config._warps.leadersCanSetWarps._ && Utilities.isLeader(uPlayer) ) {
				return true;
			}
		}
		return false;
	}
	
	
}
