package markehme.factionsplus.listeners;

import java.util.Observer;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.config.Config;
import markehme.factionsplus.references.FP;
import markehme.factionsplus.references.FPP;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.FlagWatcher;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import me.libraryaddict.disguise.utilities.DisguiseUtilities;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;

import com.massivecraft.factions.entity.UPlayer;

import de.robingrether.idisguise.api.DisguiseAPI;

import pgDev.bukkit.DisguiseCraft.DisguiseCraft;
import pgDev.bukkit.DisguiseCraft.api.DisguiseCraftAPI;

public class DisguiseListener implements Listener {
	private static final String	IDISGUISE		= "iDisguise";
	private static final String	DISGUISE_CRAFT	= "DisguiseCraft";
	private static final String	LIBS_DISGUISE	= "LibsDisguises";

	private static DisguiseListener	disguiselistener	= null;
	
	private static DCListener		dclistener			= null;
	private static IDListener		idlistener 			= null;
	private static LDListener		ldlistener 			= null;

	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if(event.isCancelled()) {
			return;
		}
		
		UPlayer fplayer = UPlayer.get(event.getPlayer());
		
		if(!fplayer.hasFaction()) {
    		return;
    	}
		
		/*
		 *  Does a check with DisguiseCraft 
		 */

		if(isDisguiseCraftIntegrated()) {
			DisguiseCraftAPI dcAPI = DisguiseCraft.getAPI();

			if(dcAPI.isDisguised(event.getPlayer())) {
				if(Config._extras._disguise.unDisguiseIfInEnemyTerritory._) {
					if(fplayer.isInEnemyTerritory()) {
						dcAPI.undisguisePlayer(event.getPlayer());
						event.getPlayer().sendMessage("You have been un-disguised!");
					}
				}

				if(Config._extras._disguise.unDisguiseIfInOwnTerritory._) {
					if(fplayer.isInOwnTerritory()) {
						dcAPI.undisguisePlayer(event.getPlayer());
						event.getPlayer().sendMessage("You have been un-disguised!");
					}
				}
			}
		}
		
		/*
		 * Does a check with iDisguise 
		 */
		
		if(isiDisguiseIntegrated()) {
			
			DisguiseAPI api = FP.server.getServicesManager().getRegistration(DisguiseAPI.class).getProvider();
			
			if(api.isDisguised(event.getPlayer())) {
				if(Config._extras._disguise.unDisguiseIfInEnemyTerritory._) {
					if(fplayer.isInEnemyTerritory()) {
						api.undisguiseToAll(event.getPlayer());
						event.getPlayer().sendMessage("You have been un-disguised!");
					}
				}

				if(Config._extras._disguise.unDisguiseIfInOwnTerritory._) {
					if(fplayer.isInOwnTerritory()) {
						api.undisguiseToAll(event.getPlayer());
						event.getPlayer().sendMessage("You have been un-disguised!");
					}
				}
			}
		}
		
		
		/*
		 * Does a check with Libs' Disguise 
		 */
		
		if(isLibsDisguiseIntegrated()) {
			
			if(DisguiseUtilities.getDisguise(event.getPlayer(), event.getPlayer().getEntityId()) != null) {

				if(Config._extras._disguise.unDisguiseIfInEnemyTerritory._) {
					if(fplayer.isInEnemyTerritory()) {
						me.libraryaddict.disguise.DisguiseAPI.undisguiseToAll(event.getPlayer());
						
						event.getPlayer().sendMessage("You have been un-disguised!");
					}
				}

				if(Config._extras._disguise.unDisguiseIfInOwnTerritory._) {
					if(fplayer.isInOwnTerritory()) {
						me.libraryaddict.disguise.DisguiseAPI.undisguiseToAll(event.getPlayer());
						event.getPlayer().sendMessage("You have been un-disguised!");
					}
				}
			}
		}


	}
	
	public final static boolean isiDisguiseIntegrated() {
		return null != idlistener;
	}

	public final static boolean isDisguiseCraftIntegrated() {
		return null != dclistener;
	}
	
	public final static boolean isLibsDisguiseIntegrated() {
		return null != ldlistener;
	}

	private final static void disintegrateDC() {
		if ( null != dclistener ) {
			
			HandlerList.unregisterAll( dclistener );
			dclistener = null;
			FP.info( "iDisguise is disintegrated" );
			if (!isiDisguiseIntegrated()) {
				disintegrateCommon();
			}
			
		}
	}
	
	
	private final static void disintegrateiD() {
		if (null != idlistener) {
			HandlerList.unregisterAll( idlistener );
			idlistener = null;
			FP.info( "iDisguise is disintegrated" );
			if (!isDisguiseCraftIntegrated()) {
				disintegrateCommon();
			}
		}
	}
	
	private final static void disintegrateLD() {
		if (null != ldlistener) {
			HandlerList.unregisterAll( ldlistener );
			ldlistener = null;
			FP.info( "Lib's Disguise is disintegrated" );
			if (!isLibsDisguiseIntegrated()) {
				disintegrateCommon();
			}
		}
	}
	
	private static void disintegrateCommon() {
		if (null != disguiselistener) {
			HandlerList.unregisterAll( disguiselistener );
			disguiselistener=null;
//			FactionsPlus.info( "DC/MD common is disintegrated" );we don't need to show this, was for debug only
			//TODO: should've used booleans instead of unlinking the already allocated instance, to avoid reallocating memory 
			//on next state change but this should be insignificant
		}
	}

	
	private final static void ensureCommonIsAllocated(FactionsPlus instance) {
		if (null == disguiselistener) {
			disguiselistener=new DisguiseListener();
			Bukkit.getServer().getPluginManager().registerEvents(disguiselistener, instance);
		}
	}
	
	public static final void enableOrDisable(FactionsPlus instance){
		if(Config._extras._disguise.enableDisguiseIntegration._ && 
				(Config._extras._disguise.unDisguiseIfInOwnTerritory._ || Config._extras._disguise.unDisguiseIfInEnemyTerritory._)) {
        	
			PluginManager pm = Bukkit.getServer().getPluginManager();
			
			
			
			// Attempt to setup DisguiseCraft
			boolean isDCplugin=pm.isPluginEnabled(DISGUISE_CRAFT);
			if ( isDCplugin && !isDisguiseCraftIntegrated() ) {
				assert ( null == dclistener );
				dclistener = new DCListener();
				pm.registerEvents( dclistener, instance );
				ensureCommonIsAllocated( instance );
				FPP.info( "Hooked into "+DISGUISE_CRAFT+"!" );
			} else if ( !isDCplugin && isDisguiseCraftIntegrated() ) {
				// either the plugin unloaded or the configs changed, we need to disable integration:
				disintegrateDC();
			}
        	
			// Attempt to setup iDisguise 
			boolean isiDplugin = pm.isPluginEnabled( IDISGUISE );
			if ( isiDplugin && !isiDisguiseIntegrated() ) {
				assert ( null == idlistener );
				idlistener = new IDListener();
				pm.registerEvents( idlistener, instance );
				ensureCommonIsAllocated( instance );
				FPP.info( "Hooked into "+IDISGUISE+"!" );
			} else if ( !isiDplugin && isiDisguiseIntegrated() ) {
					disintegrateiD();
			}
			
			// Attempt to setup Lib's Disguise
			boolean isLDplugin=pm.isPluginEnabled(LIBS_DISGUISE);
			if ( isLDplugin && !isLibsDisguiseIntegrated() ) {
				assert ( null == ldlistener );
				ldlistener = new LDListener();
				pm.registerEvents( ldlistener, instance );
				ensureCommonIsAllocated( instance );
				FPP.info( "Hooked into "+LIBS_DISGUISE+"!" );
			} else if ( !isLDplugin && isLibsDisguiseIntegrated() ) {
				disintegrateLD();
			}
			
			// Now, confirm one of them at least existed
				
	        if (!isDCplugin && !isiDplugin && !isLDplugin) {//if neither plugin exists (we know the options exists though)
	        	FPP.warn("DisguiseCraft, iDisguise, or LibsDisguises integration enabled in config, but " +
	        				"none of these plugins are installed!");
	        
	        }
	        
		} else {
        	if (isDisguiseCraftIntegrated()) {
        		disintegrateDC();
        	}
        	
        	if (isiDisguiseIntegrated()) {
        		disintegrateiD();
        	}
        	
        	if (isLibsDisguiseIntegrated()) {
        		disintegrateLD();
        	}
        }
	}

}
