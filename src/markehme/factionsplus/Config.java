package markehme.factionsplus;

import java.io.*;
import java.lang.reflect.*;

import markehme.factionsplus.extras.*;

import org.bukkit.configuration.file.*;
import org.bukkit.plugin.*;


public abstract class Config {//not named Conf so to avoid conflicts with com.massivecraft.factions.Conf

	//could use Plugin.getDataFolder() (tho no need) and move these to onEnable() or onLoad() else will likely NPE if using getDataFolder()
	public static final File	folderBase= new File( "plugins" + File.separator + "FactionsPlus" );//just never be "" cause that means root folder
	public static final File	folderWarps	= new File( folderBase, "warps" );
	public static final File	folderJails	= new File(folderBase, "jails" );
	public static final File	folderAnnouncements	= new File( folderBase, "announcements" );
	public static final File	folderFRules	= new File( folderBase, "frules" );
	public static final File	folderFBans	= new File( folderBase, "fbans" );
	public static final File	fileDisableInWarzone = new File( folderBase, "disabled_in_warzone.txt");
	
	public static File templatesFile = new File(folderBase , "templates.yml");
	public static FileConfiguration templates;
	
	static final String	fileConfigDefaults	= "config_defaults.yml";//this file is located inside .jar in root dir
	//and it contains the defaults, so that they are no longer hardcoded in java code
	public static File fileConfig = new File(Config.folderBase , "config.yml");
	
	public static FileConfiguration config;
	
	
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
		boolean failed = false;
		try {
			if ( Q.isInconsistencyFileBug() ) {
				throw FactionsPlusPlugin
					.bailOut( "Please do not have `user.dir` property set, it will mess up so many things"
						+ "(or did you use native functions to change current folder from the one that was on jvm startup?!)" );
			}
			
			if ( hasFileFieldsTrap() ) {
				throw FactionsPlusPlugin.bailOut( "there is a coding trap which will likely cause unexpected behaviour "
					+ "in places that use files, tell plugin author to fix" );
			}
		} catch ( Throwable t ) {
			failed = true;
			Q.rethrow( t );
		} finally {
			if ( failed ) {
				FactionsPlus.instance.setDisAllowPluginToEnable();
			}
		}
		
	}

	/**
	 * make sure all the File fields in this class that are likely used somewhere else in constructors like new File(field, myfile);
	 * are non-empty to avoid 'myfile' being in root of drive instead of just current folder as expected<br>
	 * this would cause some evil inconsistencies if any of those fields would resolve to empty paths<br>
	 */
	private static boolean hasFileFieldsTrap() {
		Class classToCheckFor_FileFields = Config.class;
		Field[] allFields = classToCheckFor_FileFields.getFields();
		for ( Field field : allFields ) {
			if (File.class.equals( field.getType())) {
				//got one File field to check
				try {
					File instance = (File)field.get( classToCheckFor_FileFields );
					if (instance.getPath().isEmpty()) {
						//oops, found one, to avoid traps where you expect new File( instance, yourfile); 
						// to have 'yourfile' in root folder of that drive ie. '\yourfile' instead of what you might 
						//expect "yourfile" to be just in current folder just like a new File(yourfile) would do
						return true;
					}
				} catch ( IllegalArgumentException e ) {
					Q.rethrow(e);
				} catch ( IllegalAccessException e ) {
					Q.rethrow(e);
				}
			}
		}
		return false;
	}

	/**
	 * called on plugin.onEnable() and every time you want the config to reload
	 */
	protected static void reload() {
		Config.config=null;//must be here to cause config to reload on every plugin(s) reload from console
	}

	
}
