package markehme.factionsplus.listeners;

import markehme.factionsplus.config.*;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;

import pgDev.bukkit.DisguiseCraft.api.PlayerDisguiseEvent;

public class DCListener implements Listener  {//http://dev.bukkit.org/server-mods/disguisecraft/
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerDisguise(PlayerDisguiseEvent e) {
		if (e.isCancelled()){
			return;
		}
		
    	FPlayer fplayer = FPlayers.i.get(e.getPlayer());
    		if(Config._extras._disguise.unDisguiseIfInEnemyTerritory._) {
    			if(fplayer.isInEnemyTerritory()) {
    				e.getPlayer().sendMessage("You have been un-disguised due to being on enemy territory!");
    				e.setCancelled(true);
    			}
    		}
    		
    		if(Config._extras._disguise.unDisguiseIfInOwnTerritory._) {
    			if(fplayer.isInOwnTerritory()) {
       				e.getPlayer().sendMessage("You have been un-disguised due to being on your own territory!");
    				e.setCancelled(true);
    			}
    		}
    	}
	
	
}
