package markehme.factionsplus.MCore;

import java.util.ArrayList;
import java.util.Map;

import markehme.factionsplus.extras.FType;

import org.bukkit.command.CommandSender;

import com.massivecraft.factions.Rel;
import com.massivecraft.mcore.store.Entity;
import com.massivecraft.mcore.store.SenderEntity;
import com.massivecraft.mcore.util.MUtil;
import com.massivecraft.mcore.util.Txt;

public class UConf extends Entity<UConf> {
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //

	public static UConf get(Object oid) {
		return UConfColls.get().get2(oid);
	}

	// -------------------------------------------- //
	// UNIVERSE ENABLE SWITCH
	// -------------------------------------------- //

	public boolean enabled = true;

	public static boolean isDisabled(Object universe) {
		return isDisabled(universe, null);
	}

	public static String getDisabledMessage(Object universe) {
		UConf uconf = UConf.get(universe);
		return Txt.parse("<i>FactionsPlus is disabled in the <h>%s <i>universe.", uconf.getUniverse());
	}

	public static boolean isDisabled(Object universe, Object inform) {
		UConf uconf = UConf.get(universe);
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
	
	public boolean removeJailDataOnLeave = true;
	
	public boolean denyMovementWhileJailed = true;
	
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
			"safezone", false,
			"ally", true,
			"enemy", false,
			"truce", false);
	
	public Boolean allowWarpFromOtherWorld = true;
	
	public int disallowWarpIfEnemyWithin = 0;
	
	public Map<String, Boolean> ignoreDisallowWarpIfEnemyWithinIfIn = MUtil.map(
			"owned", true,
			"wilderness", false,
			"safezone", true,
			"ally", true,
			"enemy", false,
			"truce", false);
	
	public Boolean smokeEffectOnWarp = true;
	
	public Boolean removeWarpIfInWrongTerritory = true;
	
	// ----------------------------------------
	// Factions
	// ----------------------------------------
	
	public Boolean factionNameForceFirstLetterUppercase = false;
	
	
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
		
	public Map<Rel, Boolean> whoCanTogglePeacefulState = MUtil.map(
			Rel.LEADER, false,
			Rel.OFFICER, false,
			Rel.MEMBER, false,
			Rel.RECRUIT, false);	
	
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
			"whenDathByOther", 0.0
			);	
	
	// ----------------------------------------
	// Announcements 
	// ----------------------------------------
	
	public Boolean announcementsEnabled = false;
	
	public Map<Rel, Boolean> whoCanAnnounce = MUtil.map(
			Rel.LEADER, false,
			Rel.OFFICER, false,
			Rel.MEMBER, false,
			Rel.RECRUIT, false
			);	
	
	public Map<String, Boolean> showAnnouncement = MUtil.map(
			"onlogin", true,
			"onterritoryenter", true
			);	
	
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
			"jailPlayer", 0.0,
			"setJail", 0.0,
			"unJail", 0.0,
			"toggleUpPeaceful", 0.0,
			"toggleDownPeaceful", 0.0
			);	
	
	boolean chargeFactionInsteadOfPlayer = false;
	
	// ----------------------------------------
	// Scoreboards 
	// ----------------------------------------
	public Boolean scoreboardTopFactions = false;
	public Boolean scoreboardMapOfFactions = false;
	
	public int scoreboardUpdate = 5;
	
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
	// Protection Section 
	// ----------------------------------------
	public Boolean removeSignProtectionOnClaim = true;
	
	public Map<String, Boolean> allowFactionKill = MUtil.map(
			"allyMobs", true,
			"neturalMobs", true,
			"enemyMobs", true
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
		"usePortalsInTruce", true,
		"usePortalsInPermanent", true
	);	
	
	// ----------------------------------------
	// Cannons Section 
	// ----------------------------------------
	public Map<String, Boolean> cannons = MUtil.map(
		"useCannonsInEnemy", true,
		"useCannonsInAlly", true,
		"useCannonsInWilderness", true,
		"useCannonsInTruce", true,
		"useCannonsInPermanent", true
	);	
	
	// ----------------------------------------
	// Flying Section 
	// ----------------------------------------
	public boolean allowAttackingWhileFlying = true;
	public boolean allowSplashPotionsWhileFlying = true;
	
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
			"useCreativeGatesInPermanent", true
		);
	
	// ----------------------------------------
	// Fixes Section 
	// ----------------------------------------
	public Map<String, Boolean> fixes = MUtil.map(
			"disallowChangingRelationshipToWilderness", true,
			"disallowChangingRelationshipToSafezone", true,
			"disallowChangingRelationshipToWarzone", true,
			"crossBorderLiquidFlowBlock", false
		);
}