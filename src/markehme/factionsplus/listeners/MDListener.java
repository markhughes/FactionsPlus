package markehme.factionsplus.listeners;

import markehme.factionsplus.*;
import markehme.factionsplus.config.*;
import me.desmin88.mobdisguise.api.MobDisguiseAPI;
import me.desmin88.mobdisguise.api.event.DisguiseEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;

public class MDListener implements Listener  {
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onDisguiseEvent(DisguiseEvent e) {
		FPlayer fplayer = FPlayers.i.get(e.getPlayer());
		
        	if(Config._extras._disguise.unDisguiseIfInEnemyTerritory._) {
        		if(fplayer.isInEnemyTerritory()) {
        			
        			MobDisguiseAPI.undisguisePlayer(e.getPlayer());
        			e.getPlayer().sendMessage("You have been un-disguised due to being in enemy territory!");
        			e.setCancelled(true);
        		}
        	}
        		
        	if(Config._extras._disguise.unDisguiseIfInOwnTerritory._) {
        		if(fplayer.isInOwnTerritory()) {
        			
        			MobDisguiseAPI.undisguisePlayer(e.getPlayer());
        			e.getPlayer().sendMessage("You have been un-disguised due to being in your own territory!");
        			e.setCancelled(true);
        		}
        	}
        
	}
}
