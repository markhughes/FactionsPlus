package markehme.factionsplus;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import markehme.factionsplus.config.Config;
import markehme.factionsplus.extras.LWCBase;
import markehme.factionsplus.extras.LWCFunctions;
import markehme.factionsplus.extras.Metrics;
import markehme.factionsplus.extras.Metrics.Graph;
import markehme.factionsplus.listeners.CoreListener;
import markehme.factionsplus.listeners.FPConfigLoadedListener;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.MConf;

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
	
	// Which commands are disabled in warzone
	public static HashMap<String, String> commandsDisabledInWarzone 	= new HashMap<String, String>();
	
	// Server reference
	public static Server server;
		
	// If this is true, then there is an updated
	public static boolean update_avab;
	
	// Had to put this here, so that Updater can access it
	public static File thefile;
	
	// The plugin manager
	public static PluginManager pm;
	
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
			// Let the super start off
			super.onEnable(); 
			
			// Store some useful data for later
			thefile = getFile();
			server = getServer();
			pm = server.getPluginManager();	
			pluginVersion = getDescription().getVersion(); 
			
			// Init some Factions stuff (that doesn't uses the config)
			initFactions();
			
			// Init the config
			Config.init();
			
			// Publicise that the config has been loaded
			pm.registerEvents(new FPConfigLoadedListener(), this);
			
			// Reload the configuration 
			Config.reload(); 
			
			// Add our core listener
			pm.registerEvents(this.corelistener, this);
			
			// Setup the commands
			FactionsPlusCommandManager.setup();
			
			// Hook into vault for permissions
	        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration( net.milkbowl.vault.permission.Permission.class );
	        if(permissionProvider != null) {
	            permission = permissionProvider.getProvider();
	        }
	        
	        // Provide useful metrics information 
			try {
				
				metrics = new Metrics(this);
				
				// Also provide which version of Factions
                Graph factionsVersionGraph = metrics.createGraph("Factions Version");
    	        
                factionsVersionGraph.addPlotter(new Metrics.Plotter(FactionsVersion) {

                    @Override
                    public int getValue() {
                        return 1;
                    }
                });
                
				metrics.start();
				
			} catch (IOException e) {
				
				info("Metrics could not start up: "+e.getMessage());
				
			}
									
		} catch (Throwable t) {
			// Error management at its best..
			
			FactionsPlus.severe(t);
			
			if (isEnabled()) {
				disableSelf();
			}
		}
	}
	
	
	@Override
	public void onDisable() {
		Throwable failed = null; 
		
		try {
			try {
				if(EssentialsIntegration.isHooked()) {
					EssentialsIntegration.onDisable();
				}
			} catch ( Throwable t ) {
				failed = t;
				severe( t, "Exception on unhooking Essentials" );
			} 
			
			try {
				Config.deInit();
			} catch ( Throwable t ) {
				failed = t;
				severe( t, "Exception on disabling Config" );
			}
			
			try {
				FactionsPlusCommandManager.disableSubCommands();
			} catch(Throwable t) {
				failed = t;
				severe( t, "Exception on removing FactionsPlus commands" );
			}
			try {
				if ( LWCBase.isLWCPluginPresent() ) {
					LWCFunctions.unhookLWC();
				}
			} catch ( Throwable t ) {
				failed = t;
				severe( t, "Exception on unhooking LWC" );
			}
			
			update_avab = false; // reset this here
			
			try {
				FactionsPlusUpdate.ensureNotRunning();
			} catch ( Throwable t ) {
				failed = t;
				severe( t, "Exception on disabling Updates" );
			}
			
			try {
				getServer().getServicesManager().unregisterAll( this );
			} catch ( Throwable t ) {
				failed = t;
				severe( t, "Exception on unregistering services" );
			}
			
			try {
				HandlerList.unregisterAll( FactionsPlus.instance );
			} catch ( Throwable t ) {
				failed = t;
				severe( t, "Exception on unregistering from HandlerList" );
			}
			
			try {
				// This will deInit metrics, but it will be enabled again onEnable.
				getServer().getScheduler().cancelTasks( this );
			} catch ( Throwable t ) {
				failed = t;
				severe( t, "Exception when canceling schedule tasks" );
			}
			
			try {
				if(Bukkit.getScoreboardManager().getMainScoreboard().getObjective( FactionsPlusScoreboard.objective_name ) != null &&
						(Config._extras._scoreboards.showScoreboardOfFactions._ || Config._extras._scoreboards.showScoreboardOfMap._ )) {
					
					Bukkit.getScoreboardManager().getMainScoreboard().getObjective( FactionsPlusScoreboard.objective_name ).unregister();
				}
				
			} catch( Exception t ) {
				failed = t;
				severe( t, "Exception when removing scoreboard" );
			}
			
			//TODO: investigate why nag author happens ... even though we seem to be shuttind down task correctly
			//some tasks still remain from both FP and Vault at this point if doing a server `reload` as soon as you see "[FactionsPlus] Ready." 
//			List<BukkitWorker> workers = Bukkit.getScheduler().getActiveWorkers();
//			info("Active Workers: "+workers.size());
//			
//			for ( BukkitWorker bukkitWorker : workers ) {
//				info("  workerOwner: "+bukkitWorker.getOwner()+" taskId="+bukkitWorker.getTaskId()
//					+", "+bukkitWorker.getThread().getName());			
//			}
			
			if ( null == failed ) {
				info( "Disabled successfuly." );
			}
			
		} catch ( Throwable t ) {
			failed = t;
		} finally {
			if ( null != failed ) {
				info( "Did not disable successfuly! Please check over exceptions." );
				
			}
		}
	} // onDisable
	
	/**
	 * Used to initialise Factions-based stuff
	 */
	public void initFactions() {
		// Confirm this is running Factions 2.x - we don't want to cause any issues.
		
		try {
			Class.forName("com.massivecraft.factions.entity.MConf");
		} catch (ClassNotFoundException ex) {
			warn("Could not find Factions 2.x - please update to Factions 2.x.");
			info("You are required to use 0.5.x for Factions 1.x");
			disableSelf();
			return;
		}
		
		// Store the FactionsVersion
		FactionsVersion = pm.getPlugin( "Factions" ).getDescription().getVersion();
		
		// Some debug output - can be helpful when debugging errors 
		info("Factions v" + FactionsVersion ); 
		
		// Get world-specific settings from Factions 
		ignoredPvPWorlds			= 	MConf.get().worldsIgnorePvP;
		noClaimingWorlds 			= 	MConf.get().worldsNoClaiming;
		noPowerLossWorlds 			= 	MConf.get().worldsNoPowerLoss;

	}
}
