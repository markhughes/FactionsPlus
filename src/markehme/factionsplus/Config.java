package markehme.factionsplus;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import markehme.factionsplus.extras.*;

import org.bukkit.configuration.*;
import org.bukkit.configuration.file.*;
import org.bukkit.plugin.*;
import org.yaml.snakeyaml.*;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;


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
	
	private static final String	fileConfigDefaults	= "config_defaults.yml";//this file is located inside .jar in root dir
	//and it contains the defaults, so that they are no longer hardcoded in java code
	private static File fileConfig = new File(Config.folderBase , "config.yml");
	
	public static FileConfiguration config;
	
	
	//Begin Config String Pointers
	/* the format for these is:
	 * str_NAME
	 * oa_NAME
	 * _NAME
	 * where NAME is something like officersCanSetJails
	 * and oa stands for obsolete aliases such as the old names that this config had, this is used to carry over the 
	 *  old setting to the new setting just in case the new setting had a change in name or location(the prefixes like extras.lwc.) 
	 * so example: 
	 * str_disableSomeLocksOnClaim="extras.lwc.disableAllLocksOnClaim";
	 * if the old version of the config has extras.lwc.disableAllLocksOnClaim: true
	 * and the new version of the FactionsPlus has renamed this setting to lwc.disableNonFactionMembersLocksOnClaim
	 * then the oa_disableSomeLocksOnClaim would now contain the {"extras.lwc.disableAllLocksOnClaim"} just to make sure 
	 * the old value of "true" is preserved
	 * when the old entry is removed and then the new one will look like lwc.disableNonFactionMembersLocksOnClaim: true  even though
	 * the lwc.disableNonFactionMembersLocksOnClaim would have had a default value of false which means _disableSomeLocksOnClaim=false; here
	 * another example:
	 * str_officersCanSetJails="jails.officersCanSetJails";
	 * oa_officersCanSetJails={ "officersCanSetJails" };
	 * _officersCanSetJails=true;//the default value, which will be overwritten with the value found in the existing config(if any)
	 * if the config doesn't have this key, then this default value will be added to the config 
	 */
	
	/*
	 * there are two types of defaults:
	 * 1. those that are in this file aka hardcoded
	 * 2. those in config_defaults.yml file which will be located inside the exported .jar
	 * and there's the user's config.yml file which is the real user-modifiable config (3.)
	 * 
	 * the priority of the settings goes in this order: 3,2,1
	 * ie. if the config has setting extras.lwc.disableAllLocksOnClaim set to anything, 
	 *     then this will override the setting in 2. (if it even existed there) and in 1. which always existed as _disableSomeLocksOnClaim
	 * 
	 * maybe I'm explaining this badly :) let's try this way:
	 *  X=1.X;
	 *  if (2.X.exists()) X=2.X;
	 *  if (3.X.exists()) X=3.X 
	 *  
	 *  and on config save, the setting X will now exist in 3.  (but not in 2. because we don't write in the config_default.yml inside the .jar)
	 *  typically config save will happen if there are new settings (those that didn't exist in 3.)
	 */
	public static final String delim=".";

	public static final String prefixJails="jails"+Config.delim;
	public static final String str_enableJails = prefixJails+"enableJails";
	public static boolean _enableJails=true;//default
	public static final String[] oa_enableJails={
		//hmm there were none for this
	};
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
		Config.templates=null;
		boolean failed = false;
		try {
			
			Config.ensureFoldersExist();
			
			Config.config = getConfig();// load config
			
			
			
			Config.templates = YamlConfiguration.loadConfiguration( Config.templatesFile );
			
//			_enableJails = ( (Boolean);
			System.out.println(Config.config.getInt( str_economyCostToAnnounce) );
		}catch(Throwable t){
			Q.rethrow(t);
		}
		finally{
			if (failed) {
				FactionsPlus.instance.disableSelf();//must make sure we're disabled if something failed if not /plugins  would show us green
				//but mostly, for consistency's sake and stuff we couldn't think of/anticipate now
			}
		}
	}

	protected static void ensureFoldersExist() {
		try {
			addDir(Config.folderBase);
			addDir( Config.folderWarps );
			addDir( Config.folderJails );
			addDir( Config.folderAnnouncements );
			addDir(Config.folderFRules);
			addDir( Config.folderFBans );
			
			if(!Config.fileDisableInWarzone.exists()) {
				Config.fileDisableInWarzone.createNewFile();
				FactionsPlusPlugin.info("Created file: "+Config.fileDisableInWarzone);
			}
			
			if (!Config.templatesFile.exists()) {
				
				FactionsPlusTemplates.createTemplatesFile();
				FactionsPlusPlugin.info("Created file: "+Config.templatesFile);
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
			throw FactionsPlusPlugin.bailOut("something failed when ensuring the folders exist");
		}
	}
	
	private static final void addDir(File dir) {
		if(!dir.exists()) {
			if (dir.getPath().isEmpty()) {
				throw FactionsPlusPlugin.bailOut( "bad coding, this should usually not trigger here, but earlier" );
			}
			FactionsPlusPlugin.info("Added directory: "+dir);
			dir.mkdirs();
		}
	}
	
	public final static FileConfiguration getConfig() {
		if (null == Config.config) {
			reloadConfig();
		}
		if (null == Config.config) {
			throw FactionsPlusPlugin.bailOut("reloading config failed somehow and this should not be reached");//bugged reloadConfig() if reached
		}
		return Config.config;
	}
	
	public final static void saveConfig() {
		try {
			getConfig().save( Config.fileConfig );
		} catch ( IOException e ) {
			e.printStackTrace();
			throw FactionsPlusPlugin.bailOut("could not save config file: "+Config.fileConfig.getAbsolutePath());
		}
	}
	
	public final static void reloadConfig() {
		1
		YamlConfiguration c = new YamlConfiguration();//.loadFromString("");
		String key1 = "Test";
		String key2 = "test";
		c.set( "test",Boolean.TRUE);
		assert null == c.get(key1);
		assert !c.contains( key1 );
		assert null != c.get(key2);
		assert c.contains( key2 );
		c.set(key1,Boolean.FALSE);
		assert null != c.get(key1);
		assert c.contains( key1 );
		assert null != c.get( key2 );
		assert c.contains( key2 );
		for (   Map.Entry<String,Object> element : c.getValues( true ).entrySet() ) {
//			System.out.println(element);
			FactionsPlus.info( element.getKey()+" + "+element.getValue() );
		};
		
		FactionsPlus.bailOut( "");//FIXME: temporary all from above
		
		// always get defaults, we never know how many settings (from the defaults) are missing in the existing config file
		InputStream defConfigStream = FactionsPlus.instance.getResource( Config.fileConfigDefaults );// this is the one inside the .jar
		if ( defConfigStream != null ) {
			Config.config = YamlConfiguration.loadConfiguration( defConfigStream );
		} else {
			throw FactionsPlusPlugin.bailOut( "There is no '"+Config.fileConfigDefaults+"'(supposed to contain the defaults) inside the .jar\n"
				+ "which means that the plugin author forgot to include it" );
		}
		
		if ( Config.fileConfig.exists() ) {
			if (!Config.fileConfig.isFile()) {
				throw FactionsPlusPlugin.bailOut( "While '"+Config.fileConfig.getAbsolutePath()+"' exists, it is not a file!");
			}
			// config file exists? we add the settings on top, overwriting the defaults
			try {
				//even though this config exists, some defaults might be new so we still need to write the config out later with saveConfig();
				YamlConfiguration realConfig = YamlConfiguration.loadConfiguration( Config.fileConfig );
				for ( Map.Entry<String, Object> entry : realConfig.getValues( true ).entrySet() ) {
					Object val = entry.getValue();
					if ( !( val instanceof MemorySection ) ) {//ignore sections, parse only "var: value"  tuples else it won't carry over
						FactionsPlus.info( entry.getKey()+ " ! "+val );
						String key = entry.getKey();
						if (Config.config.contains( key)) {
							//we don't want to overwrite the key cause it may be different case, funnily enough this shouldn't matter but it freaking does
							if (str_economyCostToAnnounce.equalsIgnoreCase( key )) {
//								Config.config.get
								if (!str_economyCostToAnnounce.equals(key)) {
									System.out.println(key+"+"+str_economyCostToAnnounce+"+"+config.get(str_economyCostToAnnounce));
//									DumperOptions options = new DumperOptions();
//									options.setDefaultFlowStyle( FlowStyle.BLOCK1 )
									throw FactionsPlus.bailOut( "");

								}
							}
						}else {
							Config.config.set( key,val );// overwrites existing defaults already in config
						}
						FactionsPlus.info( ""+config.get(entry.getKey())+"/2/"+config.getInt( str_economyCostToAnnounce));
						FactionsPlus.info(str_economyCostToAnnounce+"//"+entry.getKey()+"//"+ config.get(str_economyCostToAnnounce));
					}
				}
			} catch ( Exception e ) {
				e.printStackTrace();
				throw FactionsPlusPlugin.bailOut( "failed to load existing config file '"+Config.fileConfig.getAbsolutePath()+"'");
			}
		}else {
			FactionsPlusPlugin.info(Config.fileConfig+" did not previously exist, creating a new config using defaults from the .jar");
		}
		
		saveConfig();
	}
	
}
