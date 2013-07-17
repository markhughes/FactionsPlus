package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.config.Config;
import markehme.factionsplus.references.FP;
import markehme.factionsplus.references.FPP;
import me.desmin88.mobdisguise.api.MobDisguiseAPI;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;

import com.massivecraft.factions.entity.UPlayer;

import pgDev.bukkit.DisguiseCraft.DisguiseCraft;
import pgDev.bukkit.DisguiseCraft.api.DisguiseCraftAPI;


public class DisguiseListener implements Listener {
	private static final String	MOB_DISGUISE	= "MobDisguise";
	private static final String	DISGUISE_CRAFT	= "DisguiseCraft";

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if(event.isCancelled()) {
			return;
		}

		UPlayer fplayer = UPlayer.get(event.getPlayer());
		if(!fplayer.hasFaction()) {
    		return;
    	}
		
		if(isMobDisguiseIntegrated()) {

			if(MobDisguiseAPI.isDisguised(event.getPlayer())) {
				if(Config._extras._disguise.unDisguiseIfInEnemyTerritory._) {
					if(fplayer.isInEnemyTerritory()) {
						MobDisguiseAPI.undisguisePlayer(event.getPlayer());
						event.getPlayer().sendMessage("You have been un-disguised!");
					}
				}

				if(Config._extras._disguise.unDisguiseIfInOwnTerritory._) {
					if(fplayer.isInOwnTerritory()) {
						MobDisguiseAPI.undisguisePlayer(event.getPlayer());
						event.getPlayer().sendMessage("You have been un-disguised!");
					}
				}
			}
		}

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


	}

	
	private static DisguiseListener	disguiselistener	= null;
	private static DCListener		dclistener			= null;
	private static MDListener		mdlistener 			= null;

	public final static boolean isMobDisguiseIntegrated() {
		return null != mdlistener;
	}

	public final static boolean isDisguiseCraftIntegrated() {
		return null != dclistener;
	}

	private final static void disintegrateDC() {
//		assert null != dclistener:"bad call: DC was not already integrated";
		if ( null != dclistener ) {
			HandlerList.unregisterAll( dclistener );
			dclistener = null;
			FP.info( "DC is disintegrated" );
			if (!isMobDisguiseIntegrated()) {
				disintegrateCommon();
			}
		}
	}
	
	
	private final static void disintegrateMD() {
		if (null != mdlistener) {
			HandlerList.unregisterAll( mdlistener );
			mdlistener = null;
			FP.info( "MD is disintegrated" );
			if (!isDisguiseCraftIntegrated()) {
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
			
			//we allow both MD and DC to be enabled, but the chances are only DC is.
			
			boolean isDCplugin=pm.isPluginEnabled(DISGUISE_CRAFT);
			if ( isDCplugin && !isDisguiseCraftIntegrated() ) {
				// not already integrated? do it now
				assert ( null == dclistener );
				dclistener = new DCListener();
				pm.registerEvents( dclistener, instance );
				ensureCommonIsAllocated( instance );
				FPP.info( "Hooked into "+DISGUISE_CRAFT+"!" );
			} else
				if ( !isDCplugin && isDisguiseCraftIntegrated() ) {
					// either the plugin went offline or the configs changed, we need to disable integration:
					disintegrateDC();
				}
        	
			boolean isMDplugin = pm.isPluginEnabled( MOB_DISGUISE );
			if ( isMDplugin && !isMobDisguiseIntegrated() ) {
				assert ( null == mdlistener );
				mdlistener = new MDListener();
				pm.registerEvents( mdlistener, instance );
				ensureCommonIsAllocated( instance );
				FPP.info( "Hooked into "+MOB_DISGUISE+"!" );
			} else
				if ( !isMDplugin && isMobDisguiseIntegrated() ) {
					disintegrateMD();
				}

        	if (!isDCplugin && !isMDplugin) {//if neither plugin exists (we know the options exists though)
        		FPP.warn("MobDisguise or DisguiseCraft integration enabled in config, but " +
        				"none of these plugins are installed!");
        	}
        }else{
        	if (isDisguiseCraftIntegrated()) {
        		disintegrateDC();
        	}
        	
        	if (isMobDisguiseIntegrated()) {
        		disintegrateMD();
        	}
        }
	}

}
