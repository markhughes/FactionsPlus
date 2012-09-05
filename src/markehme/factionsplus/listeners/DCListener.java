package markehme.factionsplus.listeners;

import markehme.factionsplus.config.Config;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import pgDev.bukkit.DisguiseCraft.api.PlayerDisguiseEvent;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;

public class DCListener implements Listener  {//http://dev.bukkit.org/server-mods/disguisecraft/
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerDisguise(PlayerDisguiseEvent e) {
		if (e.isCancelled()){
			return;
		}
		
    	FPlayer fplayer = FPlayers.i.get(e.getPlayer());
    	if (!fplayer.hasFaction()){
    		return;
    	}
    	
    		if(Config._extras._disguise.unDisguiseIfInEnemyTerritory._) {
    			if(fplayer.isInEnemyTerritory()) {
    				e.getPlayer().sendMessage(ChatColor.RED+"You may not disguise in enemy territory!");
    				e.setCancelled(true);
    			}
    		}
    		
    		if(Config._extras._disguise.unDisguiseIfInOwnTerritory._) {
    			if(fplayer.isInOwnTerritory()) {
       				e.getPlayer().sendMessage(ChatColor.RED+"You may not disguise in your own territory!");
    				e.setCancelled(true);
    			}
    		}
    	}
	
	
}
