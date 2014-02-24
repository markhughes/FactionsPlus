package markehme.factionsplus.listeners;

import markehme.factionsplus.config.Config;
import me.libraryaddict.disguise.events.DisguiseEvent;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.factions.entity.UPlayer;

/**
 * Lib's Disguises
 * http://dev.bukkit.org/bukkit-plugins/libs-disguises/
 *
 */
public class LDListener implements Listener {
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerDisguise(DisguiseEvent e){
		if( e.isCancelled() || e.getEntity().getType() != EntityType.PLAYER ) {
			return;
		}
		
		Player CurrentPlayer = (Player) e.getEntity();
				
    	UPlayer uPlayer = UPlayer.get( CurrentPlayer );
    	if (!uPlayer.hasFaction()){
    		return;
    	}
    	if(Config._extras._disguise.unDisguiseIfInEnemyTerritory._) {
    		if(uPlayer.isInEnemyTerritory()) {
    			CurrentPlayer.sendMessage(ChatColor.RED+"You may not disguise in enemy territory!");
    			e.setCancelled(true);
    		}
    	}
    		
    	if(Config._extras._disguise.unDisguiseIfInOwnTerritory._) {
    		if(uPlayer.isInOwnTerritory()) {
    			CurrentPlayer.sendMessage(ChatColor.RED+"You may not disguise in your own territory!");
    			e.setCancelled(true);
    		}
    	}


	}
}
