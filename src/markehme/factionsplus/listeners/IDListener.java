package markehme.factionsplus.listeners;

import markehme.factionsplus.config.Config;
import markehme.factionsplus.references.FP;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.factions.entity.UPlayer;

import de.robingrether.idisguise.api.DisguiseEvent;

/**
 * iDisguise
 * http://dev.bukkit.org/bukkit-plugins/idisguise/
 *
 */
public class IDListener implements Listener {
		
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerDisguise(DisguiseEvent e) {
		if (e.isCancelled()){
			return;
		}
		
    	UPlayer uPlayer = UPlayer.get( e.getPlayer() );
    	if (!uPlayer.hasFaction()){
    		return;
    	}
    	
    	if(Config._extras._disguise.unDisguiseIfInEnemyTerritory._) {
    		if(uPlayer.isInEnemyTerritory()) {
    			e.getPlayer().sendMessage(ChatColor.RED+"You may not disguise in enemy territory!");
    			e.setCancelled(true);
    		}
    	}
    		
    	if(Config._extras._disguise.unDisguiseIfInOwnTerritory._) {
    		if(uPlayer.isInOwnTerritory()) {
       			e.getPlayer().sendMessage(ChatColor.RED+"You may not disguise in your own territory!");
    			e.setCancelled(true);
    		}
    	}
	}
}
