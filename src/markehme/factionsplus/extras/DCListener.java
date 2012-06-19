package markehme.factionsplus.extras;

import markehme.factionsplus.FactionsPlus;

import org.bukkit.event.Listener;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;

import pgDev.bukkit.DisguiseCraft.api.PlayerDisguiseEvent;

public class DCListener implements Listener  {
	public void onPlayerDisguise(PlayerDisguiseEvent e) {
    	FPlayer fplayer = FPlayers.i.get(e.getPlayer());
    		if(FactionsPlus.config.getBoolean("unDisguiseIfInEnemyTerritory")) {
    			if(fplayer.isInEnemyTerritory()) {
    				e.getPlayer().sendMessage("You have been un-disguised!");
    				e.setCancelled(true);
    			}
    		}
    		
    		if(FactionsPlus.config.getBoolean("unDisguiseIfInOwnTerritory")) {
    			if(fplayer.isInOwnTerritory()) {
       				e.getPlayer().sendMessage("You have been un-disguised!");
    				e.setCancelled(true);
    			}
    		}
    	}
	
	
}
