package markehme.factionsplus.MCore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import markehme.factionsplus.extras.FType;
import markehme.factionsplus.scoreboard.CurrentScoreboard;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

import com.massivecraft.factions.Rel;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.store.SenderEntity;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.Txt;

public class FPUConf extends Entity<FPUConf> {
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //

	public static FPUConf get(Object oid) {
		return FPUConfColls.get().get2(oid);
	}

	// -------------------------------------------- //
	// UNIVERSE ENABLE SWITCH
	// -------------------------------------------- //

	public boolean enabled = true;

	public static boolean isDisabled(Object universe) {
		return isDisabled(universe, null);
	}

	public static String getDisabledMessage(Object universe) {
		FPUConf uconf = FPUConf.get(universe);
		return Txt.parse("<i>FactionsPlus is disabled in the <h>%s <i>universe.", uconf.getUniverse());
	}

	public static boolean isDisabled(Object universe, Object inform) {
		FPUConf uconf = FPUConf.get(universe);
		if (uconf.enabled) return false;

		if (inform instanceof CommandSender) {
			((CommandSender)inform).sendMessage(getDisabledMessage(universe));
		}
		else if (inform instanceof SenderEntity)
		{
			((SenderEntity<?>)inform).sendMessage(getDisabledMessage(universe));
		}

		return true;
	}
	
	
	// ----------------------------------------
	// Jails
	// ----------------------------------------
	
	public boolean jailsEnabled = false;

	public Map<Rel, Boolean> whoCanSetJails = MUtil.map(
			Rel.LEADER, true,
			Rel.OFFICER, true,
			Rel.MEMBER, false,
			Rel.RECRUIT, false);
	
	public Map<Rel, Boolean> whoCanJail = MUtil.map(
			Rel.LEADER, true,
			Rel.OFFICER, false,
			Rel.MEMBER, false,
			Rel.RECRUIT, false);
	
	public boolean removeJailDataOnLeave = false;
	
	public boolean denyMovementWhileJailed = true;
	
	public boolean denyChatWhileJailed = true;
	
	public int delayBeforeSentToJail = 0;
	
	public boolean mustBeInOwnTerritoryToJail = true;
	
	public boolean tellPlayerWhoJailedThem = true;
	
	
	// ----------------------------------------
	// Warps
	// ----------------------------------------
	
	public boolean warpsEnabled = false;
	
	public Map<Rel, Boolean> whoCanSetWarps = MUtil.map(
			Rel.LEADER, true,
			Rel.OFFICER, true,
			Rel.MEMBER, false,
			Rel.RECRUIT, false);
	
	public Map<Rel, Boolean> whoCanUseWarps = MUtil.map(
			Rel.LEADER, true,
			Rel.OFFICER, true,
			Rel.MEMBER, true,
			Rel.RECRUIT, true);
	
	public Map<String, Boolean> allowWarpsIn = MUtil.map(
			"owned", true,
			"wilderness", false,
			"safezone", false,
			"warzone", false,
			"ally", false,
			"enemy", false,
			"neutral", false,
			"truce", false);
	
	
	public boolean enforceAllowOnTeleport = true;
	
	public int maxWarps = 5;
	
	public Map<String, Boolean> allowWarpFrom = MUtil.map(
			"owned", true,
			"wilderness", true,
			"safezone", true,
			"warzone", false,
			"enemy", false,
			"ally", true,
			"neurtal", true,
			"truce", true);
	
	public Boolean allowWarpFromOtherWorld = true;
	
	public int disallowWarpIfEnemyWithin = 0;
	
	public Map<String, Boolean> ignoreDisallowWarpIfEnemyWithinIfIn = MUtil.map(
			"owned", true,
			"wilderness", false,
			"safezone", true,
			"warzone", false,
			"ally", true,
			"enemy", false,
			"neutral", false,
			"truce", false);
	
	public Boolean smokeEffectOnWarp = true;
	
	public Boolean removeWarpIfInWrongTerritory = true;
	
	// ----------------------------------------
	// Factions
	// ----------------------------------------
	
	public Boolean factionNameForceFirstLetterUppercase = false;
	public Boolean factionJoinLeaveMessagesLockedToFaction = false;
	
	public Boolean factionChestAllowAnywhere = false;
	
	public Map<Rel, Boolean> whoCanSetChest = MUtil.map(
			Rel.LEADER, true,
			Rel.OFFICER, true,
			Rel.MEMBER, false,
			Rel.RECRUIT, false);
	
	public Map<Rel, Boolean> whoCanSetFlags = MUtil.map(
			Rel.LEADER, true,
			Rel.OFFICER, false,
			Rel.MEMBER, false,
			Rel.RECRUIT, false);
	
	// ----------------------------------------
	// Banning
	// ----------------------------------------
	
	public boolean bansEnabled = false;
	
	public Map<Rel, Boolean> whoCanBan = MUtil.map(
			Rel.LEADER, true,
			Rel.OFFICER, true,
			Rel.MEMBER, false,
			Rel.RECRUIT, false);
	
	
	// ----------------------------------------
	// Rules
	// ----------------------------------------
	
	public boolean rulesEnabled = false;
	
	public Map<Rel, Boolean> whoCanSetRules = MUtil.map(
			Rel.LEADER, true,
			Rel.OFFICER, true,
			Rel.MEMBER, false,
			Rel.RECRUIT, false);
	
	public int maxRulesPerFaction = 12;
	
	public Boolean showRulesOnJoin = false;
	
	// ----------------------------------------
	// Peaceful
	// ----------------------------------------
	
	public Boolean enablePeacefulBoost = false;
	public double peacefulPowerBoost = 1.0;
	
	public Map<Rel, Boolean> whoCanTogglePeacefulState = MUtil.map(
			Rel.LEADER, false,
			Rel.OFFICER, false,
			Rel.MEMBER, false,
			Rel.RECRUIT, false);	
	
	public long peacefulToggleDelayInSeconds = 0;
	
	public boolean peacefulCantDisband = false;
	
	public boolean peacefulChestProtect = true;
	
	// ----------------------------------------
	// Powerboosts
	// ----------------------------------------
	
	public Map<String, Double> extraPowerBoosts = MUtil.map(
			"whenKillEnemyPlayer", 0.0,
			"whenKillAllyPlayer", 0.0,
			"whenKillTrucePlayer", 0.0,
			"whenKillNeutralPlayer", 0.0,
			"whenKillAnotherMonster", 0.0
			);	
	
	public Map<String, Double> extraPowerLoss = MUtil.map(
			"whenDeathBySuicide", 0.0,
			"whenDeathByPVP", 0.0,
			"whenDeathByMob", 0.0,
			"whenDeathByCactus", 0.0,
			"whenDeathByTNT", 0.0,
			"whenDeathByFire", 0.0,
			"whenDeathByPotion", 0.0,
			"whenDeathByOther", 0.0
			);	
	
	// ----------------------------------------
	// Announcements 
	// ----------------------------------------
	
	public Boolean announcementsEnabled = false;
	
	public Map<Rel, Boolean> whoCanAnnounce = MUtil.map(
			Rel.LEADER, true,
			Rel.OFFICER, false,
			Rel.MEMBER, false,
			Rel.RECRUIT, false
			);	
	
	public Map<String, Boolean> showAnnouncement = MUtil.map(
			"onlogin", true,
			"onterritoryenter", true
			);	
	
	public Boolean allowColoursInAnnouncements = true;
	
	// ----------------------------------------
	// Teleport 
	// ----------------------------------------
	
	public Map<String, Boolean> denyTeleportIntoAlly = MUtil.map(
			"viaHome", false,
			"viaBack", false,
			"viaEnderPearls", false,
			"reportToConsole", false
			);	
	
	public Map<String, Boolean> denyTeleportIntoEnemy = MUtil.map(
			"viaHome", false,
			"viaBack", false,
			"viaEnderPearls", false,
			"reportToConsole", false
			);	
	
	public Map<String, Boolean> denyTeleportIntoNeutral = MUtil.map(
			"viaHome", false,
			"viaBack", false,
			"viaEnderPearls", false,
			"reportToConsole", false
			);	
	
	public Map<String, Boolean> denyTeleportIntoSafeZone = MUtil.map(
			"viaHome", false,
			"viaBack", false,
			"viaEnderPearls", false,
			"reportToConsole", false
			);	
	
	public Map<String, Boolean> denyTeleportIntoWarZone = MUtil.map(
			"viaHome", false,
			"viaBack", false,
			"viaEnderPearls", false,
			"reportToConsole", false
			);	
	
	// ----------------------------------------
	// Economy 
	// ----------------------------------------
	public Map<String, Double> economyCost = MUtil.map(
			"warp", 0.0,
			"createwarp", 0.0,
			"deletewarp", 0.0,
			"announce", 0.0,
			"jail", 0.0,
			"setJail", 0.0,
			"unJail", 0.0,
			"toggleUpPeaceful", 0.0,
			"toggleDownPeaceful", 0.0
			);	
	
	boolean chargeFactionInsteadOfPlayer = false;
	
	// ----------------------------------------
	// Scoreboards 
	// ----------------------------------------
	public Boolean scoreboardEnabled = false;
	
	public CurrentScoreboard scoreboardDefault = CurrentScoreboard.Power;
	
	public int scoreboardUpdate = 5;
	
	public Boolean scoreboardRotateEnabled = true;
	
	public Map<CurrentScoreboard, Boolean> scoreboardRotates = MUtil.map(
			CurrentScoreboard.Power, true,
			CurrentScoreboard.Land, true,
			CurrentScoreboard.Members, false,
			CurrentScoreboard.Money, false
			);
	
	// ----------------------------------------
	// Deny Commands In Areas 
	// ----------------------------------------
	public Map<FType, ArrayList<String>> denyCommandsIn = MUtil.map(
			FType.FACTION, new ArrayList<String>(),
			FType.SAFEZONE, new ArrayList<String>(),
			FType.WARZONE, new ArrayList<String>(),
			FType.WILDERNESS, new ArrayList<String>()
			);
	
	// ----------------------------------------
	// Deny Claims In Areas 
	// ----------------------------------------
	public Map<Rel, Boolean> denyClaimsWhenNearby = MUtil.map(
			Rel.ALLY, false,
			Rel.ENEMY, false,
			Rel.NEUTRAL, false,
			Rel.TRUCE, false
			);

	
	// ----------------------------------------
	// Protection Section 
	// ----------------------------------------
	public Boolean removeSignProtectionOn = true;
	public Boolean removeLWCProtectionOn = true;
	
	public Boolean disallowPublicAccessToLWCProtectionsInLands = true;
	
	public Map<String, Boolean> allowFactionKill = MUtil.map(
			"allyMobs", true,
			"neturalMobs", true,
			"enemyMobs", true,
			"truceMobs", true
			);	
	
	public Map<EntityType, Boolean> allowFactionInteract = MUtil.map(
			EntityType.PIG, true,
			EntityType.HORSE, true
		);	
	
	public Boolean protectPassiveMobsSafeZone = false;
	public Boolean makeSafeZoneExtraSafe = false;
	
	public Boolean strictFarming = false;
	
	public Boolean allowShopsInWilderness = true;
	public Boolean allowShopsInTerritory = true;
	
	public Boolean allowSignProtectionInWilderness = true;
	public Boolean allowSignProtectionInTerritory = true;
	
	public Boolean onlyPeacefulCreateLWCProtections = false;
	
	public Boolean enableWorldGuardRegionCheck = true;
	public Boolean allowBuildingInRegionIfMember = true;
	public Boolean checkForWorldGuardRegionFlags = false;
	
	public Boolean disallowAccessToVillagersToOtherFactions = true;
	

	
	// ----------------------------------------
	// Flying Section 
	// ----------------------------------------
	public boolean allowAttackingWhileFlying = true;
	public boolean allowSplashPotionsWhileFlying = true;
	
	// ----------------------------------------
	// Fixes Section 
	// ----------------------------------------
	public Map<String, Boolean> fixes = MUtil.map(
			"disallowChangingRelationshipToWilderness", true,
			"disallowChangingRelationshipToSafezone", true,
			"disallowChangingRelationshipToWarzone", true,
			"crossBorderLiquidFlowBlock", false,
			"crossBorderLiquidFlowBlockMakeCobblestone", true
			
		);
	
	// ----------------------------------------
	// /f faction/f command additions section 
	// ----------------------------------------
	public List<String> factionCommandHideDescriptionInfo = MUtil.list(
			"enterFactionID"
		);
	
	public List<String> factionCommandHideAgeInfo = MUtil.list(
			"enterFactionID"
		);
	
	public List<String> factionCommandHideOpenInfo = MUtil.list(
			"enterFactionID"
		);
	
	public List<String> factionCommandHidePowerInfo = MUtil.list(
			"enterFactionID"
		);
	
	public List<String> factionCommandHideLandInfo = MUtil.list(
			"enterFactionID"
		);
	
	public List<String> factionCommandHideBankInfo = MUtil.list(
			"enterFactionID"
		);
	
	public List<String> factionCommandHidePermanentInfo = MUtil.list(
			"enterFactionID"
		);
	
	public List<String> factionCommandHidePeacefulInfo = MUtil.list(
			"enterFactionID"
		);
	
	public List<String> factionCommandHideIsTruceWithInfo = MUtil.list(
			"enterFactionID"
		);
	
	public List<String> factionCommandHideIsAlyWithInfo = MUtil.list(
			"enterFactionID"
		);
	
	public List<String> factionCommandHideIsEnemyWithInfo = MUtil.list(
			"enterFactionID"
		);
	
	public List<String> factionCommandHideFollowersOnlineInfo = MUtil.list(
			"enterFactionID"
		);
	public List<String> factionCommandHideFollowersOfflineInfo = MUtil.list(
			"enterFactionID"
		);
	
	// ----------------------------------------
	// misc and unsorted 
	// ----------------------------------------
	public Map<Rel, Boolean> whoCanToggleListeningNeeds = MUtil.map(
			Rel.LEADER, true
		);

	// ----------------------------------------
	// CreativeGates Section 
	// ----------------------------------------
	public Map<String, Boolean> creativegates = MUtil.map(
			"destroyOnClaimUnclaim", true,
			"allowCreateInOwnTerritories", true,
			"allowCreateInWilderness", true,
			"useCreativeGatesInEnemy", true,
			"useCreativeGatesInAlly", true,
			"useCreativeGatesInWilderness", true,
			"useCreativeGatesInTruce", true,
			"useCreativeGatesInNeutral", true,
			"useCreativeGatesInPermanent", true
		);
	
	// ----------------------------------------
	// Disguises Section 
	// ----------------------------------------
	public Boolean disguiseRemoveIfInOwnTerritory = false;
	public Boolean disguiseRemoveIfInWilderness = false;
	public Boolean disguiseRemoveIfInEnemyTerritory = false;
	
	// ----------------------------------------
	// MultiVerse Section 
	// ----------------------------------------
	public Map<String, Boolean> multiverse = MUtil.map(
		"usePortalsInEnemy", true,
		"usePortalsInAlly", true,
		"usePortalsInWilderness", true,
		"usePortalsInWarzone", true,
		"usePortalsInSafezone", true,
		"usePortalsInTruce", true,
		"usePortalsInNeutral", true
		
	);	
	
	// ----------------------------------------
	// Cannons Section 
	// ----------------------------------------
	public Map<String, Boolean> cannons = MUtil.map(
		"useCannonsInEnemy", true,
		"useCannonsInAlly", true,
		"useCannonsInWilderness", true,
		"useCannonsInTruce", true,
		"useCannonsInPermanent", true,
		"useCannonsInOwn", true
	);		
	
	// ----------------------------------------
	// Magic Section 
	// ----------------------------------------
	public boolean magicAllowInWilderness = true;
	public boolean magicAllowInWarzone = true;
	public boolean magicAllowInSafezone = true;
	public boolean magicAllowInOwnFaction = true;
	public boolean magicAllowInEnemyFaction = true;
	public boolean magicAllowInAllyFaction = true;
	public boolean magicMustBeInAFactionToUse = false;
	
	public List<String> magicDisallowInFactions = MUtil.list(
			"enterFactionID"
		);

	public double magicRequiresMinimumPowerOf = 0;
	
}