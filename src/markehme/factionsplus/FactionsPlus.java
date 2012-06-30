package markehme.factionsplus;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

import markehme.factionsplus.FactionsBridge.*;
import markehme.factionsplus.config.*;
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

	public static FactionsPlus instance;//should never be null, unless not yet loaded by bukkit
	
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
	
	private static Metrics metrics=null;

	
	public FactionsPlus() {//constructor
		super();
		if (null != instance) {
			throw bailOut("this was not expected, getting new-ed again");
		}
		instance=this;
	}
	
	@Override
	public void onDisable() {
		try {
		if (null != metrics) {
			try {
				metrics.disable();
			} catch ( IOException e ) {
				e.printStackTrace();
			}
		}
		
		//TODO: unhook Factions registered commands on disabling self else they'll still call our code and possibly NPE 
		//since we deinited some of our parts
		
		if (LWCBase.isLWC()) {
			LWCFunctions.unhookLWC();
		}
		
		getServer().getServicesManager().unregisterAll(this);//not really needed at this point, only for when using .register(..)
		FactionsPlusPlugin.info("Disabled successfuly.");
		}catch(Throwable t) {
			FactionsPlusPlugin.info("unable to successfully disable"); 
			FactionsPlus.severe(t);
		}
	}
	
	
	@Override
	public void onEnable() { try { super.onEnable();//be first
		Config.init();
		Config.reload();//be as soon as possible
	    
		PluginManager pm = this.getServer().getPluginManager();
		
		pm.registerEvents(this.corelistener, this);
		
		FactionsPlusJail.server = getServer();
		
		Bridge.init();
		FactionsVersion = (pm.getPlugin("Factions").getDescription().getVersion());
		FactionsPlusPlugin.info("Factions version " + FactionsVersion );
		
		
		FactionsPlusCommandManager.setup();
		TeleportsListener.init(this);
		
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        
        
        if(Config.economy.enabled) {
        	RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        	
        	if (economyProvider != null) {
            	economy = economyProvider.getProvider();
        	}
        }
        if(Config.announce.enabled){
    		pm.registerEvents(this.announcelistener, this);
        }
        if(Config.banning.enabled) {
        	pm.registerEvents(this.banlistener, this);
        }
        if(Config.jails.enabled) {
        	pm.registerEvents(this.jaillistener, this);
        }
        if(Config.extras.disguise.enableDisguiseIntegration && (Config.extras.disguise.unDisguiseIfInOwnTerritory || Config.extras.disguise.unDisguiseIfInEnemyTerritory)) {
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
        

		if ( LWCBase.isLWC() ) {// LWCFunctions.isLWC() also works here though
			
			LWCFunctions.hookLWC();// this must be inside an if, else NoClassDefFoundError if LWC is not on
			
			if ( ( com.massivecraft.factions.Conf.lwcIntegration ) && ( com.massivecraft.factions.Conf.onCaptureResetLwcLocks ) ) {
				// if Faction plugin has setting to reset locks (which only resets for chests)
				// then have FactionPlus suggest its setting so that also locked furnaces/doors etc. will get reset
				if ( !Config.extras.lwc.removeLWCLocksOnClaim ) {
					// TODO: maybe someone can modify this message so that it would make sense to the console reader
					FactionsPlusPlugin.info( "Consider setting `" + Config.str_removeLWCLocksOnClaim
						+ "` to reset locks(on land claim) for more than just chests," +
						" which is what Factions plugin already does right now" );
					// this also means in Factions having onCaptureResetLwcLocks to false would be good, if ours is on true
				}
				
			}
			
		} else {//no LWC
			if ( Config.extras.lwc.blockCPublicAccessOnNonOwnFactionTerritory 
				|| Config.extras.lwc.removeLWCLocksOnClaim ) 
			{
				FactionsPlusPlugin
					.warn( "LWC plugin was not found(or not enabled yet) but a few settings that require LWC are Enabled!"
						+ " This means those settings will be ignored & have no effect" );
			}
			return;
		}
        
	
       
        
        
        if(Config.peaceful.enablePeacefulBoosts) {
        	pm.registerEvents(this.peacefullistener, this);
        }
        if(Config.powerboosts.enabled) {
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
