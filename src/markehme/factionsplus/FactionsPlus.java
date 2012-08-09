package markehme.factionsplus;

import java.io.*;
import java.util.Set;
import java.util.logging.Logger;

import markehme.factionsplus.FactionsBridge.*;
import markehme.factionsplus.config.*;
import markehme.factionsplus.extras.*;
import markehme.factionsplus.listeners.*;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.event.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.massivecraft.factions.*;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class FactionsPlus extends FactionsPlusPlugin {

	public static FactionsPlus instance;//should never be null, unless not yet loaded by bukkit
	
	public static Logger log = Logger.getLogger("Minecraft");
	
	Factions factions;
	FPlayers fplayers;
	Faction faction;
	 
    public static Permission permission = null;
    
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
	public final LiquidFlowListener liquidflowlistener = new LiquidFlowListener();
	
	public final DCListener dclistener = new DCListener();
	public final MDListener mdlistener = new MDListener();

	

	public static WorldEditPlugin worldEditPlugin = null;
	public static WorldGuardPlugin worldGuardPlugin = null;
	
	public static String version;
	public static String FactionsVersion;
	
	private static Metrics metrics=null;
	
	// XXX: if Factions gets reloaded (ie. via plugman) and it has different settings for these now, they will have new
	// instances but we're still gonna be pointing to the old ones because of this
	public static Set<String> ignoredPvPWorlds = com.massivecraft.factions.Conf.worldsIgnorePvP;
	public static Set<String> noClaimingWorlds = com.massivecraft.factions.Conf.worldsNoClaiming;
	public static Set<String> noPowerLossWorlds = com.massivecraft.factions.Conf.worldsNoPowerLoss;
	
	public FactionsPlus() {//constructor
		super();
		if (null != instance) {
			throw bailOut("this was not expected, getting new-ed again without getting unloaded first.\n"
				+"Safest way to reload is to stop and start the server!");
		}
		instance=this;
	}
	
	
	@Override
	public void onDisable() {
		Throwable failed = null;// TODO: find a way to chain all thrown exception rather than overwrite all older
		try {
			
			try {
				EssentialsIntegration.onDisable();
			} catch ( Throwable t ) {
				failed = t;
			}
			
			try {
				Config.deInit();
			} catch ( Throwable t ) {
				failed = t;
			}
			
			// TODO: unhook Factions registered commands on disabling self else they'll still call our code and possibly NPE
			// since we deinited some of our parts
			
			try {
				if ( LWCBase.isLWC() ) {
					LWCFunctions.unhookLWC();
				}
			} catch ( Throwable t ) {
				failed = t;
			}
			
			try {
				FactionsPlusUpdate.ensureNotRunning();
			} catch ( Throwable t ) {
				failed = t;
			}
			
			try {
				getServer().getServicesManager().unregisterAll( this );// not really needed at this point, only for when using
																		// .register(..)
			} catch ( Throwable t ) {
				failed = t;
			}
			
			try {
				HandlerList.unregisterAll( FactionsPlus.instance );
			} catch ( Throwable t ) {
				failed = t;
			}
			
			try {
				//this will deInit metrics, but it will be enabled again onEnable
				getServer().getScheduler().cancelTasks( this);
			} catch ( Throwable t ) {
				failed = t;
			}
			
			if (null == failed) {
				FactionsPlusPlugin.info( "Disabled successfuly." );
			}
			
		} catch ( Throwable t ) {
			failed = t;
		} finally {
			if ( null != failed ) {
				FactionsPlusPlugin.info( "unable to successfully disable" );
				FactionsPlus.severe( failed, "This is the last seen exception:" );
			}
		}
	}
	
	
	@Override
	public void onEnable() { try { super.onEnable();//be first
		Config.init();
		Bridge.init();
		PluginManager pm = this.getServer().getPluginManager();
		FactionsVersion = (pm.getPlugin("Factions").getDescription().getVersion());
		FactionsPlusPlugin.info("Factions version " + FactionsVersion );//before reload
		pm.registerEvents(new FPConfigLoadedListener(),this);
		Config.reload();//be as soon as possible but after the above
		
		pm.registerEvents(this.corelistener, this);
		
		FactionsPlusJail.server = getServer();
		
	
		
		
		FactionsPlusCommandManager.setup();
		
		
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        
        
        if(Config._announce.enabled._){
    		pm.registerEvents(this.announcelistener, this);
        }
        if(Config._banning.enabled._) {
        	pm.registerEvents(this.banlistener, this);
        }
        if(Config._jails.enabled._) {
        	pm.registerEvents(this.jaillistener, this);
        }
        if(Config._extras._disguise.enableDisguiseIntegration._ && (Config._extras._disguise.unDisguiseIfInOwnTerritory._ || Config._extras._disguise.unDisguiseIfInEnemyTerritory._)) {
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
        

        
        
        if(Config._peaceful.enablePeacefulBoosts._) {
        	pm.registerEvents(this.peacefullistener, this);
        }
        if(Config._powerboosts.enabled._) {
        	pm.registerEvents(this.powerboostlistener, this);
        }
        if(Config._extras.crossBorderLiquidFlowBlock._) {
        	pm.registerEvents(this.liquidflowlistener, this);
        }

        
        
        version = getDescription().getVersion();
        
        
        
		FactionsPlusPlugin.info("Ready.");
		
			try {
				metrics = new Metrics( this );
				metrics.start();
			} catch ( IOException e ) {
				FactionsPlusPlugin.info("Waah! Couldn't metrics-up! :'( "+e.getMessage() );
			}

		
		
		//put your code above, let this be last:
	}catch (Throwable t) {
		FactionsPlus.severe( t);
//		t.printStackTrace();//fixed: this makes each line have [SEVERE] which is unlike what happens when you just allow it to throw
		if (isEnabled()) {
			disableSelf();
		}
	}
	}//onEnable
	
	
}
