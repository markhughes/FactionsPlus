package markehme.factionsplus;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import markehme.factionsplus.extras.DCListener;
import markehme.factionsplus.extras.LWCFunctions;
import markehme.factionsplus.extras.LWCListener;
import markehme.factionsplus.extras.MDListener;
import markehme.factionsplus.extras.Metrics;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.griefcraft.lwc.LWCPlugin;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

/*
	- fixed onEntityDeath

TODO: LIST OF STUFF TO DO
- made maxWarps configuration option work

*/

public class FactionsPlus extends JavaPlugin {

	public static FactionsPlus plugin;
	
	public static Logger log = Logger.getLogger("Minecraft");
	
	Factions factions;
	FPlayers fplayers;
	Faction faction;
	
    public static Permission permission = null;
    public static Economy economy = null;
    
    public static final String  FP_TAG_IN_LOGS="[FactionsPlus] ";
	public static final File	folderBase= new File( "plugins" + File.separator + "FactionsPlus" + File.separator );
	public static final File	folderWarps	= new File( folderBase, "warps" );
	public static final File	folderJails	= new File( folderBase, "jails" );
	public static final File	folderAnnouncements	= new File( folderBase, "announcements" );
	public static final File	folderFRules	= new File( folderBase, "frules" );
	public static final File	folderFBans	= new File( folderBase, "fbans" );
	public static final File	fileDisableInWarzone = new File( folderBase, "disabled_in_warzone.txt");
	
	private static final File currentFolder_OnClassInit=Utilities.getCurrentFolder();
	private static       File currentFolder_OnEnable=null;

    public static File templatesFile = new File(folderBase , "templates.yml");
	public static File configFile = new File(folderBase , "config.yml");
	
	public static FileConfiguration wconfig;
	public static FileConfiguration config;
	public static FileConfiguration templates;

	public static boolean isMobDisguiseEnabled = false;
	public static boolean isDisguiseCraftEnabled = false;
	public static boolean isWorldEditEnabled = false;
	public static boolean isWorldGuardEnabled = false;
	public static boolean isLWCEnabled = false;
	
	public static boolean useLWCIntegrationFix = false;
	
	public final FactionsPlusListener FPListener = new FactionsPlusListener();
	
	public final DCListener DCListener = new DCListener();
	public final MDListener MDListener = new MDListener();
	public final LWCListener LWCListener = new LWCListener();

	public static WorldEditPlugin worldEditPlugin = null;
	public static WorldGuardPlugin worldGuardPlugin = null;
	
	public static String version;
	public static String FactionsVersion;
	public static boolean isOnePointSix;

	@Override
	public void onEnable() { 
		ensureConfigFilesLocationSafety();
	    
		PluginManager pm = this.getServer().getPluginManager();
		
		pm.registerEvents(this.FPListener, this);
		
		FactionsPlusJail.server = getServer();
		
		FactionsVersion = (this.getServer().getPluginManager().getPlugin("Factions").getDescription().getVersion());
		if(FactionsVersion.startsWith("1.6")) {
			isOnePointSix = true;
		} else {
			isOnePointSix = false;
		}
		info("Factions version " + FactionsVersion + " - " + isOnePointSix);
		
		try {
			addDir(folderBase).addDir( folderWarps ).addDir( folderJails ).addDir( folderAnnouncements );
			addDir(folderFRules).addDir( folderFBans );
			
			if(!fileDisableInWarzone.exists()) {
				fileDisableInWarzone.createNewFile();
				info("Created file: "+fileDisableInWarzone);
			}
			

			
		} catch (Exception e) {
			//FIXME: are we gonna allow the plugin to ignore exception here which means later will possibly fail to save config? 
			e.printStackTrace();
		}
		
		if(!FactionsPlus.configFile.exists()) {
			try {
				FactionsPlus.configFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			wconfig = YamlConfiguration.loadConfiguration(configFile);
			
			configFile.delete();
			configFile.createNewFile();
			
			config = YamlConfiguration.loadConfiguration(configFile);
			
			if(wconfig.isSet("disableUpdateCheck")) {
				config.set("disableUpdateCheck", wconfig.getBoolean("disableUpdateCheck"));
			} else config.set("disableUpdateCheck", false);
			
			if(wconfig.isSet("unDisguiseIfInOwnTerritory")) {
				config.set("unDisguiseIfInOwnTerritory", wconfig.getBoolean("unDisguiseIfInOwnTerritory"));
			} else config.set("unDisguiseIfInOwnTerritory", Boolean.valueOf(false));
			
			if(wconfig.isSet("unDisguiseIfInEnemyTerritory")) {
				config.set("unDisguiseIfInEnemyTerritory", wconfig.getBoolean("unDisguiseIfInEnemyTerritory"));
			} else config.set("unDisguiseIfInEnemyTerritory", Boolean.valueOf(false));
			
			if(wconfig.isSet("leadersCanSetWarps")) {
				config.set("leadersCanSetWarps", wconfig.getBoolean("leadersCanSetWarps"));
			} else config.set("leadersCanSetWarps", true);
			
			if(wconfig.isSet("officersCanSetWarps")) {
				config.set("officersCanSetWarps", wconfig.getBoolean("officersCanSetWarps"));
			} else config.set("officersCanSetWarps", true);
			
			if(wconfig.isSet("membersCanSetWarps")) {
				config.set("membersCanSetWarps", wconfig.getBoolean("membersCanSetWarps"));
			} else config.set("membersCanSetWarps", false);
			
			if(wconfig.isSet("warpSetting")) {
				config.set("warpSetting", wconfig.getInt("warpSetting"));
			} else config.set("warpSetting", Integer.valueOf(1));
			
			if(wconfig.isSet("maxWarps")) {
				config.set("maxWarps", wconfig.getInt("maxWarps"));
			} else config.set("maxWarps", Integer.valueOf(5));
			
			if(wconfig.isSet("mustBeInOwnTerritoryToCreate")) {
				config.set("mustBeInOwnTerritoryToCreate", wconfig.getBoolean("mustBeInOwnTerritoryToCreate"));
			} else config.set("mustBeInOwnTerritoryToCreate", true);
			
			if(wconfig.isSet("warpTeleportAllowedFromEnemyTerritory")){
				config.set("warpTeleportAllowedFromEnemyTerritory", wconfig.getBoolean("warpTeleportAllowedFromEnemyTerritory"));
			} else config.set("warpTeleportAllowedFromEnemyTerritory", true);
			
			if(wconfig.isSet("warpTeleportAllowedFromDifferentWorld")){
				config.set("warpTeleportAllowedFromDifferentWorld", wconfig.getBoolean("warpTeleportAllowedFromDifferentWorld"));
			} else config.set("warpTeleportAllowedFromDifferentWorld", true);
			
			if(wconfig.isSet("warpTeleportAllowedEnemyDistance")) {
				config.set("warpTeleportAllowedEnemyDistance", wconfig.getInt("warpTeleportAllowedEnemyDistance"));
			} else config.set("warpTeleportAllowedEnemyDistance", Integer.valueOf(35));
			
			if(wconfig.isSet("warpTeleportIgnoreEnemiesIfInOwnTerritory")){
				config.set("warpTeleportIgnoreEnemiesIfInOwnTerritory", wconfig.getBoolean("warpTeleportIgnoreEnemiesIfInOwnTerritory"));
			} else config.set("warpTeleportIgnoreEnemiesIfInOwnTerritory", true);
			
			if(wconfig.isSet("smokeEffectOnWarp")) {
				config.set("smokeEffectOnWarp", wconfig.getBoolean("smokeEffectOnWarp"));
			} else config.set("smokeEffectOnWarp", true);
			
			if(wconfig.isSet("powerBoostIfPeaceful")) {
				config.set("powerBoostIfPeaceful", wconfig.getInt("powerBoostIfPeaceful"));
			} else config.set("powerBoostIfPeaceful", Integer.valueOf(0));
			
			if(wconfig.isSet("leadersCanSetJails")) {
				config.set("leadersCanSetJails", wconfig.getBoolean("leadersCanSetJails"));
			} else config.set("leadersCanSetJails", true);
			
			if(wconfig.isSet("officersCanSetJails")) {
				config.set("officersCanSetJails", wconfig.getBoolean("officersCanSetJails"));
			} else config.set("officersCanSetJails", true);
			
			if(wconfig.isSet("leadersCanSetRules")) {
				config.set("leadersCanSetRules", wconfig.getBoolean("leadersCanSetRules"));
			} else config.set("leadersCanSetRules", true);
			
			if(wconfig.isSet("officersCanSetRules")) {
				config.set("officersCanSetRules", wconfig.getBoolean("officersCanSetRules"));
			} else config.set("officersCanSetRules", true);
			
			if(wconfig.isSet("maxRulesPerFaction")) {
				config.set("maxRulesPerFaction", wconfig.getInt("maxRulesPerFaction"));
			} else config.set("maxRulesPerFaction", Integer.valueOf(12));
			
			if(wconfig.isSet("membersCanSetJails")) {
				config.set("membersCanSetJails", wconfig.getBoolean("membersCanSetJails"));
			} else config.set("membersCanSetJails", false);
			
			if(wconfig.isSet("leadersCanJail")) {
				config.set("leadersCanJail", wconfig.getBoolean("leadersCanJail"));
			} else config.set("leadersCanJail", true);
			
			if(wconfig.isSet("officersCanJail")) {
				config.set("officersCanJail", wconfig.getBoolean("officersCanJail"));
			} else config.set("officersCanJail", true);
			
			if(wconfig.isSet("leadersCanAnnounce")) {
				config.set("leadersCanAnnounce", wconfig.getBoolean("leadersCanAnnounce"));
			} else config.set("leadersCanAnnounce", true);
			
			if(wconfig.isSet("officersCanAnnounce")) {
				config.set("officersCanAnnounce", wconfig.getBoolean("officersCanAnnounce"));
			} else config.set("officersCanAnnounce", true);
			
			if(wconfig.isSet("showLastAnnounceOnLogin")) {
				config.set("showLastAnnounceOnLogin", wconfig.getBoolean("showLastAnnounceOnLogin"));
			} else config.set("showLastAnnounceOnLogin", true);
			
			if(wconfig.isSet("showLastAnnounceOnLandEnter")) {
				config.set("showLastAnnounceOnLandEnter", wconfig.getBoolean("showLastAnnounceOnLandEnter"));
			} else config.set("showLastAnnounceOnLandEnter", true);
			
			if(wconfig.isSet("leadersCanFactionBan")) {
				config.set("leadersCanFactionBan", wconfig.getBoolean("leadersCanFactionBan"));
			} else config.set("leadersCanFactionBan", true);
				
			if(wconfig.isSet("officersCanFactionBan")) {
				config.set("officersCanFactionBan", wconfig.getBoolean("officersCanFactionBan"));
			} else config.set("officersCanFactionBan", true);
				
			if(wconfig.isSet("leaderCanNotBeBanned")) {
				config.set("leaderCanNotBeBanned", wconfig.getBoolean("leaderCanNotBeBanned"));
			} else config.set("leaderCanNotBeBanned", true);
			
			if(wconfig.isSet("leadersCanToggleState")) {
				config.set("leadersCanToggleState", wconfig.getBoolean("leadersCanToggleState"));
			} else config.set("leadersCanToggleState", false);
			
			if(wconfig.isSet("officersCanToggleState")) {
				config.set("officersCanToggleState", wconfig.getBoolean("officersCanToggleState"));
			} else config.set("officersCanToggleState", false);
			
			if(wconfig.isSet("membersCanToggleState")) {
				config.set("membersCanToggleState", wconfig.getBoolean("membersCanToggleState"));
			} else config.set("membersCanToggleState", false);
			
			if(wconfig.isSet("extraPowerLossIfDeathByOther")) {
				config.set("extraPowerLossIfDeathByOther", wconfig.getDouble("extraPowerLossIfDeathByOther"));
			} else config.set("extraPowerLossIfDeathByOther", Double.valueOf(0));
			
			if(wconfig.isSet("extraPowerWhenKillPlayer")) {
				config.set("extraPowerWhenKillPlayer", wconfig.getDouble("extraPowerWhenKillPlayer"));
			} else config.set("extraPowerWhenKillPlayer", Double.valueOf(0));
			
			if(wconfig.isSet("extraPowerLossIfDeathBySuicide")) {
				config.set("extraPowerLossIfDeathBySuicide", wconfig.getDouble("extraPowerLossIfDeathBySuicide"));
			} else config.set("extraPowerLossIfDeathBySuicide", Double.valueOf(0));
			
			if(wconfig.isSet("extraPowerLossIfDeathByPVP")) {
				config.set("extraPowerLossIfDeathByPVP", wconfig.getDouble("extraPowerLossIfDeathByPVP"));
			} else config.set("extraPowerLossIfDeathByPVP", Double.valueOf(0));
			
			if(wconfig.isSet("extraPowerLossIfDeathByMob")) {
				config.set("extraPowerLossIfDeathByMob", wconfig.getDouble("extraPowerLossIfDeathByMob"));
			} else config.set("extraPowerLossIfDeathByMob", Double.valueOf(0));
			
			if(wconfig.isSet("extraPowerLossIfDeathByCactus")) {
				config.set("extraPowerLossIfDeathByCactus", wconfig.getDouble("extraPowerLossIfDeathByCactus"));
			} else config.set("extraPowerLossIfDeathByCactus", Double.valueOf(0));
			
			if(wconfig.isSet("extraPowerLossIfDeathByTNT")) {
				config.set("extraPowerLossIfDeathByTNT", wconfig.getDouble("extraPowerLossIfDeathByTNT"));
			} else config.set("extraPowerLossIfDeathByTNT", Double.valueOf(0));
			
			if(wconfig.isSet("extraPowerLossIfDeathByFire")) {
				config.set("extraPowerLossIfDeathByFire", wconfig.getDouble("extraPowerLossIfDeathByFire"));
			} else config.set("extraPowerLossIfDeathByFire", Double.valueOf(0));
			
			if(wconfig.isSet("extraPowerLossIfDeathByPotion")) {
				config.set("extraPowerLossIfDeathByPotion", wconfig.getDouble("extraPowerLossIfDeathByPotion"));
			} else config.set("extraPowerLossIfDeathByPotion", Double.valueOf(0));
			
			if(wconfig.isSet("enablePermissionGroups")) {
				config.set("enablePermissionGroups", wconfig.getBoolean("enablePermissionGroups"));
			} else config.set("enablePermissionGroups", Boolean.valueOf(false));
			
			if(wconfig.isSet("economy_enable")) {
				config.set("economy_enable", wconfig.getBoolean("economy_enable"));
			} else config.set("economy_enable", Boolean.valueOf(false));
				
			if(wconfig.isSet("economy_costToWarp")) {
				config.set("economy_costToWarp", wconfig.getInt("economy_costToWarp"));
			} else config.set("economy_costToWarp", Integer.valueOf(0));
			
			if(wconfig.isSet("economy_costToCreateWarp")) {
				config.set("economy_costToCreateWarp", wconfig.getInt("economy_costToCreateWarp"));
			} else config.set("economy_costToCreateWarp", Integer.valueOf(0));
			
			if(wconfig.isSet("economy_costToDeleteWarp")) {
				config.set("economy_costToDeleteWarp", wconfig.getInt("economy_costToDeleteWarp"));
			} else config.set("economy_costToDeleteWarp", Integer.valueOf(0));
			
			if(wconfig.isSet("economy_costToAnnounce")) {
				config.set("economy_costToAnnounce", wconfig.getInt("economy_costToAnnounce"));
			} else config.set("economy_costToAnnounce", Integer.valueOf(0));
			
			if(wconfig.isSet("economy_costToJail")) {
				config.set("economy_costToJail", wconfig.getInt("economy_costToJail"));
			} else config.set("economy_costToJail", Integer.valueOf(0));
			
			if(wconfig.isSet("economy_costToSetJail")) {
				config.set("economy_costToSetJail", wconfig.getInt("economy_costToSetJail"));
			} else config.set("economy_costToSetJail", Integer.valueOf(0));
			
			if(wconfig.isSet("economy_costToUnJail")) {
				config.set("economy_costToUnJail", wconfig.getInt("economy_costToUnJail"));
			} else config.set("economy_costToUnJail", Integer.valueOf(0));
				
			if(wconfig.isSet("economy_costToToggleUpPeaceful")) {
				config.set("economy_costToToggleUpPeaceful", wconfig.getInt("economy_costToToggleUpPeaceful"));
			} else config.set("economy_costToToggleUpPeaceful", Integer.valueOf(0));
			
			if(wconfig.isSet("economy_costToToggleDownPeaceful")) {
				config.set("economy_costToToggleDownPeaceful", wconfig.getInt("economy_costToToggleDownPeaceful"));
			} else config.set("economy_costToToggleDownPeaceful", Integer.valueOf(0));
			
			if(wconfig.isSet("useLWCIntegrationFix")) {
				config.set("useLWCIntegrationFix", wconfig.getBoolean("useLWCIntegrationFix"));
			} else config.set("useLWCIntegrationFix", false);
			
			config.set("DoNotChangeMe", Integer.valueOf(8));
			
			config.save(configFile);
			
			saveConfig();
		} catch(Exception e) {
		   	e.printStackTrace();
		   	info("An error occured while managing the configuration file (-18)");
		   	getPluginLoader().disablePlugin(this);
		}		
		
		if (!templatesFile.exists()) {
			
			FactionsPlusTemplates.createTemplatesFile();
		    
		} 
		
		templates = YamlConfiguration.loadConfiguration(templatesFile);	
		config = YamlConfiguration.loadConfiguration(configFile);
		
		FactionsPlusCommandManager.setup();
		FactionsPlusHelpModifier.modify();
		
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        
        
        if(config.getBoolean("economy_enable")) {
        	RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        	
        	if (economyProvider != null) {
            	economy = economyProvider.getProvider();
        	}
        }
        
        if(getServer().getPluginManager().isPluginEnabled("DisguiseCraft")) {
        	pm.registerEvents(this.DCListener, this);
        	info("Hooked into DisguiseCraft!");
        	isDisguiseCraftEnabled = true;
        }
        
        if(getServer().getPluginManager().isPluginEnabled("MobDisguise")) {
        	pm.registerEvents(this.MDListener, this);
        	info("Hooked into MobDisguise!");
        	isMobDisguiseEnabled = true;
        }
        
        if(getServer().getPluginManager().isPluginEnabled("WorldEdit")) {
        	worldEditPlugin = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
        	info("Hooked into WorldEdit!");
        	isWorldEditEnabled = true;
        }
        
        if(getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
        	worldGuardPlugin = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
        	info("Hooked into WorldGuard!");
        	isWorldGuardEnabled = true;
        }
        if(config.getBoolean("useLWCIntegrationFix") == true) {
            if(getServer().getPluginManager().isPluginEnabled("LWC")) {
            	pm.registerEvents(this.LWCListener, this);
            	info("Hooked into LWC!");
            	LWCFunctions.integrateLWC((LWCPlugin)getServer().getPluginManager().getPlugin("LWC"));
            	isLWCEnabled = true;
            	
            }
            else {
            	info("No LWC Found but Integration Option Is Enabled!");
            }
        }

        
        
        FactionsPlus.config = YamlConfiguration.loadConfiguration(FactionsPlus.configFile);
        

        version = getDescription().getVersion();
        
        FactionsPlusUpdate.checkUpdates();
        
		info("Ready.");
		
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) {
		    info("Waah! Couldn't metrics-up! :'(");
		}
	}



	@Override
	public void onDisable() {
		info("Disabled.");
	}
	
	
	/**
	 * to be called only in onEnable();
	 */
	private static void ensureConfigFilesLocationSafety() throws RuntimeException {
		// the following paranoid check is to make sure the configs don't get corrupted (so to speak) if the current folder
		// miraculously changed(ie. a different folder is now the current folder) by the time onEnable was called
		currentFolder_OnEnable = Utilities.getCurrentFolder();
		if ( ( null == currentFolder_OnEnable ) || ( null == currentFolder_OnClassInit )
			|| ( !currentFolder_OnEnable.equals( currentFolder_OnClassInit ) ) )
		{
			log.severe( FP_TAG_IN_LOGS+"Bukkit(or something) changed current folder between the time the class was inited & "
				+ "the time onEnable() was issued.\nThis will mess up some thing as this was not expected, therefore we stop here" );
			throw new RuntimeException( "currentFolder_OnClassInit=`" + currentFolder_OnClassInit
				+ "` differs from currentFolder_OnEnable=`" + currentFolder_OnEnable + "`" );
			// this typically means that any new File(path) calls in the above class fields have a different current folder than
			// any of the new File() calls after onEnable() was issued, which further means FactionPlus configs may be
			// read/written to a different location
			// but the thing is, this is expected/assumed from bukkit that the current folder between class init and
			// onEnable() would not be changed
		}
	}
	
	private FactionsPlus addDir(File dir) {
		if(!dir.exists()) {
			info("Added directory: "+dir);
			dir.mkdirs();
		}
		return this;
	}
	
	public static void info(String logInfoMsg) {
		log.info( FP_TAG_IN_LOGS+logInfoMsg );
	}
}
