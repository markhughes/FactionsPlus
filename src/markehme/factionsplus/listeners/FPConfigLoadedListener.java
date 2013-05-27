package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusPlugin;
import markehme.factionsplus.FactionsPlusUpdate;
import markehme.factionsplus.config.Config;
import markehme.factionsplus.events.FPConfigLoadedEvent;
import markehme.factionsplus.extras.LWCBase;
import markehme.factionsplus.extras.LWCFunctions;
import markehme.factionsplus.extras.LocketteBase;
import markehme.factionsplus.extras.LocketteFunctions;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;



public class FPConfigLoadedListener implements Listener {
	
	/**
	 * called after the config is (re)loaded, which is typically when plugin is enabled and when /f reloadfp  is issued<br>
	 */
	@EventHandler
	public void onConfigLoaded( @SuppressWarnings( "unused" ) FPConfigLoadedEvent event ) {
        Config._economy.enableOrDisableEconomy();
        //TODO: add more here and make sure they can change states between on/off just like they would by a server 'reload' command
        //because this hook is called every time the config is reloaded, which means some things could have been previously enabled
        //and now the config may dictate that they are disabled (state changed) so we must properly handle that behaviour.
        TeleportsListener.initOrDeInit(FactionsPlus.instance);
       
        PluginManager pm = Bukkit.getServer().getPluginManager();
        
        FactionsPlusUpdate.enableOrDisableCheckingForUpdates();
        
       
        
        
//        LWCBase.refreshLWC();
        if ( LWCBase.isLWCPluginPresent() ) {// LWCFunctions.isLWC() also works here though
			
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

			//this after the above setting
			LWCFunctions.hookLWCIfNeeded();//XXX: this must be inside an if, else NoClassDefFoundError if LWC is not on
			
		} else {//no LWC
			if ( Config._extras._protection._lwc.blockCPublicAccessOnNonOwnFactionTerritory._ 
				|| Config._extras._protection._lwc.removeAllLocksOnClaim._ ) 
			{
				FactionsPlusPlugin
					.warn( "LWC plugin was not found(or not enabled yet) but a few settings that require LWC are Enabled!"
						+ " This means those settings will be ignored & have no effect" );
			}
			
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

		// PowerboostListener.startOrStopPowerBoostsListenerAsNeeded();
		Listen.startOrStopListenerAsNeeded( Config._powerboosts.enabled._, PowerboostListener.class );
		Listen.startOrStopListenerAsNeeded( Config._announce.enabled._, AnnounceListener.class );
		Listen.startOrStopListenerAsNeeded( Config._banning.enabled._, BanListener.class );
		Listen.startOrStopListenerAsNeeded( Config._jails.enabled._, JailListener.class );
		Listen.startOrStopListenerAsNeeded( Config._peaceful.enablePeacefulBoosts._, PeacefulListener.class );
		Listen.startOrStopListenerAsNeeded( Config._extras.crossBorderLiquidFlowBlock._, LiquidFlowListener.class );
		Listen.startOrStopListenerAsNeeded( Config._extras._protection._pvp.shouldInstallDenyClaimListener(), DenyClaimListener.class );
        
	} //onConfigLoaded method ends
}
