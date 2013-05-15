package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.FactionsBridge.Bridge;
import markehme.factionsplus.FactionsBridge.FactionsAny;
import markehme.factionsplus.FactionsBridge.FactionsAny.Relation;
import markehme.factionsplus.config.Config;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;

public class MVPListener implements Listener {
	
	@EventHandler(priority=EventPriority.HIGH)
	public void MVPortalEvent(com.onarandombox.MultiversePortals.event.MVPortalEvent e) {
		
		FPlayer fplayer = FPlayers.i.get( e.getTeleportee() );
		Relation rel = Bridge.factions.getRelationBetween( fplayer.getFaction(),  Board.getFactionAt( e.getFrom() ));
		
		if(FactionsPlus.permission.has(e.getTeleportee(), "factionsplus.useanyportal") || fplayer.isInOwnTerritory() ) {
			return;
		}
		
		// SafeZone / Wilderness / WarZone = not a faction, so rules do not apply
		if(Utilities.isSafeZone(Board.getFactionAt( e.getFrom() )) || Utilities.isWilderness(Board.getFactionAt( e.getFrom() )) || Utilities.isWarZone(Board.getFactionAt( e.getFrom() )) ) {
			return;
		}
		
		if(Config._extras._MultiVerse.enemyCantUseEnemyPortals._) {
			if(fplayer.isInEnemyTerritory()) {
				
				fplayer.msg("Enemies can not use share portals. ");
				e.setCancelled(true);
				
				return;
			}
		}
				
		if(Config._extras._MultiVerse.alliesCanUseEachOthersPortals._ && Config._extras._MultiVerse.mustBeInOwnTerritoryToUsePortals._) {
			
			if ( !FactionsAny.Relation.NEUTRAL.equals( rel ) ) {
				
				fplayer.msg("You can not use this portal as you are not an aly, and are not apart of this Faction. ");
				e.setCancelled(true);
				
				return;
				
			} else {
				return;
			}
		}
		
		
	}
}
