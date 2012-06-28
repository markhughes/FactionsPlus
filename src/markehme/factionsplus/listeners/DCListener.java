package markehme.factionsplus.listeners;

import markehme.factionsplus.*;
import markehme.factionsplus.config.*;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;

import pgDev.bukkit.DisguiseCraft.api.PlayerDisguiseEvent;

public class DCListener implements Listener  {
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerDisguise(PlayerDisguiseEvent e) {
    	FPlayer fplayer = FPlayers.i.get(e.getPlayer());
    		if(Config.config.getBoolean("unDisguiseIfInEnemyTerritory")) {
    			if(fplayer.isInEnemyTerritory()) {
    				e.getPlayer().sendMessage("You have been un-disguised due to being on enemy territory!");
    				e.setCancelled(true);
    			}
    		}
    		
    		if(Config.config.getBoolean("unDisguiseIfInOwnTerritory")) {
    			if(fplayer.isInOwnTerritory()) {
       				e.getPlayer().sendMessage("You have been un-disguised due to being on your own territory!");
    				e.setCancelled(true);
    			}
    		}
    	}
	
	
}
