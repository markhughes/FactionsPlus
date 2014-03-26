package markehme.factionsplus.listeners;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusScoreboard;
import markehme.factionsplus.FactionsPlusUpdate;
import markehme.factionsplus.config.Config;
import markehme.factionsplus.events.FPConfigLoadedEvent;
import markehme.factionsplus.extras.LWCBase;
import markehme.factionsplus.extras.LWCFunctions;
import markehme.factionsplus.extras.LocketteFunctions;
import markehme.factionsplus.extras.Updater;
import markehme.factionsplus.references.FP;
import markehme.factionsplus.references.FPP;

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
	 * called after the config is (re)loaded, which is typically when plugin is enabled and when /f reloadfp  is issued<br>
	 */
	@EventHandler
	public void onConfigLoaded( FPConfigLoadedEvent event ) {
        Config._economy.enableOrDisableEconomy();
        //TODO: add more here and make sure they can change states between on/off just like they would by a server 'reload' command
        //because this hook is called every time the config is reloaded, which means some things could have been previously enabled
        //and now the config may dictate that they are disabled (state changed) so we must properly handle that behaviour.
        TeleportsListener.initOrDeInit(FP.instance);
       
        PluginManager pm = Bukkit.getServer().getPluginManager();
        
        
        if( pm.isPluginEnabled( "WorldEdit" ) ) {
       		FactionsPlus.worldEditPlugin = (WorldEditPlugin) pm.getPlugin( "WorldEdit" );
       		FactionsPlus.isWorldEditEnabled = true;
       	}
        
        if( pm.isPluginEnabled( "WorldGuard" ) ) {
        	FactionsPlus.worldGuardPlugin = ( WorldGuardPlugin ) pm.getPlugin( "WorldGuard" );	            	
        	FactionsPlus.isWorldGuardEnabled = true;
        }
        
        if( pm.isPluginEnabled( "Multiverse-Portals" ) ) { 
        	Plugin MVc = pm.getPlugin( "Multiverse-Portals" );
            
            if (MVc instanceof MultiversePortals) {
            	FactionsPlus.multiversePortalsPlugin = ( MultiversePortals ) MVc;
            	
            	FactionsPlus.isMultiversePortalsEnabled = true;
            }
            
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
				FPP.info( "Couldn't hook LWC.. ignoring." );
			}
			
		} else { //no LWC
			if ( Config._extras._protection._lwc.blockCPublicAccessOnNonOwnFactionTerritory._ 
				|| Config._extras._protection._lwc.removeAllLocksOnClaim._ ) 
			{
				FPP
					.warn( "LWC plugin was not found(or not enabled yet) but a few settings that require LWC are Enabled!"
						+ " This means those settings will be ignored & have no effect" );
			}
			
			//if there is no LWC anymore ie. plugman unload lwc
			//then we might still have hooks into LWC from before, and we kinda take care of unlinking those here
			LWCFunctions.deregListenerIfNeeded();
		}
        
        // Lockette 
        LocketteFunctions.enableOrDisable(FP.instance);
        
        // Disguises 
        DisguiseListener.enableOrDisable(FP.instance);
        
        // Multiverse-portals
        MVPListener.enableOrDisable(FP.instance);
        
        // CreativeGates
        CreativeGatesListener.enableOrDisable(FP.instance);
        
        // Cannons
        CannonsListener.enableOrDisable(FP.instance);
        
		// PowerboostListener.startOrStopPowerBoostsListenerAsNeeded();
		Listen.startOrStopListenerAsNeeded( Config._powerboosts.enabled._, PowerboostListener.class );
		Listen.startOrStopListenerAsNeeded( Config._announce.enabled._, AnnounceListener.class );
		Listen.startOrStopListenerAsNeeded( Config._banning.enabled._, BanListener.class );
		Listen.startOrStopListenerAsNeeded( Config._jails.enabled._, JailListener.class );
		Listen.startOrStopListenerAsNeeded( Config._peaceful.enablePeacefulBoosts._, PeacefulListener.class );
		Listen.startOrStopListenerAsNeeded( Config._extras.crossBorderLiquidFlowBlock._, LiquidFlowListener.class );
		Listen.startOrStopListenerAsNeeded( Config._extras._protection._pvp.shouldInstallDenyClaimListener(), DenyClaimListener.class );
		
		// ChestShop
		if( Bukkit.getPluginManager().getPlugin( "ChestShop" ) != null ) {
			
			Listen.startOrStopListenerAsNeeded( (Config._extras._protection.allowShopsInTerritory._ || Config._extras._protection.allowShopsInWilderness._), ChestShopListener.class );
			
		}
		
		// ChestShop
		if( Bukkit.getPluginManager().getPlugin( "ShowCaseStandalone" ) != null ) {
			
			Listen.startOrStopListenerAsNeeded( (Config._extras._protection.allowShopsInTerritory._ || Config._extras._protection.allowShopsInWilderness._), ShowCaseStandaloneListener.class );
			
		}
		

		// Animal Damager Listener
		
		if(
				! Config._extras._protection.allowFactionKillAlliesMobs._ ||
				! Config._extras._protection.allowFactionKillEnemyMobs._ ||
				! Config._extras._protection.allowFactionKillNeutralMobs._ ||
				Config._extras._protection.protectSafeAnimalsInSafeZone._
		) {
			
			Listen.startOrStopListenerAsNeeded( true, AnimalDamageListener.class );
			
		}
		
		
		
		// Enabled a Score Board of Map and/or Factions
		if( Config._extras._scoreboards.showScoreboardOfMap._ || Config._extras._scoreboards.showScoreboardOfFactions._ ) {
			
			FPP.info( "Enabling scoreboards" );
			
			FactionsPlusScoreboard.setup();
		}
		
		// Load values from disabled_in_warzone.txt
		FactionsPlus.commandsDisabledInWarzone.clear();
		
		BufferedReader buff = null;
		String filterRow = null;
		
		try {
			
			buff = new BufferedReader(new FileReader(Config.fileDisableInWarzone));
			
			String currentCommand;
			
			while((filterRow = buff.readLine()) != null) {
				currentCommand = filterRow.split(" ")[0].replace("/", "");
				
				FactionsPlus.commandsDisabledInWarzone.put(currentCommand, currentCommand);
				FP.info("Baring command " + currentCommand + " in warzones");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(null != buff) {
				try {
					buff.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		// Updates
		FactionsPlusUpdate.enableOrDisableCheckingForUpdates();
		
	} // onConfigLoaded method ends
}