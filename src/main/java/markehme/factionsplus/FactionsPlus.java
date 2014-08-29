package markehme.factionsplus;

import java.util.HashMap;
import java.util.logging.Logger;

import markehme.factionsplus.MCore.Const;
import markehme.factionsplus.MCore.FactionData;
import markehme.factionsplus.MCore.FactionDataColl;
import markehme.factionsplus.MCore.FactionDataColls;
import markehme.factionsplus.MCore.LConfColl;
import markehme.factionsplus.MCore.MConf;
import markehme.factionsplus.MCore.MConfColl;
import markehme.factionsplus.MCore.FPUConfColls;
import markehme.factionsplus.config.OldMigrate;
import markehme.factionsplus.extras.LWCBase;
import markehme.factionsplus.extras.LWCFunctions;
import markehme.factionsplus.extras.Metrics;
import markehme.factionsplus.extras.Metrics.Graph;
import markehme.factionsplus.extras.WGFlagIntegration;
import markehme.factionsplus.listeners.*;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.massivecore.Aspect;
import com.massivecraft.massivecore.AspectColl;
import com.massivecraft.massivecore.Multiverse;
import com.onarandombox.MultiversePortals.MultiversePortals;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

/**
 * FactionsPlus has been designed to increase the power of Factions, by adding
 * features that I have thought would be helpful, and from suggestions others 
 * have made. This all started because I wanted warps in my Factions - it's come
 * pretty far since then.
 */
public class FactionsPlus extends FactionsPlusPlugin {
	
	public static FactionsPlus instance = null;
	
	public static PluginManager pm = null;
	
	public static Logger log = Logger.getLogger("Minecraft");
	
	public static Permission permission = null;
        
	public FactionsPlusListener factionsPlusListener = null;
		
	// Other Plugins
	public static Factions factions = null;
	public static MultiversePortals multiversePortalsPlugin = null;
	public static WorldGuardPlugin worldGuardPlugin = null;
	
	// Version information 
	public static String pluginVersion = "";
	public static String FactionsVersion = "";
	
	// Metrics - read dev.bukkit.org/bukkit-plugins/factionsplus for more information 
	private static Metrics metrics = null;
				
	// Had to put this here, so that Updater can access it
	
	// Aspect Stuff
	private Aspect aspect;
	public Aspect getAspect() { return this.aspect; }
	public Multiverse getMultiverse() { return this.getAspect().getMultiverse(); }
	
	public FactionsPlus() {
		super();
		
		if(null != instance) {
			throw bailOut(  "This was not expected, getting new-ed again without getting unloaded first.\n" +
							"Safest way to reload is to stop and start the server!");
		}
		
		// Store the instance
		instance = this;
	}
	
	@Override
	public void onEnable() {
		try {
			super.onEnable(); 
			
			// Store some useful data for later
			pm = getServer().getPluginManager();	
			pluginVersion = getDescription().getVersion(); 
			
			// Check Factions before pushing forward with MassiveCore
			checkFactions();
			
			// Setup MassiveCore related stuff 
			this.aspect = AspectColl.get().get(Const.ASPECT, true);
			this.aspect.register();
			this.aspect.setDesc(
				"<i>If the FactionsPlus system even is enabled and how it's configured.",
				"<i>What Factions exists and what players belong to them."
			);
			
			MConfColl.get().init();
			FPUConfColls.get().init();
			LConfColl.get().init();
			
			// Migration Check 
			OldMigrate om = new OldMigrate();
			if(om.shouldMigrate()) {
				info(ChatColor.GOLD + "Converting database and config, please wait ..");
				
				om.migrateDatabase();
			}
						
			registerAll();
			
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
					
					// Fetch the Factions Version
					metrics.createGraph("Factions Version").addPlotter(new Metrics.Plotter(FactionsVersion) {
	                    @Override
	                    public int getValue() { return 1; }
	                });
					
					// Get the Factions count  
					metrics.createGraph("Factions").addPlotter(new Metrics.Plotter(""+FactionColls.get().getColls().size()) {
	                    @Override
	                    public int getValue() { return 1; }
	                });
					
					// Get the amount of chunks claimed 
					metrics.createGraph("Chunks Claimed").addPlotter(new Metrics.Plotter(""+BoardColls.get().getColls().size()) {
	                    @Override
	                    public int getValue() { return 1; }
	                });
					
					int warpC = countWarps();
					
					if(warpC > 0) { 
					
						metrics.createGraph("Total Warps").addPlotter(new Metrics.Plotter(""+warpC) {
		                    @Override
		                    public int getValue() { return 1; }
		                });
	                
					}
					
					if(FactionsPlusListener.pluginFeaturesEnabled.size() > 0) {
						Graph pluginsUsed = metrics.createGraph("Plugins Hooked");
						
						for(String name : FactionsPlusListener.pluginFeaturesEnabled) {
							pluginsUsed.addPlotter(new Metrics.Plotter(name) {
			                    @Override
			                    public int getValue() { return 1; }
			                });
						}
					}
					
					metrics.start();
					
				} catch (Exception e) {
					info("Metrics could not start up "+e.getMessage());					
				}
	        }
									
		} catch (Throwable t) {
			// Error management at its best..
			
			FactionsPlus.severe(t);
			
			if (isEnabled()) disableSelf();
		}
	}
	
	
	/**
	 * Plugin disable, we ensure everything that FactionsPlus is
	 * doing or using is turned off.
	 */
	@Override
	public void onDisable() {
		HashMap<Throwable, String> errors = new HashMap<Throwable, String>();
		
		try {
			if(EssentialsIntegration.isHooked()) {
				EssentialsIntegration.onDisable();
			}
		} catch (Throwable t) {
			errors.put(t, "Exception on unhooking Essentials.");
		} 
		
		try {
			FactionsPlusCommandManager.disableSubCommands();
		} catch(Throwable t) {
			errors.put(t, "Exception on removing FactionsPlus commands.");
		}
		
		try {
			if (LWCBase.isLWCPluginPresent()) {
				LWCFunctions.unhookLWC();
			}
		} catch (Throwable t) {
			errors.put(t, "Exception on unhooking LWC.");
		}
			
		try {
			FactionsPlusUpdate.ensureNotRunning();
		} catch (Throwable t) {
			errors.put(t, "Exception on disabling Updates.");
		}
		
		try {
			getServer().getServicesManager().unregisterAll(this);
		} catch (Throwable t) {
			errors.put(t, "Exception on unregistering services.");
		}
		
		try {
			HandlerList.unregisterAll(FactionsPlus.instance);
		} catch (Throwable t) {
			errors.put(t, "Exception on unregistering from HandlerList.");
		}
		
		try {
			getServer().getScheduler().cancelTasks(this);
		} catch (Throwable t) {
			errors.put(t, "Exception when canceling schedule tasks.");
		}
			
		try {
			if(FactionsPlusScoreboard.scoreBoard != null) {
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(p.getScoreboard() == FactionsPlusScoreboard.scoreBoard) {
						p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
					}
				}
				
				FactionsPlusScoreboard.scoreBoard = null;
			}

		} catch(Exception t) {
			errors.put(t, "Exception when removing scoreboard.");
		}
		
		if(errors.size() == 0) {
			info("Disabled successfuly.");
		} else {
			for(Throwable error : errors.keySet()) {
				warn(errors.get(error));
				error.printStackTrace();
				warn(" ");
			}
			
			warn("Did not disable successfuly! Please check above errors.");
		}
		
		errors.clear();
	}
	
	/**
	 * Used to initialise Factions-based stuff
	 */
	public void checkFactions() {
		// Confirm this is running Factions 2.x - we don't want to cause any issues.
		// This is why Factions is soft-depended, we can make nicer errors this way.
		try {
			Class.forName("com.massivecraft.factions.entity.MConf");
		} catch (ClassNotFoundException ex) {
			warn("Could not find Factions 2.5.0+ - please update to Factions 2.5.0+");
			warn("(or not compaitable - check for updates)");
			disableSelf();
			return;
		}
		
		// Store the FactionsVersion
		FactionsVersion = pm.getPlugin("Factions").getDescription().getVersion();
		
		// Some debug output - can be helpful when debugging errors 
		debug("Factions v" + FactionsVersion ); 

	}
	
	public void registerAll() {
		
		if(Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
			if(worldGuardPlugin == null) worldGuardPlugin = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
		}
		
		// Register our listener
		factionsPlusListener = new FactionsPlusListener();
		pm.registerEvents(factionsPlusListener, this);
		
		// Register our permanent listeners
		factionsPlusListener.setupPermanentListeners(this);
		
		// TODO: move this into a sub listener
        TeleportsListener.initOrDeInit(this);
        
        // WorldGuard Custom Flags integration
        if(pm.isPluginEnabled("WGCustomFlags")) {
        	WGFlagIntegration WGFi = new WGFlagIntegration();
        	WGFi.addFlags();
        }
        
		// Scoreboard Setup
		FactionsPlusScoreboard.setup();
		
		FactionsPlusUpdate.enableOrDisableCheckingForUpdates();

	}
	
	public int countWarps() {
		int total = 0;
		
		for(FactionDataColl fColl : FactionDataColls.get().getColls()) {
			for(String id : fColl.getIds()) {
				FactionData fData = fColl.get(id);
				
				if(fData.warpLocation.size() > 0) {
					total = total + fData.warpLocation.size();
				}
			}
		}
		
		return total;
	}
}
