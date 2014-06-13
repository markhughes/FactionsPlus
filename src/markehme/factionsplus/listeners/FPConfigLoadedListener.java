package markehme.factionsplus.listeners;


import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusScoreboard;
import markehme.factionsplus.FactionsPlusUpdate;
import markehme.factionsplus.events.FPConfigLoadedEvent;
import markehme.factionsplus.extras.LWCBase;
import markehme.factionsplus.extras.LWCFunctions;
import markehme.factionsplus.extras.LocketteFunctions;
import markehme.factionsplus.extras.WGFlagIntegration;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.onarandombox.MultiversePortals.MultiversePortals;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class FPConfigLoadedListener implements Listener {
	
	/**
	 * called after the config is loaded, which is typically when plugin is enabled
	 */
	@EventHandler
	public void onConfigLoaded(FPConfigLoadedEvent event) {
        TeleportsListener.initOrDeInit(FactionsPlus.instance);
       
        PluginManager pm = Bukkit.getServer().getPluginManager();
        
        // Fetch world edit 
        if(pm.isPluginEnabled("WorldEdit")) {
       		FactionsPlus.worldEditPlugin = (WorldEditPlugin) pm.getPlugin("WorldEdit");
       		FactionsPlus.isWorldEditEnabled = true;
       	}
        
        // fetch world guard
        if(pm.isPluginEnabled("WorldGuard")) {
        	FactionsPlus.worldGuardPlugin = (WorldGuardPlugin) pm.getPlugin("WorldGuard");	            	
        	FactionsPlus.isWorldGuardEnabled = true;
        }
        
        // fetch mvp
        if(pm.isPluginEnabled("Multiverse-Portals")) { 
        	Plugin MVc = pm.getPlugin("Multiverse-Portals");
            
            if(MVc instanceof MultiversePortals) {
            	FactionsPlus.multiversePortalsPlugin = (MultiversePortals) MVc;
            	
            	FactionsPlus.isMultiversePortalsEnabled = true;
            }
            
        }
        
        // WorldGuard Custom Flags integration
        if(pm.isPluginEnabled("WGCustomFlags")) {
        	WGFlagIntegration WGFi = new WGFlagIntegration();
        	WGFi.addFlags();
        }
        
        if ( LWCBase.isLWCPluginPresent() ) {
        	
			// TODO: is this still used ? - can't find in Factions config 
        	/*
			if ( ( com.massivecraft.factions.Conf.lwcIntegration ) && ( com.massivecraft.factions.Conf.onCaptureResetLwcLocks ) ) {
				// if Faction plugin has setting to reset locks (which only resets for chests)
				// then have FactionPlus suggest its setting so that also locked furnaces/doors etc. will get reset
				if ( !Config._extras._protection._lwc.removeAllLocksOnClaim._ ) {
					Config._extras._protection._lwc.removeAllLocksOnClaim._=true;
					// meh: maybe someone can modify this message so that it would make sense to the console reader
					FactionsPlusPlugin.info( "Automatically setting `" + Config._extras._protection._lwc.removeAllLocksOnClaim._dottedName_asString
						+ "` for this session because you have Factions.`onCaptureResetLwcLocks` set to true" );
					// this also means in Factions having onCaptureResetLwcLocks to false would be good, if ours is on true
				}
				
			}
			*/

			try { 
				LWCFunctions.hookLWCIfNeeded();
			} catch(NoClassDefFoundError e) {
				FactionsPlus.debug( "Couldn't hook LWC.. ignoring." );
			}
			
		} else { //no LWC
			/*
			if ( OldConfig._extras._protection._lwc.blockCPublicAccessOnNonOwnFactionTerritory._ 
				|| OldConfig._extras._protection._lwc.removeAllLocksOnClaim._ ) 
			{
				FPP
					.warn( "LWC plugin was not found(or not enabled yet) but a few settings that require LWC are Enabled!"
						+ " This means those settings will be ignored & have no effect" );
			}
			*/
			
			//if there is no LWC anymore ie. plugman unload lwc
			//then we might still have hooks into LWC from before, and we kinda take care of unlinking those here
			LWCFunctions.deregListenerIfNeeded();
		}
        
        // Lockette 
        LocketteFunctions.enableOrDisable(FactionsPlus.instance);
        
        // Disguises 
        DisguiseListener.enableOrDisable(FactionsPlus.instance);
        
        // Multiverse-portals
        MVPListener.enableOrDisable(FactionsPlus.instance);
        
        // CreativeGates
        CreativeGatesListener.enableOrDisable(FactionsPlus.instance);
        
        // Cannons
        CannonsListener.enableOrDisable(FactionsPlus.instance);
        
		// TODO: look through usage UConf and see if these should be enabled (inc. ChestShop, ShowCaseStandalone
		Listen.startOrStopListenerAsNeeded( true, PowerboostListener.class );
		Listen.startOrStopListenerAsNeeded( true, AnnounceListener.class );
		Listen.startOrStopListenerAsNeeded( true, BanListener.class );
		Listen.startOrStopListenerAsNeeded( true, JailListener.class );
		Listen.startOrStopListenerAsNeeded( true, PeacefulListener.class );
		Listen.startOrStopListenerAsNeeded( true, LiquidFlowListener.class );
		Listen.startOrStopListenerAsNeeded( true, DenyClaimListener.class );
		
		// ChestShop
		if( Bukkit.getPluginManager().getPlugin( "ChestShop" ) != null ) {
			
			Listen.startOrStopListenerAsNeeded( true, ChestShopListener.class );
			
		}
		
		// ChestShop
		if( Bukkit.getPluginManager().getPlugin( "ShowCaseStandalone" ) != null ) {
			
			Listen.startOrStopListenerAsNeeded( true, ShowCaseStandaloneListener.class );
			
		}
		

		// Animal Damager Listener
		Listen.startOrStopListenerAsNeeded( true, AnimalDamageListener.class );
		
		// Scoreboard Setup
		FactionsPlusScoreboard.setup();
		
		// Updates
		FactionsPlusUpdate.enableOrDisableCheckingForUpdates();
		
	}
}