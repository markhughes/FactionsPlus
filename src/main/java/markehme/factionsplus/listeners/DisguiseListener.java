package markehme.factionsplus.listeners;


import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.MCore.LConf;
import me.libraryaddict.disguise.utilities.DisguiseUtilities;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;

import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.util.Txt;

import de.robingrether.idisguise.api.DisguiseAPI;

import pgDev.bukkit.DisguiseCraft.DisguiseCraft;
import pgDev.bukkit.DisguiseCraft.api.DisguiseCraftAPI;

public class DisguiseListener implements Listener {
	private static final String	DISGUISE_CRAFT	= "DisguiseCraft";
	private static final String	IDISGUISE		= "iDisguise";
	private static final String	LIBS_DISGUISE	= "LibsDisguises";

	private static DisguiseListener	disguiselistener	= null;
	
	private static DCListener		dclistener			= null;
	private static IDListener		idlistener 			= null;
	private static LDListener		ldlistener 			= null;

	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		
		if(!FPUConf.get(UPlayer.get(event.getPlayer()).getUniverse()).enabled) return;
		
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
				
				if(FPUConf.get(fplayer.getUniverse()).disguiseRemoveIfInEnemyTerritory) {
					if(fplayer.isInEnemyTerritory()) {
						dcAPI.undisguisePlayer(event.getPlayer());
						event.getPlayer().sendMessage(Txt.parse(LConf.get().disguisesUndisguised));
					}
				}

				if(FPUConf.get(fplayer.getUniverse()).disguiseRemoveIfInOwnTerritory) {
					if(fplayer.isInOwnTerritory()) {
						dcAPI.undisguisePlayer(event.getPlayer());
						event.getPlayer().sendMessage(Txt.parse(LConf.get().disguisesUndisguised));
					}
				}
			}
		}
		
		/*
		 * Does a check with iDisguise 
		 */
		
		if(isiDisguiseIntegrated()) {
			
			DisguiseAPI api = FactionsPlus.instance.getServer().getServicesManager().getRegistration(DisguiseAPI.class).getProvider();
			
			if(api.isDisguised(event.getPlayer())) {
				if(FPUConf.get(fplayer.getUniverse()).disguiseRemoveIfInEnemyTerritory) {
					if(fplayer.isInEnemyTerritory()) {
						api.undisguiseToAll(event.getPlayer());
						event.getPlayer().sendMessage(Txt.parse(LConf.get().disguisesUndisguised));
					}
				}

				if(FPUConf.get(fplayer.getUniverse()).disguiseRemoveIfInOwnTerritory) {
					if(fplayer.isInOwnTerritory()) {
						api.undisguiseToAll(event.getPlayer());
						event.getPlayer().sendMessage(Txt.parse(LConf.get().disguisesUndisguised));
					}
				}
			}
		}
		
		
		/*
		 * Does a check with Libs' Disguise 
		 */
		
		if(isLibsDisguiseIntegrated()) {
			
			if(DisguiseUtilities.getDisguise(event.getPlayer(), event.getPlayer()) != null) {

				if(FPUConf.get(fplayer.getUniverse()).disguiseRemoveIfInEnemyTerritory) {
					if(fplayer.isInEnemyTerritory()) {
						me.libraryaddict.disguise.DisguiseAPI.undisguiseToAll(event.getPlayer());
						
						event.getPlayer().sendMessage(Txt.parse(LConf.get().disguisesUndisguised));
					}
				}

				if(FPUConf.get(fplayer.getUniverse()).disguiseRemoveIfInOwnTerritory) {
					if(fplayer.isInOwnTerritory()) {
						me.libraryaddict.disguise.DisguiseAPI.undisguiseToAll(event.getPlayer());
						event.getPlayer().sendMessage(Txt.parse(LConf.get().disguisesUndisguised));
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
			FactionsPlus.debug( "iDisguise is disintegrated" );
			if (!isiDisguiseIntegrated()) {
				disintegrateCommon();
			}
			
		}
	}
	
	
	private final static void disintegrateiD() {
		if (null != idlistener) {
			HandlerList.unregisterAll( idlistener );
			idlistener = null;
			FactionsPlus.debug( "iDisguise is disintegrated" );
			if (!isDisguiseCraftIntegrated()) {
				disintegrateCommon();
			}
		}
	}
	
	private final static void disintegrateLD() {
		if (null != ldlistener) {
			HandlerList.unregisterAll( ldlistener );
			ldlistener = null;
			FactionsPlus.debug( "Lib's Disguise is disintegrated" );
			if (!isLibsDisguiseIntegrated()) {
				disintegrateCommon();
			}
		}
	}
	
	private static void disintegrateCommon() {
		if (null != disguiselistener) {
			HandlerList.unregisterAll( disguiselistener );
			disguiselistener=null;
		}
	}

	
	private final static void ensureCommonIsAllocated(FactionsPlus instance) {
		if (null == disguiselistener) {
			disguiselistener=new DisguiseListener();
			Bukkit.getServer().getPluginManager().registerEvents(disguiselistener, instance);
		}
	}
	
	public static final void enableOrDisable(FactionsPlus instance){
        	
			PluginManager pm = Bukkit.getServer().getPluginManager();
			
			// Attempt to setup DisguiseCraft
			boolean isDCplugin=pm.isPluginEnabled(DISGUISE_CRAFT);
			if ( isDCplugin && !isDisguiseCraftIntegrated() ) {
				assert ( null == dclistener );
				dclistener = new DCListener();
				pm.registerEvents( dclistener, instance );
				ensureCommonIsAllocated( instance );
				FactionsPlus.debug( "Hooked into "+DISGUISE_CRAFT+"!" );
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
				FactionsPlus.debug( "Hooked into "+IDISGUISE+"!" );
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
				FactionsPlus.debug( "Hooked into "+LIBS_DISGUISE+"!" );
			} else if ( !isLDplugin && isLibsDisguiseIntegrated() ) {
				disintegrateLD();
			}
			
			// Now, confirm one of them at least existed
				
	        if (!isDCplugin && !isiDplugin && !isLDplugin) { //if neither plugin exists (we know the options exists though)
	        	FactionsPlus.warn("DisguiseCraft, iDisguise, or LibsDisguises integration enabled in config, but " +
	        				"none of these plugins are installed!");
	        
	        }
	        
	}

}
