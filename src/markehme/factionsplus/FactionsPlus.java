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

public class FactionsPlus extends JavaPlugin {

	public static FactionsPlus instance;
	
	public static Logger log = Logger.getLogger("Minecraft");
	
	Factions factions;
	FPlayers fplayers;
	Faction faction;
	
    public static Permission permission = null;
    public static Economy economy = null;
    
    public static final String  FP_TAG_IN_LOGS="[FactionsPlus] ";
    //FIXME: use getDataFolder() and move these to onEnable() or onLoad() else will likely NPE if using getDataFolder()
	public static final File	folderBase= new File( "plugins" + File.separator + "FactionsPlus" );
	public static final File	folderWarps	= new File( folderBase, "warps" );
	public static final File	folderJails	= new File( folderBase, "jails" );
	public static final File	folderAnnouncements	= new File( folderBase, "announcements" );
	public static final File	folderFRules	= new File( folderBase, "frules" );
	public static final File	folderFBans	= new File( folderBase, "fbans" );
	public static final File	fileDisableInWarzone = new File( folderBase, "disabled_in_warzone.txt");
	
	private static final File currentFolder_OnClassInit=Utilities.getCurrentFolder();
	private static       File currentFolder_OnEnable=null;

    public static File templatesFile = new File(folderBase , "templates.yml");
	public static FileConfiguration templates;

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
		instance=this;
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
		info("Disabled.");
	}
	
	
	@Override
	public void onEnable() {
		Config.config=null;//must be here to cause config to reload on every plugin(s) reload from console
		ensureConfigFilesLocationSafety();
	    
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
		info("Factions version " + FactionsVersion + " - " + isOnePointSix);
		
		
		try {
			addDir(folderBase).addDir( folderWarps ).addDir( folderJails ).addDir( folderAnnouncements );
			addDir(folderFRules).addDir( folderFBans );
			
			if(!fileDisableInWarzone.exists()) {
				fileDisableInWarzone.createNewFile();
				info("Created file: "+fileDisableInWarzone);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw bailOut("something failed when ensuring the folders exist");
		}
		
		Config.config = getConfig();//load config
		
		if (!templatesFile.exists()) {
			
			FactionsPlusTemplates.createTemplatesFile();
		    
		} 
		
		templates = YamlConfiguration.loadConfiguration(templatesFile);	
		
		FactionsPlusCommandManager.setup();
		TeleportsListener.init(this);
		
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        
        
        if(Config.config.getBoolean(Config.confStr_enableEconomy)) {
        	RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        	
        	if (economyProvider != null) {
            	economy = economyProvider.getProvider();
        	}
        }
        if(Config.config.getBoolean(Config.confStr_enableAnnounce)) {
    		pm.registerEvents(this.announcelistener, this);
        }
        if(Config.config.getBoolean(Config.confStr_enableBans)) {
        	pm.registerEvents(this.banlistener, this);
        }
        if(Config.config.getBoolean(Config.confStr_enableJails)) {
        	pm.registerEvents(this.jaillistener, this);
        }
        if(Config.config.getBoolean(Config.confStr_enableDisguiseIntegration) && (Config.config.getBoolean(Config.confStr_unDisguiseIfInOwnTerritory) || Config.config.getBoolean(Config.confStr_unDisguiseIfInEnemyTerritory))) {
        	if(getServer().getPluginManager().isPluginEnabled("DisguiseCraft")) {
        		pm.registerEvents(this.dclistener, this);
        		info("Hooked into DisguiseCraft!");
        		isDisguiseCraftEnabled = true;
        		pm.registerEvents(this.disguiselistener, this);
        	}
        	if(getServer().getPluginManager().isPluginEnabled("MobDisguise")) {
        		pm.registerEvents(this.mdlistener, this);
        		info("Hooked into MobDisguise!");
        		isMobDisguiseEnabled = true;
        		pm.registerEvents(this.disguiselistener, this);
        	}
        	else {
        		info("MobDisguise or DisguiseCraft enabled, but no plugin found!");
        	}
        }
        if(1<2) {        //Temporary Always True Until a Config Option is Created 
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
        }
        

        
        LWCFunctions.try_integrateLWC();
        
		if ( LWCFunctions.isLWC() ) {
			if ( ( com.massivecraft.factions.Conf.lwcIntegration ) && ( com.massivecraft.factions.Conf.onCaptureResetLwcLocks ) ) {
				// if Faction plugin has setting to reset locks (which only resets for chests)
				// then have FactionPlus suggest its setting so that also locked furnaces/doors etc. will get reset
				if ( !Config.config.getBoolean( Config.confStr_removeLWCLocksOnClaim ) ) {
					// TODO: maybe someone can modify this message so that it would make sense to the console reader
					info( "Consider setting " + Config.confStr_removeLWCLocksOnClaim
						+ " to reset locks for more than just the chests" );
					// this also means in Factions having onCaptureResetLwcLocks to false would be good, if ours is on true
				}
				
			}
		}
	
       
        
        
        if(Config.config.getBoolean(Config.confStr_enablePeacefulBoosts)) {
        	pm.registerEvents(this.peacefullistener, this);
        }
        if(Config.config.getBoolean(Config.confStr_enablePowerBoosts)) {
        	pm.registerEvents(this.powerboostlistener, this);
        }

        
        
        version = getDescription().getVersion();
        
        FactionsPlusUpdate.checkUpdates();
        
		info("Ready.");
		
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
		    info("Waah! Couldn't metrics-up! :'(");
		}
		
	}

	@Override
	public FileConfiguration getConfig() {
		if (null == Config.config) {
			reloadConfig();
		}
		if (null == Config.config) {
			throw bailOut("reloading config failed somehow and this should not be reached");//bugged reloadConfig() if reached
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
			throw bailOut( "There is no '"+Config.fileConfigDefaults+"'(supposed to contain the defaults) inside the .jar\n"
				+ "which means that the plugin author forgot to include it" );
		}
		
		if ( Config.fileConfig.exists() ) {
			if (!Config.fileConfig.isFile()) {
				throw bailOut( "While '"+Config.fileConfig.getAbsolutePath()+"' exists, it is not a file!");
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
				throw bailOut( "failed to load existing config file '"+Config.fileConfig.getAbsolutePath()+"'");
			}
		}else {
			info(Config.fileConfig+" did not previously exist, creating a new config using defaults from the .jar");
		}
		
		saveConfig();
	}
	
	@Override
	public void saveConfig() {
		try {
			getConfig().save( Config.fileConfig );
		} catch ( IOException e ) {
			e.printStackTrace();
			throw bailOut("could not save config file: "+Config.fileConfig.getAbsolutePath());
		}
	}
	
	/**
	 * to be called only in onEnable();
	 */
	private void ensureConfigFilesLocationSafety() throws RuntimeException {
		// the following paranoid check is to make sure the configs don't get corrupted (so to speak) if the current folder
		// miraculously changed(ie. a different folder is now the current folder) by the time onEnable was called
		currentFolder_OnEnable = Utilities.getCurrentFolder();
		if ( ( null == currentFolder_OnEnable ) || ( null == currentFolder_OnClassInit )
			|| ( !currentFolder_OnEnable.equals( currentFolder_OnClassInit ) ) )
		{
			severe( "Bukkit(or something) changed current folder between the time the class was inited & "
				+ "the time onEnable() was issued.\nThis will mess up some thing as this was not expected, therefore we stop here" );
//			this.getServer().getPluginManager().disablePlugin( this );
			disableSelf(this);
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
//		log.info( FP_TAG_IN_LOGS+logInfoMsg );//log.info won't handle colors btw
		tellConsole(ChatColor.GOLD+FP_TAG_IN_LOGS+ChatColor.RESET+logInfoMsg );//they are logged with [INFO] level
	}
	
	public static void warn(String logInfoMsg) {
//		log.warn( FP_TAG_IN_LOGS+logInfoMsg );
		tellConsole(ChatColor.GOLD+FP_TAG_IN_LOGS+ChatColor.DARK_RED+"[WARNING]:"+ChatColor.RESET+logInfoMsg );//they are logged with [INFO] level
	}
	
	public static void severe(String logInfoMsg) {
		log.severe( FP_TAG_IN_LOGS+logInfoMsg );//allowed so that [SEVERE] appears
		tellConsole(ChatColor.RED+FP_TAG_IN_LOGS+ChatColor.DARK_PURPLE+logInfoMsg);
	}
	
	/**
	 * allows the use of ChatColor in messages but they will be prefixed by [INFO]
	 * @param msg
	 */
	public static void tellConsole(String msg) {
		//nvm; find another way to display colored msgs in console without having [INFO] prefix
		//there's no other way it's done via ColouredConsoleSender of craftbukkit
		//there are only two ways: colors+[INFO] prefix, or no colors + whichever prefix
		Bukkit.getConsoleSender().sendMessage( msg);//this will log with [INFO] level
	}
	
	/**
	 * calling this will not stop execution at the point where the call is made, but will mark the plugin as disabled<br>
	 * ie. shown in red when /plugins  is issued
	 * @param fpInstance
	 */
	public static void disableSelf(FactionsPlus fpInstance) {
		Bukkit.getPluginManager().disablePlugin( fpInstance );//it will call onDisable()
		//it won't deregister commands ie. /f fc  will still work
	}
	
	public static RuntimeException disableSelf(FactionsPlus fpInstance, boolean forceStop) {
		disableSelf(fpInstance);
		if (forceStop) {
			throw new RuntimeException(FP_TAG_IN_LOGS+" execution stopped by disableSelf()");
		}
		return null;
	}
	
	/**
	 * allowed to be used as: throw bailOut(..); for obvious reasons that it will throw but also so that eclipse won't 
	 * complain about NPEs because it doesn't see that it can "throw"
	 * @param fpInstance
	 * @param msg
	 * @return is a dummy return so that it can be used like: throw bailOut(...);
	 */
	public static RuntimeException bailOut(String msg) {
		severe(msg);
		throw disableSelf(instance, true);
	}
}
