package markehme.factionsplus;

import java.io.*;

import markehme.factionsplus.extras.*;

import org.bukkit.configuration.file.*;
import org.bukkit.plugin.*;


public abstract class Config {

	
	static final String	fileConfigDefaults	= "config_defaults.yml";//this file is located inside .jar in root dir
	//and it contains the defaults, so that they are no longer hardcoded in java code
	public static File fileConfig = new File(FactionsPlus.folderBase , "config.yml");
	
	public static FileConfiguration config;//not named Conf so to avoid conflicts with com.massivecraft.factions.Conf
	
	
	public static final String prefJails="jails"+Config.delim;
	public static final String confStr_enableJails = prefJails+"enableJails";
	public static final String confStr_leadersCanSetJails = prefJails+"leadersCanSetJails";
	public static final String confStr_officersCanSetJails = prefJails+"officersCanSetJails";
	public static final String confStr_membersCanSetJails = prefJails+"membersCanSetJails";
	public static final String confStr_leadersCanJail = prefJails+"leadersCanJail";
	public static final String confStr_officersCanJail = prefJails+"officersCanJail";
	//Begin Config String Pointers
	public static final String delim=".";
	public static final String prefWarps="warps"+delim;

	public static final String confStr_enableWarps = prefWarps+"enableWarps";
	public static final String confStr_leadersCanSetWarps = prefWarps+"leadersCanSetWarps";
	public static final String confStr_officersCanSetWarps = prefWarps+"officersCanSetWarps";
	public static final String confStr_membersCanSetWarps = prefWarps+"membersCanSetWarps";
	public static final String confStr_mustBeInOwnTerritoryToCreate  = prefWarps+"mustBeInOwnTerritoryToCreate";
	public static final String confStr_maxWarps = prefWarps+"maxWarps";
	public static final String confStr_warpTeleportAllowedFromEnemyTerritory = prefWarps+"warpTeleportAllowedFromEnemyTerritory";
	public static final String confStr_warpTeleportAllowedFromDifferentWorld = prefWarps+"warpTeleportAllowedFromDifferentWorld";
	public static final String confStr_warpTeleportAllowedEnemyDistance = prefWarps+"warpTeleportAllowedEnemyDistance";
	public static final String confStr_warpTeleportIgnoreEnemiesIfInOwnTerritory = prefWarps+"warpTeleportIgnoreEnemiesIfInOwnTerritory";
	public static final String confStr_smokeEffectOnWarp = prefWarps+"smokeEffectOnWarp";
	public static final String prefBanning="banning"+delim;
	public static final String confStr_enableBans=prefBanning+"enableBans";
	public static final String confStr_leadersCanFactionBan=prefBanning+"leadersCanFactionBan";
	public static final String confStr_officersCanFactionBan=prefBanning+"officersCanFactionBan";
	public static final String confStr_leadersCanFactionUnban=prefBanning+"leadersCanFactionUnban";
	public static final String confStr_officersCanFactionUnban=prefBanning+"officersCanFactionUnban";
	public static final String confStr_leaderCanNotBeBanned=prefBanning+"leaderCanNotBeBanned";
	public static final String prefRules="rules"+delim;
	public static final String confStr_enableRules=prefRules+"enableRules";
	public static final String confStr_leadersCanSetRules=prefRules+"leadersCanSetRules";
	public static final String confStr_officersCanSetRules=prefRules+"officersCanSetRules";
	public static final String confStr_maxRulesPerFaction=prefRules+"maxRulesPerFaction";
	public static final String prefPeaceful="peaceful"+delim;
	public static final String confStr_leadersCanToggleState=prefPeaceful+"leadersCanToggleState";
	public static final String confStr_officersCanToggleState=prefPeaceful+"officersCanToggleState";
	public static final String confStr_membersCanToggleState=prefPeaceful+"membersCanToggleState";
	public static final String confStr_enablePeacefulBoosts=prefPeaceful+"enablePeacefulBoosts";
	public static final String confStr_powerBoostIfPeaceful=prefPeaceful+"powerBoostIfPeaceful";
	public static final String prefPowerboosts="powerboosts"+delim;
	public static final String confStr_enablePowerBoosts=prefPowerboosts+"enablePowerBoosts";
	public static final String confStr_extraPowerWhenKillPlayer=prefPowerboosts+"extraPowerWhenKillPlayer";
	public static final String confStr_extraPowerLossIfDeathBySuicide=prefPowerboosts+"extraPowerLossIfDeathBySuicide";
	public static final String confStr_extraPowerLossIfDeathByPVP=prefPowerboosts+"extraPowerLossIfDeathByPVP";
	public static final String confStr_extraPowerLossIfDeathByMob=prefPowerboosts+"extraPowerLossIfDeathByMob";
	public static final String confStr_extraPowerLossIfDeathByCactus=prefPowerboosts+"extraPowerLossIfDeathByCactus";
	public static final String confStr_extraPowerLossIfDeathByTNT=prefPowerboosts+"extraPowerLossIfDeathByTNT";
	public static final String confStr_extraPowerLossIfDeathByFire=prefPowerboosts+"extraPowerLossIfDeathByFire";
	public static final String confStr_extraPowerLossIfDeathByPotion=prefPowerboosts+"extraPowerLossIfDeathByPotion";
	public static final String confStr_extraPowerLossIfDeathByOther=prefPowerboosts+"extraPowerLossIfDeathByOther";
	public static final String prefAnnounce="announce"+delim;
	public static final String confStr_enableAnnounce=prefAnnounce+"enableAnnounce";
	public static final String confStr_leadersCanAnnounce=prefAnnounce+"leadersCanAnnounce";
	public static final String confStr_officersCanAnnounce=prefAnnounce+"officersCanAnnounce";
	public static final String confStr_showLastAnnounceOnLogin=prefAnnounce+"showLastAnnounceOnLogin";
	public static final String confStr_showLastAnnounceOnLandEnter=prefAnnounce+"showLastAnnounceOnLandEnter";
	public static final String prefEconomy="economy"+delim;
	public static final String confStr_enableEconomy=prefEconomy+"enableEconomy";
	public static final String confStr_economyCostToWarp=prefEconomy+"economyCostToWarp";
	public static final String confStr_economyCostToCreateWarp=prefEconomy+"economyCostToCreateWarp";
	public static final String confStr_economyCostToDeleteWarp=prefEconomy+"economyCostToDeleteWarp";
	public static final String confStr_economyCostToAnnounce=prefEconomy+"economyCostToAnnounce";
	public static final String confStr_economyCostToJail=prefEconomy+"economyCostToJail";
	public static final String confStr_economyCostToSetJail=prefEconomy+"economyCostToSetJail";
	public static final String confStr_economyCostToUnJail=prefEconomy+"economyCostToUnJail";
	public static final String confStr_economyCostToToggleUpPeaceful=prefEconomy+"economyCostToToggleUpPeaceful";
	public static final String confStr_economyCostToToggleDownPeaceful=prefEconomy+"economyCostToToggleDownPeaceful";
	public static final String prefTeleports="Teleports"+delim;
	public static final String confStr_disallowTeleportingToEnemyLandViaHomeCommand= prefTeleports+"disallowTeleportingToEnemyLandViaHomeCommand";
	public static final String confStr_reportSuccessfulByCommandTeleportsIntoEnemyLand=prefTeleports+"reportSuccessfulByCommandTeleportsIntoEnemyLand";
	public static final String confStr_disallowTeleportingToEnemyLandViaEnderPeals=prefTeleports+"disallowTeleportingToEnemyLandViaEnderPeals";
	public static final String prefExtras="extras"+delim;
	public static final String confStr_disableUpdateCheck=prefExtras+"disableUpdateCheck";
	public static final String prefExtrasLWC=prefExtras+"LWC"+delim;
	public static final String confStr_removeLWCLocksOnClaim=prefExtrasLWC+"removeLWCLocksOnClaim";
	public static final String confStr_blockCPublicAccessOnNonOwnFactionTerritory=prefExtrasLWC+"blockCPublicAccessOnNonOwnFactionTerritory";
	public static final String prefExtrasMD=prefExtras+"disguise"+delim;
	public static final String confStr_enableDisguiseIntegration=prefExtrasMD+"enableDisguiseIntegration";
	public static final String confStr_unDisguiseIfInOwnTerritory=prefExtrasMD+"unDisguiseIfInOwnTerritory";
	public static final String confStr_unDisguiseIfInEnemyTerritory=prefExtrasMD+"unDisguiseIfInEnemyTerritory";
	public static final String confStr_DoNotChangeMe="DoNotChangeMe";
	//End Config String Pointer
	
	
	private static File currentFolder_OnPluginClassInit;
	private static       File currentFolder_OnEnable=null;

	/**
	 * call this in plugin.onLoad (the thing that happens before onEnable() )
	 * @param plugin
	 */
	protected static void onLoad() {
		if (Q.isInconsistencyFileBug()) {
			FactionsPlus.bailOut( "Please do not have `user.dir` property set, it will mess up so many things" );
		}

	}
	
	/**
	 * called on plugin.onEnable() and every time you want the config to reload
	 */
	protected static void reload() {
		Config.config=null;//must be here to cause config to reload on every plugin(s) reload from console
	}
	
}
