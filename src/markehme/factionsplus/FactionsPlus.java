package markehme.factionsplus;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

import markehme.factionsplus.FactionsBridge.*;
import markehme.factionsplus.extras.*;
import markehme.factionsplus.listeners.*;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.*;
import org.bukkit.configuration.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.griefcraft.lwc.LWCPlugin;
import com.massivecraft.factions.*;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class FactionsPlus extends FactionsPlusPlugin {

	public static FactionsPlus instance;
	
	public static Logger log = Logger.getLogger("Minecraft");
	
	Factions factions;
	FPlayers fplayers;
	Faction faction;
	
    public static Permission permission = null;
    public static Economy economy = null;
    
    public static final String  FP_TAG_IN_LOGS="[FactionsPlus] ";
    public static boolean isMobDisguiseEnabled = false;
	public static boolean isDisguiseCraftEnabled = false;
	public static boolean isWorldEditEnabled = false;
	public static boolean isWorldGuardEnabled = false;
	
	public final AnnounceListener announcelistener = new AnnounceListener();
	public final BanListener banlistener = new BanListener();
	public final CoreListener corelistener = new CoreListener();
	public final DisguiseListener disguiselistener = new DisguiseListener();
	public final JailListener jaillistener = new JailListener();
	public final PeacefulListener peacefullistener = new PeacefulListener();
	public final PowerboostListener powerboostlistener = new PowerboostListener();
	
	public final DCListener dclistener = new DCListener();
	public final MDListener mdlistener = new MDListener();
	

	public static WorldEditPlugin worldEditPlugin = null;
	public static WorldGuardPlugin worldGuardPlugin = null;
	
	public static String version;
	public static String FactionsVersion;
	public static boolean isOnePointSix;
	
	private static Metrics metrics=null;

	
	public FactionsPlus() {//constructor
		super();
		instance=this;
	}
	
	@Override
	public void onLoad() {
		Config.onLoad();
	}
	
	@Override
	public void onDisable() {
		if (null != metrics) {
			try {
				metrics.disable();
			} catch ( IOException e ) {
				e.printStackTrace();
			}
		}
		LWCFunctions.ensure_LWC_Disintegrate();
		getServer().getServicesManager().unregisterAll(this);//not really needed at this point, only for when using .register(..)
		FactionsPlusPlugin.info("Disabled.");
	}
	
	
	@Override
	public void onEnable() { super.onEnable();//be first
		Config.reload();//be as soon as possible
	    
		PluginManager pm = this.getServer().getPluginManager();
		
		pm.registerEvents(this.corelistener, this);
		
		FactionsPlusJail.server = getServer();
		
		Bridge.init();
		FactionsVersion = (pm.getPlugin("Factions").getDescription().getVersion());
		if(FactionsVersion.startsWith("1.6")) {
			isOnePointSix = true;
		} else {
			isOnePointSix = false;
		}
		FactionsPlusPlugin.info("Factions version " + FactionsVersion + " - " + isOnePointSix);
		
		
		try {
			addDir(Config.folderBase).addDir( Config.folderWarps ).addDir( Config.folderJails ).addDir( Config.folderAnnouncements );
			addDir(Config.folderFRules).addDir( Config.folderFBans );
			
			if(!Config.fileDisableInWarzone.exists()) {
				Config.fileDisableInWarzone.createNewFile();
				FactionsPlusPlugin.info("Created file: "+Config.fileDisableInWarzone);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw FactionsPlusPlugin.bailOut("something failed when ensuring the folders exist");
		}
		
		Config.config = getConfig();//load config
		
		if (!Config.templatesFile.exists()) {
			
			FactionsPlusTemplates.createTemplatesFile();
		    
		} 
		
		Config.templates = YamlConfiguration.loadConfiguration(Config.templatesFile);	
		
		FactionsPlusCommandManager.setup();
		TeleportsListener.init(this);
		
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        
        
        if(Config.config.getBoolean(Config.str_enableEconomy)) {
        	RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        	
        	if (economyProvider != null) {
            	economy = economyProvider.getProvider();
        	}
        }
        if(Config.config.getBoolean(Config.str_enableAnnounce)) {
    		pm.registerEvents(this.announcelistener, this);
        }
        if(Config.config.getBoolean(Config.str_enableBans)) {
        	pm.registerEvents(this.banlistener, this);
        }
        if(Config.config.getBoolean(Config.str_enableJails)) {
        	pm.registerEvents(this.jaillistener, this);
        }
        if(Config.config.getBoolean(Config.str_enableDisguiseIntegration) && (Config.config.getBoolean(Config.str_unDisguiseIfInOwnTerritory) || Config.config.getBoolean(Config.str_unDisguiseIfInEnemyTerritory))) {
        	if(getServer().getPluginManager().isPluginEnabled("DisguiseCraft")) {
        		pm.registerEvents(this.dclistener, this);
        		FactionsPlusPlugin.info("Hooked into DisguiseCraft!");
        		isDisguiseCraftEnabled = true;
        		pm.registerEvents(this.disguiselistener, this);
        	}
        	if(getServer().getPluginManager().isPluginEnabled("MobDisguise")) {
        		pm.registerEvents(this.mdlistener, this);
        		FactionsPlusPlugin.info("Hooked into MobDisguise!");
        		isMobDisguiseEnabled = true;
        		pm.registerEvents(this.disguiselistener, this);
        	}
        	else {
        		FactionsPlusPlugin.info("MobDisguise or DisguiseCraft enabled, but no plugin found!");
        	}
        }
        if(1<2) {        //Temporary Always True Until a Config Option is Created 
        	if(getServer().getPluginManager().isPluginEnabled("WorldEdit")) {
        		worldEditPlugin = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
        		FactionsPlusPlugin.info("Hooked into WorldEdit!");
        		isWorldEditEnabled = true;
        	}
            if(getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
            	worldGuardPlugin = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
            	FactionsPlusPlugin.info("Hooked into WorldGuard!");
            	isWorldGuardEnabled = true;
            }
        }
        

        
        LWCFunctions.try_integrateLWC();
        
		if ( LWCFunctions.isLWC() ) {
			if ( ( com.massivecraft.factions.Conf.lwcIntegration ) && ( com.massivecraft.factions.Conf.onCaptureResetLwcLocks ) ) {
				// if Faction plugin has setting to reset locks (which only resets for chests)
				// then have FactionPlus suggest its setting so that also locked furnaces/doors etc. will get reset
				if ( !Config.config.getBoolean( Config.str_removeLWCLocksOnClaim ) ) {
					// TODO: maybe someone can modify this message so that it would make sense to the console reader
					FactionsPlusPlugin.info( "Consider setting " + Config.str_removeLWCLocksOnClaim
						+ " to reset locks for more than just the chests" );
					// this also means in Factions having onCaptureResetLwcLocks to false would be good, if ours is on true
				}
				
			}
		}
	
       
        
        
        if(Config.config.getBoolean(Config.str_enablePeacefulBoosts)) {
        	pm.registerEvents(this.peacefullistener, this);
        }
        if(Config.config.getBoolean(Config.str_enablePowerBoosts)) {
        	pm.registerEvents(this.powerboostlistener, this);
        }

        
        
        version = getDescription().getVersion();
        
        FactionsPlusUpdate.checkUpdates();
        
		FactionsPlusPlugin.info("Ready.");
		
		try {
			if (null == metrics) {
				//first time
				metrics = new Metrics(this);
				metrics.start();
			}else{
				//second+  time(s)
				metrics.enable();
			}
		    
		} catch (IOException e) {
		    FactionsPlusPlugin.info("Waah! Couldn't metrics-up! :'(");
		}
		
	}

	@Override
	public FileConfiguration getConfig() {
		if (null == Config.config) {
			reloadConfig();
		}
		if (null == Config.config) {
			throw FactionsPlusPlugin.bailOut("reloading config failed somehow and this should not be reached");//bugged reloadConfig() if reached
		}
		return Config.config;
	}
	
	@Override
	public void reloadConfig() {
		// always get defaults, we never know how many settings (from the defaults) are missing in the existing config file
		InputStream defConfigStream = getResource( Config.fileConfigDefaults );// this is the one inside the .jar
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
						Config.config.set( entry.getKey(),val );// overwrites existing defaults already in config
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
	
	@Override
	public void saveConfig() {
		try {
			getConfig().save( Config.fileConfig );
		} catch ( IOException e ) {
			e.printStackTrace();
			throw FactionsPlusPlugin.bailOut("could not save config file: "+Config.fileConfig.getAbsolutePath());
		}
	}
	
	
	
	private FactionsPlus addDir(File dir) {
		if(!dir.exists()) {
			if (dir.getPath().isEmpty()) {
				throw FactionsPlusPlugin.bailOut( "bad coding, this should usually not trigger here, but earlier" );
			}
			FactionsPlusPlugin.info("Added directory: "+dir);
			dir.mkdirs();
		}
		return this;
	}
}
