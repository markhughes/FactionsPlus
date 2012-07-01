package markehme.factionsplus.config.sections;

import com.massivecraft.factions.*;

import markehme.factionsplus.*;
import markehme.factionsplus.config.*;


public class Section_Warps{
	
	@Option(oldAliases_alwaysDotted={
		"warps.enableWarps"
		,"enableWarps"
		}, realAlias_inNonDottedFormat = "enabled" )
	public boolean enabled=true;
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.leadersCanSetWarps"
		"leadersCanSetWarps"
		}, realAlias_inNonDottedFormat = "leadersCanSetWarps" )
	public boolean leadersCanSetWarps=true;
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.officersCanSetWarps"
		"officersCanSetWarps"
		}, realAlias_inNonDottedFormat = "officersCanSetWarps" )
	public boolean officersCanSetWarps=true;
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.membersCanSetWarps"
		"membersCanSetWarps"
		}, realAlias_inNonDottedFormat = "membersCanSetWarps" )
	public boolean membersCanSetWarps=false;
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.mustBeInOwnTerritoryToCreate"
		"mustBeInOwnTerritoryToCreate"
	}, realAlias_inNonDottedFormat = "mustBeInOwnTerritoryToCreate" )
	public boolean mustBeInOwnTerritoryToCreate=true;
	
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.maxWarps"
		"maxWarps"
	}, realAlias_inNonDottedFormat = "maxWarps" )
	public int maxWarps=5;
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.warpTeleportAllowedFromEnemyTerritory"
		"warpTeleportAllowedFromEnemyTerritory"
	}, realAlias_inNonDottedFormat = "warpTeleportAllowedFromEnemyTerritory" )
	public boolean warpTeleportAllowedFromEnemyTerritory=true;
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.warpTeleportAllowedFromDifferentWorld"
		"warpTeleportAllowedFromDifferentWorld"
	}, realAlias_inNonDottedFormat = "warpTeleportAllowedFromDifferentWorld" )
	public boolean warpTeleportAllowedFromDifferentWorld=true;
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.warpTeleportAllowedEnemyDistance"
		"warpTeleportAllowedEnemyDistance"
	}, realAlias_inNonDottedFormat = "warpTeleportAllowedEnemyDistance" )
	public int warpTeleportAllowedEnemyDistance=35;
	
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.warpTeleportIgnoreEnemiesIfInOwnTerritory"
		"warpTeleportIgnoreEnemiesIfInOwnTerritory"
	}, realAlias_inNonDottedFormat = "warpTeleportIgnoreEnemiesIfInOwnTerritory" )
	public boolean warpTeleportIgnoreEnemiesIfInOwnTerritory=true;
	
	
	@Option(oldAliases_alwaysDotted={
//		"warps.smokeEffectOnWarp"
		"smokeEffectOnWarp"
	}, realAlias_inNonDottedFormat = "smokeEffectOnWarp" )
	public boolean smokeEffectOnWarp=true;
	
	
	
	public boolean canSetOrRemoveWarps(FPlayer fplayer) {
		if(Config._warps.membersCanSetWarps) {
			return true;
		} else {
			if(Utilities.isOfficer(fplayer) && Config._warps.officersCanSetWarps) {
				return true;
			} else if(Utilities.isLeader(fplayer) && Config._warps.leadersCanSetWarps) {
				return true;
			}
		}
		return false;
	}
	
	
}
