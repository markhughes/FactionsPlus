package markehme.factionsplus.config;

import com.massivecraft.factions.*;

import markehme.factionsplus.*;


public class WarpsSection{
	
	@ConfigOption(oldAliases={
		"warps.enableWarps"
		,"enableWarps"
		})
	public boolean enabled=true;
	
	
	@ConfigOption(oldAliases={
//		"warps.leadersCanSetWarps"
		"leadersCanSetWarps"
		})
	public boolean leadersCanSetWarps=true;
	
	
	@ConfigOption(oldAliases={
//		"warps.officersCanSetWarps"
		"officersCanSetWarps"
		})
	public boolean officersCanSetWarps=true;
	
	
	@ConfigOption(oldAliases={
//		"warps.membersCanSetWarps"
		"membersCanSetWarps"
		})
	public boolean membersCanSetWarps=false;
	
	
	@ConfigOption(oldAliases={
//		"warps.mustBeInOwnTerritoryToCreate"
		"mustBeInOwnTerritoryToCreate"
	})
	public boolean mustBeInOwnTerritoryToCreate=true;
	
	
	
	@ConfigOption(oldAliases={
//		"warps.maxWarps"
		"maxWarps"
	})
	public int maxWarps=5;
	
	
	@ConfigOption(oldAliases={
//		"warps.warpTeleportAllowedFromEnemyTerritory"
		"warpTeleportAllowedFromEnemyTerritory"
	})
	public boolean warpTeleportAllowedFromEnemyTerritory=true;
	
	
	@ConfigOption(oldAliases={
//		"warps.warpTeleportAllowedFromDifferentWorld"
		"warpTeleportAllowedFromDifferentWorld"
	})
	public boolean warpTeleportAllowedFromDifferentWorld=true;
	
	
	@ConfigOption(oldAliases={
//		"warps.warpTeleportAllowedEnemyDistance"
		"warpTeleportAllowedEnemyDistance"
	})
	public int warpTeleportAllowedEnemyDistance=35;
	
	
	
	@ConfigOption(oldAliases={
//		"warps.warpTeleportIgnoreEnemiesIfInOwnTerritory"
		"warpTeleportIgnoreEnemiesIfInOwnTerritory"
	})
	public boolean warpTeleportIgnoreEnemiesIfInOwnTerritory=true;
	
	
	@ConfigOption(oldAliases={
//		"warps.smokeEffectOnWarp"
		"smokeEffectOnWarp"
	})
	public boolean smokeEffectOnWarp=true;
	
	
	
	public boolean canSetOrRemoveWarps(FPlayer fplayer) {
		if(Config.warps.membersCanSetWarps) {
			return true;
		} else {
			if(Utilities.isOfficer(fplayer) && Config.warps.officersCanSetWarps) {
				return true;
			} else if(Utilities.isLeader(fplayer) && Config.warps.leadersCanSetWarps) {
				return true;
			}
		}
		return false;
	}
	
	
}
