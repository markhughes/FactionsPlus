package markehme.factionsplus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import markehme.factionsplus.MCore.Const;
import markehme.factionsplus.MCore.LConfColl;
import markehme.factionsplus.MCore.MConf;
import markehme.factionsplus.MCore.MConfColl;
import markehme.factionsplus.MCore.FPUConfColls;
import markehme.factionsplus.config.OldMigrate;
import markehme.factionsplus.extras.LWCBase;
import markehme.factionsplus.extras.LWCFunctions;
import markehme.factionsplus.extras.Metrics;
import markehme.factionsplus.listeners.CoreListener;
import markehme.factionsplus.listeners.FPConfigLoadedListener;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.massivecraft.factions.Factions;
import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.AspectColl;
import com.massivecraft.massivecore.Multiverse;

import com.onarandombox.MultiversePortals.MultiversePortals;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

/**
 * FactionsPlus has been designed to increase the power of Factions, by adding
 * features that I have thought would be helpful, and from suggestions others 
 * have made. This all started because I wanted warps in my Factions - it's come
 * pretty far since then.
 */
public class FactionsPlus extends FactionsPlusPlugin {

	// Our instance
	public static FactionsPlus instance;
	
	// The logger
	public static Logger log 									= 	Logger.getLogger("Minecraft");
	
	// Factions instance
	Factions factions;
	
	// Used by vault 
    public static Permission permission 						= 	null;
    
    public static List<UUID> whosIgnoringNeeds 					= 	new ArrayList<UUID>();
    
    // See which plugins are enabled  
	public static boolean isWorldEditEnabled 					= 	false;
	public static boolean isWorldGuardEnabled 					= 	false;
	public static boolean isMultiversePortalsEnabled 			= 	false;
	
	// Our core listener
	public final CoreListener corelistener 						=	new CoreListener();
	
	// WorldEdit + World Guard plugins
	public static WorldEditPlugin worldEditPlugin 				= 	null;
	public static WorldGuardPlugin worldGuardPlugin 			= 	null;
	
	// MultiversePortals plugin
	public static MultiversePortals multiversePortalsPlugin 	= 	null;
	
	// Version information 
	public static String pluginVersion;
	public static String FactionsVersion;
	
	// Metrics - read dev.bukkit.org/bukkit-plugins/factionsplus for more information 
	private static Metrics metrics 								= 	null;
	
	// Factions-specific world information 
	public static Set<String> ignoredPvPWorlds 					= 	null;
	public static Set<String> noClaimingWorlds 					= 	null;
	public static Set<String> noPowerLossWorlds 				= 	null;
		
	// Server reference
	public static Server server;
	
	// Had to put this here, so that Updater can access it
	public static File thefile;
	
	// The plugin manager
	public static PluginManager pm;
	
	// Aspect Stuff
	private Aspect aspect;
	public Aspect getAspect() { return this.aspect; }
	public Multiverse getMultiverse() { return this.getAspect().getMultiverse(); }
	
	public FactionsPlus() {
		super();
		
		//  instance was not null, which means we wern't disabled properly - bail!
		if(null != instance) {
			throw bailOut("This was not expected, getting new-ed again without getting unloaded first.\n" +
							"Safest way to reload is to stop and start the server!");
		}
		
		// Store the instance
		instance = this;
	}
	
	@Override
	public void onEnable() {
		try {
			super.onEnable(); 
			
			// MCore is a go!
			
			this.aspect = AspectColl.get().get(Const.ASPECT, true);
			this.aspect.register();
			this.aspect.setDesc(
				"<i>If the FactionsPlus system even is enabled and how it's configured.",
				"<i>What Factions exists and what players belong to them."
			);
			
			// Init MStore 
			MConfColl.get().init();
			FPUConfColls.get().init();
			LConfColl.get().init();
			
			// Store some useful data for later
			thefile = getFile();
			server = getServer();
			pm = server.getPluginManager();	
			pluginVersion = getDescription().getVersion(); 
			
			OldMigrate om = new OldMigrate();
			
			if(om.shouldMigrate()) {
				info(ChatColor.GOLD + "Converting database and config, please wait ..");
				
				om.migrateDatabase();
			}
			
			initFactions();
			
			// Publicise that the config has been loaded
			pm.registerEvents(new FPConfigLoadedListener(), this);
			
			// Add our core listener
			pm.registerEvents(this.corelistener, this);
			
			// Setup the commands
			FactionsPlusCommandManager.setup();
			
			// Let's test for Vault 
			try {
				Class.forName("net.milkbowl.vault.permission.Permission");
			} catch (ClassNotFoundException ex) {
				warn("Could not find Vault - please setup vault!");
				info("Download Vault here: http://dev.bukkit.org/bukkit-plugins/vault/ ");
				
				disableSelf();
				return;
			}
			
			// Hook into vault for permissions
			RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration( net.milkbowl.vault.permission.Permission.class );
	        if(permissionProvider != null) {
	            permission = permissionProvider.getProvider();
	        }
	        
	        // Provide useful metrics information 
	        if(MConf.get().metrics.booleanValue()) {
				try {
					metrics = new Metrics(this);
	                metrics.createGraph("Factions Version").addPlotter(new Metrics.Plotter(FactionsVersion) {
	                    @Override
	                    public int getValue() { return 1; }
	                });
					metrics.start();
					
				} catch (Exception e) {
					info("Metrics could not start up "+e.getMessage());					
				}
	        }
									
		} catch (Throwable t) {
			// Error management at its best..
			
			FactionsPlus.severe(t);
			
			if (isEnabled()) {
				disableSelf();
			}
		}
	}
	
	
	/**
	 * Plugin disable, we ensure everything that FactionsPlus is
	 * doing or using is turned off.
	 */
	@Override
	public void onDisable() {
		Throwable failed = null; 
		
		try {
			try {
				if(EssentialsIntegration.isHooked()) {
					EssentialsIntegration.onDisable();
				}
			} catch (Throwable t) {
				failed = t;
				severe(t, "Exception on unhooking Essentials");
			} 
			
			try {
				FactionsPlusCommandManager.disableSubCommands();
			} catch(Throwable t) {
				failed = t;
				severe(t, "Exception on removing FactionsPlus commands");
			}
			try {
				if (LWCBase.isLWCPluginPresent()) {
					LWCFunctions.unhookLWC();
				}
			} catch (Throwable t) {
				failed = t;
				severe(t, "Exception on unhooking LWC");
			}
					
			try {
				FactionsPlusUpdate.ensureNotRunning();
			} catch ( Throwable t ) {
				failed = t;
				severe(t, "Exception on disabling Updates");
			}
			
			try {
				getServer().getServicesManager().unregisterAll(this);
			} catch ( Throwable t ) {
				failed = t;
				severe(t, "Exception on unregistering services");
			}
			
			try {
				HandlerList.unregisterAll(FactionsPlus.instance);
			} catch ( Throwable t ) {
				failed = t;
				severe(t, "Exception on unregistering from HandlerList");
			}
			
			try {
				getServer().getScheduler().cancelTasks(this);
			} catch ( Throwable t ) {
				failed = t;
				severe(t, "Exception when canceling schedule tasks");
			}
			
			try {
				if(Bukkit.getScoreboardManager().getMainScoreboard().getObjective(FactionsPlusScoreboard.objective_name) != null) {
					Bukkit.getScoreboardManager().getMainScoreboard().getObjective(FactionsPlusScoreboard.objective_name).unregister();
				}
			} catch( Exception t ) {
				failed = t;
				severe(t, "Exception when removing scoreboard");
			}
			
			if(null == failed) {
				info("Disabled successfuly.");
			}
			
		} catch (Throwable t) {
			failed = t;
		} finally {
			if ( null != failed ) {
				info("Did not disable successfuly! Please check over exceptions.");
			}
		}
	}
	
	/**
	 * Used to initialise Factions-based stuff
	 */
	public void initFactions() {
		// Confirm this is running Factions 2.x - we don't want to cause any issues.
		
		try {
			Class.forName("com.massivecraft.factions.entity.MConf");
		} catch (ClassNotFoundException ex) {
			warn("Could not find Factions 2.x - please update to Factions 2.x.");
			warn("(or not compaitable)");
			
			disableSelf();
			return;
		}
		
		// Store the FactionsVersion
		FactionsVersion = pm.getPlugin("Factions").getDescription().getVersion();
		
		// Some debug output - can be helpful when debugging errors 
		debug("Factions v" + FactionsVersion ); 

	}
}
