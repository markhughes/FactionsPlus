package markehme.factionsplus.config.sections;

import com.massivecraft.factions.*;

import markehme.factionsplus.*;
import markehme.factionsplus.config.*;


public final class Section_Warps{
	
	@Option(oldAliases_alwaysDotted={
		"warps.enableWarps"
		,"enableWarps"
		}, realAlias_inNonDottedFormat = "enabled" )
	public static final _boolean enabled=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.leadersCanSetWarps"
		"leadersCanSetWarps"
		}, realAlias_inNonDottedFormat = "leadersCanSetWarps" )
	public static final _boolean leadersCanSetWarps=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.officersCanSetWarps"
		"officersCanSetWarps"
		}, realAlias_inNonDottedFormat = "officersCanSetWarps" )
	public static final _boolean officersCanSetWarps=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.membersCanSetWarps"
		"membersCanSetWarps"
		}, realAlias_inNonDottedFormat = "membersCanSetWarps" )
	public static final _boolean membersCanSetWarps=new _boolean(false);
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.mustBeInOwnTerritoryToCreate"
		"mustBeInOwnTerritoryToCreate"
	}, realAlias_inNonDottedFormat = "mustBeInOwnTerritoryToCreate" )
	public static final _boolean mustBeInOwnTerritoryToCreate=new _boolean(true);
	
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.maxWarps"
		"maxWarps"
	}, realAlias_inNonDottedFormat = "maxWarps" )
	public static final _int maxWarps=new _int(5);
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.warpTeleportAllowedFromEnemyTerritory"
		"warpTeleportAllowedFromEnemyTerritory"
	}, realAlias_inNonDottedFormat = "warpTeleportAllowedFromEnemyTerritory" )
	public static final _boolean warpTeleportAllowedFromEnemyTerritory=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.warpTeleportAllowedFromDifferentWorld"
		"warpTeleportAllowedFromDifferentWorld"
	}, realAlias_inNonDottedFormat = "warpTeleportAllowedFromDifferentWorld" )
	public static final _boolean warpTeleportAllowedFromDifferentWorld=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.warpTeleportAllowedEnemyDistance"
		"warpTeleportAllowedEnemyDistance"
	}, realAlias_inNonDottedFormat = "warpTeleportAllowedEnemyDistance" )
	public static final _int warpTeleportAllowedEnemyDistance=new _int(35);
	
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.warpTeleportIgnoreEnemiesIfInOwnTerritory"
		"warpTeleportIgnoreEnemiesIfInOwnTerritory"
	}, realAlias_inNonDottedFormat = "warpTeleportIgnoreEnemiesIfInOwnTerritory" )
	public static final _boolean warpTeleportIgnoreEnemiesIfInOwnTerritory=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.smokeEffectOnWarp"
		"smokeEffectOnWarp"
	}, realAlias_inNonDottedFormat = "smokeEffectOnWarp" )
	public static final _boolean smokeEffectOnWarp=new _boolean(true);
	
	
	
	public static final boolean canSetOrRemoveWarps(FPlayer fplayer) {
		if(Config._warps.membersCanSetWarps._) {
			return true;
		} else {
			if(Utilities.isOfficer(fplayer) && Config._warps.officersCanSetWarps._) {
				return true;
			} else if(Utilities.isLeader(fplayer) && Config._warps.leadersCanSetWarps._) {
				return true;
			}
		}
		return false;
	}
	
	
}
