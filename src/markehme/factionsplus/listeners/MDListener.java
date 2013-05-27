package markehme.factionsplus.listeners;

import markehme.factionsplus.config.Config;

import me.desmin88.mobdisguise.api.MobDisguiseAPI;
import me.desmin88.mobdisguise.api.event.DisguiseEvent;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.factions.entity.UPlayer;

/**
 * MobDisguise
 * http://dev.bukkit.org/server-mods/mobdisguise/
 *
 */
public class MDListener implements Listener  {
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onDisguiseEvent(DisguiseEvent e) {
		if (e.isCancelled()){
			return;
		}
		
		UPlayer fplayer = UPlayer.get(e.getPlayer());
		if (!fplayer.hasFaction()){
    		return;
    	}
		
        	if(Config._extras._disguise.unDisguiseIfInEnemyTerritory._) {
        		if(fplayer.isInEnemyTerritory()) {
        			
        			MobDisguiseAPI.undisguisePlayer(e.getPlayer());
        			e.getPlayer().sendMessage(ChatColor.RED+"You may not disguise in enemy territory!");
        			e.setCancelled(true);
        		}
        	}
        		
        	if(Config._extras._disguise.unDisguiseIfInOwnTerritory._) {
        		if(fplayer.isInOwnTerritory()) {
        			
        			MobDisguiseAPI.undisguisePlayer(e.getPlayer());
        			e.getPlayer().sendMessage(ChatColor.RED+"You may not disguise in your own territory!");
        			e.setCancelled(true);
        		}
        	}
        
	}
}
