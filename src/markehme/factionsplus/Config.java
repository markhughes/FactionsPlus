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
	
	
	//Begin Config String Pointers
	public static final String delim=".";

	public static final String prefixJails="jails"+Config.delim;
	public static final String str_enableJails = prefixJails+"enableJails";
	public static final String str_leadersCanSetJails = prefixJails+"leadersCanSetJails";
	public static final String str_officersCanSetJails = prefixJails+"officersCanSetJails";
	public static final String str_membersCanSetJails = prefixJails+"membersCanSetJails";
	public static final String str_leadersCanJail = prefixJails+"leadersCanJail";
	public static final String str_officersCanJail = prefixJails+"officersCanJail";

	public static final String prefixWarps="warps"+delim;
	public static final String str_enableWarps = prefixWarps+"enableWarps";
	public static final String str_leadersCanSetWarps = prefixWarps+"leadersCanSetWarps";
	public static final String str_officersCanSetWarps = prefixWarps+"officersCanSetWarps";
	public static final String str_membersCanSetWarps = prefixWarps+"membersCanSetWarps";
	public static final String str_mustBeInOwnTerritoryToCreate  = prefixWarps+"mustBeInOwnTerritoryToCreate";
	public static final String str_maxWarps = prefixWarps+"maxWarps";
	public static final String str_warpTeleportAllowedFromEnemyTerritory = prefixWarps+"warpTeleportAllowedFromEnemyTerritory";
	public static final String str_warpTeleportAllowedFromDifferentWorld = prefixWarps+"warpTeleportAllowedFromDifferentWorld";
	public static final String str_warpTeleportAllowedEnemyDistance = prefixWarps+"warpTeleportAllowedEnemyDistance";
	public static final String str_warpTeleportIgnoreEnemiesIfInOwnTerritory = prefixWarps+"warpTeleportIgnoreEnemiesIfInOwnTerritory";
	public static final String str_smokeEffectOnWarp = prefixWarps+"smokeEffectOnWarp";
	
	public static final String prefixBanning="banning"+delim;
	public static final String str_enableBans=prefixBanning+"enableBans";
	public static final String str_leadersCanFactionBan=prefixBanning+"leadersCanFactionBan";
	public static final String str_officersCanFactionBan=prefixBanning+"officersCanFactionBan";
	public static final String str_leadersCanFactionUnban=prefixBanning+"leadersCanFactionUnban";
	public static final String str_officersCanFactionUnban=prefixBanning+"officersCanFactionUnban";
	public static final String str_leaderCanNotBeBanned=prefixBanning+"leaderCanNotBeBanned";

	public static final String prefixRules="rules"+delim;
	public static final String str_enableRules=prefixRules+"enableRules";
	public static final String str_leadersCanSetRules=prefixRules+"leadersCanSetRules";
	public static final String str_officersCanSetRules=prefixRules+"officersCanSetRules";
	public static final String str_maxRulesPerFaction=prefixRules+"maxRulesPerFaction";

	public static final String prefixPeaceful="peaceful"+delim;
	public static final String str_leadersCanToggleState=prefixPeaceful+"leadersCanToggleState";
	public static final String str_officersCanToggleState=prefixPeaceful+"officersCanToggleState";
	public static final String str_membersCanToggleState=prefixPeaceful+"membersCanToggleState";
	public static final String str_enablePeacefulBoosts=prefixPeaceful+"enablePeacefulBoosts";
	public static final String str_powerBoostIfPeaceful=prefixPeaceful+"powerBoostIfPeaceful";

	public static final String prefixPowerboosts="powerboosts"+delim;
	public static final String str_enablePowerBoosts=prefixPowerboosts+"enablePowerBoosts";
	public static final String str_extraPowerWhenKillPlayer=prefixPowerboosts+"extraPowerWhenKillPlayer";
	public static final String str_extraPowerLossIfDeathBySuicide=prefixPowerboosts+"extraPowerLossIfDeathBySuicide";
	public static final String str_extraPowerLossIfDeathByPVP=prefixPowerboosts+"extraPowerLossIfDeathByPVP";
	public static final String str_extraPowerLossIfDeathByMob=prefixPowerboosts+"extraPowerLossIfDeathByMob";
	public static final String str_extraPowerLossIfDeathByCactus=prefixPowerboosts+"extraPowerLossIfDeathByCactus";
	public static final String str_extraPowerLossIfDeathByTNT=prefixPowerboosts+"extraPowerLossIfDeathByTNT";
	public static final String str_extraPowerLossIfDeathByFire=prefixPowerboosts+"extraPowerLossIfDeathByFire";
	public static final String str_extraPowerLossIfDeathByPotion=prefixPowerboosts+"extraPowerLossIfDeathByPotion";
	public static final String str_extraPowerLossIfDeathByOther=prefixPowerboosts+"extraPowerLossIfDeathByOther";

	public static final String prefixAnnounce="announce"+delim;
	public static final String str_enableAnnounce=prefixAnnounce+"enableAnnounce";
	public static final String str_leadersCanAnnounce=prefixAnnounce+"leadersCanAnnounce";
	public static final String str_officersCanAnnounce=prefixAnnounce+"officersCanAnnounce";
	public static final String str_showLastAnnounceOnLogin=prefixAnnounce+"showLastAnnounceOnLogin";
	public static final String str_showLastAnnounceOnLandEnter=prefixAnnounce+"showLastAnnounceOnLandEnter";

	public static final String prefixEconomy="economy"+delim;
	public static final String str_enableEconomy=prefixEconomy+"enableEconomy";
	public static final String str_economyCostToWarp=prefixEconomy+"economyCostToWarp";
	public static final String str_economyCostToCreateWarp=prefixEconomy+"economyCostToCreateWarp";
	public static final String str_economyCostToDeleteWarp=prefixEconomy+"economyCostToDeleteWarp";
	public static final String str_economyCostToAnnounce=prefixEconomy+"economyCostToAnnounce";
	public static final String str_economyCostToJail=prefixEconomy+"economyCostToJail";
	public static final String str_economyCostToSetJail=prefixEconomy+"economyCostToSetJail";
	public static final String str_economyCostToUnJail=prefixEconomy+"economyCostToUnJail";
	public static final String str_economyCostToToggleUpPeaceful=prefixEconomy+"economyCostToToggleUpPeaceful";
	public static final String str_economyCostToToggleDownPeaceful=prefixEconomy+"economyCostToToggleDownPeaceful";

	public static final String prefixTeleports="Teleports"+delim;
	public static final String str_disallowTeleportingToEnemyLandViaHomeCommand= prefixTeleports+"disallowTeleportingToEnemyLandViaHomeCommand";
	public static final String str_reportSuccessfulByCommandTeleportsIntoEnemyLand=prefixTeleports+"reportSuccessfulByCommandTeleportsIntoEnemyLand";
	public static final String str_disallowTeleportingToEnemyLandViaEnderPeals=prefixTeleports+"disallowTeleportingToEnemyLandViaEnderPeals";

	public static final String prefixExtras="extras"+delim;
	public static final String str_disableUpdateCheck=prefixExtras+"disableUpdateCheck";
	public static final String prefExtrasLWC=prefixExtras+"LWC"+delim;
	public static final String str_removeLWCLocksOnClaim=prefExtrasLWC+"removeLWCLocksOnClaim";
	public static final String str_blockCPublicAccessOnNonOwnFactionTerritory=prefExtrasLWC+"blockCPublicAccessOnNonOwnFactionTerritory";

	public static final String prefixExtrasMD=prefixExtras+"disguise"+delim;
	public static final String str_enableDisguiseIntegration=prefixExtrasMD+"enableDisguiseIntegration";
	public static final String str_unDisguiseIfInOwnTerritory=prefixExtrasMD+"unDisguiseIfInOwnTerritory";
	public static final String str_unDisguiseIfInEnemyTerritory=prefixExtrasMD+"unDisguiseIfInEnemyTerritory";
	
	public static final String str_DoNotChangeMe="DoNotChangeMe";
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
