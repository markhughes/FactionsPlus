package markehme.factionsplus;

import java.io.*;
import java.util.logging.Logger;

import markehme.factionsplus.FactionsBridge.*;
import markehme.factionsplus.config.*;
import markehme.factionsplus.extras.*;
import markehme.factionsplus.listeners.*;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.*;
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
		
		FactionsPlusUpdate.ensureNotRunning();
		
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
		Bridge.init();
		PluginManager pm = this.getServer().getPluginManager();
		FactionsVersion = (pm.getPlugin("Factions").getDescription().getVersion());
		FactionsPlusPlugin.info("Factions version " + FactionsVersion );//before reload
		pm.registerEvents(new FPConfigLoadedListener(),this);
		Config.reload();//be as soon as possible but after the above
		
		pm.registerEvents(this.corelistener, this);
		
		FactionsPlusJail.server = getServer();
		
	
		
		
		FactionsPlusCommandManager.setup();
		TeleportsListener.init(this);
		
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
        

		if ( LWCBase.isLWC() ) {// LWCFunctions.isLWC() also works here though
			
			LWCFunctions.hookLWC();// this must be inside an if, else NoClassDefFoundError if LWC is not on
			
			if ( ( com.massivecraft.factions.Conf.lwcIntegration ) && ( com.massivecraft.factions.Conf.onCaptureResetLwcLocks ) ) {
				// if Faction plugin has setting to reset locks (which only resets for chests)
				// then have FactionPlus suggest its setting so that also locked furnaces/doors etc. will get reset
				if ( !Config._extras._protection._lwc.removeAllLocksOnClaim._ ) {
					// TODO: maybe someone can modify this message so that it would make sense to the console reader
					FactionsPlusPlugin.info( "Consider setting `" + Config._extras._protection._lwc.removeAllLocksOnClaim._dottedName_asString
						+ "` to reset locks(on land claim) for more than just chests," +
						" which is what Factions plugin already does right now" );
					// this also means in Factions having onCaptureResetLwcLocks to false would be good, if ours is on true
				}
				
			}
			
		} else {//no LWC
			if ( Config._extras._protection._lwc.blockCPublicAccessOnNonOwnFactionTerritory._ 
				|| Config._extras._protection._lwc.removeAllLocksOnClaim._ ) 
			{
				FactionsPlusPlugin
					.warn( "LWC plugin was not found(or not enabled yet) but a few settings that require LWC are Enabled!"
						+ " This means those settings will be ignored & have no effect" );
			}
			return;
		}
        
	
       
        
        
        if(Config._peaceful.enablePeacefulBoosts._) {
        	pm.registerEvents(this.peacefullistener, this);
        }
        if(Config._powerboosts.enabled._) {
        	pm.registerEvents(this.powerboostlistener, this);
        }

        
        
        version = getDescription().getVersion();
        
        FactionsPlusUpdate.checkUpdates(this);
        
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
