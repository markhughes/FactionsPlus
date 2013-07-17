package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusPlugin;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.ps.PS;
import com.onarandombox.MultiverseCore.event.MVPlayerTouchedPortalEvent;
import com.onarandombox.MultiverseCore.event.MVTeleportEvent;
import com.onarandombox.MultiversePortals.event.MVPortalEvent;

public class MVPListener implements Listener {
	
	public static boolean isMVPIntegrated = false;
	public static MVPListener mvplistener;
	

	@EventHandler(priority=EventPriority.HIGH)
	public void MVPlayerTouchedPortalEvent(MVPortalEvent e) {
		// TODO: Put messages into template config
		UPlayer fplayer = UPlayer.get( e.getTeleportee() );
			
		Rel rel = fplayer.getFaction().getRelationTo(BoardColls.get().getFactionAt( PS.valueOf( e.getTeleportee().getLocation() ) ));
		
		if(FactionsPlus.permission.has(e.getTeleportee(), "factionsplus.useanyportal") || fplayer.isInOwnTerritory() ) {
			return;
		}
		
		
		// SafeZone / Wilderness / WarZone = not a faction, so rules do not apply
		if(Utilities.isSafeZone(BoardColls.get().getFactionAt( PS.valueOf(e.getTeleportee().getLocation() ))) || Utilities.isWilderness(BoardColls.get().getFactionAt( PS.valueOf( e.getTeleportee().getLocation() ) )) || Utilities.isWarZone(BoardColls.get().getFactionAt( PS.valueOf( e.getTeleportee().getLocation() ) )) ) {
			return;
		}
		
		if(Config._extras._MultiVerse.enemyCantUseEnemyPortals._) {
			if(fplayer.isInEnemyTerritory()) {
				
				fplayer.msg(ChatColor.RED + "Enemies can not share portals. ");
				e.setCancelled(true);
				
				return;
			}
		}
				
		if(Config._extras._MultiVerse.alliesCanUseEachOthersPortals._ && Config._extras._MultiVerse.mustBeInOwnTerritoryToUsePortals._) {
			
			if ( !Rel.ALLY.equals( rel ) ) {
				fplayer.msg(ChatColor.RED + "You can not use this portal as you are not an aly, and are not apart of this Faction. ");
				e.setCancelled(true);
				
				return;
				
			} else {
				if(fplayer.getFactionId() == "0") {
					fplayer.msg(ChatColor.RED + "You are not apart of a Faction, so we can not determine if you are enemies or not.");
					e.setCancelled(true);
					return;
				} else {
					return;
				}
			}
		} else {
			fplayer.msg(ChatColor.RED + "You must be apart of this Faction to use this portal.");
			e.setCancelled(true);
			return;
		}		
		
	}
	
	public static final void enableOrDisable(FactionsPlus instance){
 		PluginManager pm = Bukkit.getServer().getPluginManager();
			
		boolean isMVPplugin = pm.isPluginEnabled("Multiverse-Portals");
		
		if ( isMVPplugin && !isMVPIntegrated ) {
			assert ( null == mvplistener );
			
			mvplistener = new MVPListener();
			pm.registerEvents( mvplistener, instance );
			
			if (null == mvplistener) {
				mvplistener = new MVPListener();
				Bukkit.getServer().getPluginManager().registerEvents(mvplistener, instance);
			}
			
			FactionsPlusPlugin.info( "Hooked into Multiverse-portals." );
		}	
	}
}
