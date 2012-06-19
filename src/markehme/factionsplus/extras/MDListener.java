package markehme.factionsplus.extras;

import markehme.factionsplus.FactionsPlus;
import me.desmin88.mobdisguise.api.MobDisguiseAPI;
import me.desmin88.mobdisguise.api.event.DisguiseEvent;

import org.bukkit.event.Listener;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;

public class MDListener implements Listener  {
	public void onDisguiseEvent(DisguiseEvent e) {
		FPlayer fplayer = FPlayers.i.get(e.getPlayer());
		
        	if(FactionsPlus.config.getBoolean("unDisguiseIfInEnemyTerritory")) {
        		if(fplayer.isInEnemyTerritory()) {
        			
        			MobDisguiseAPI.undisguisePlayer(e.getPlayer());
        			e.getPlayer().sendMessage("You have been un-disguised!");
        			e.setCancelled(true);
        		}
        	}
        		
        	if(FactionsPlus.config.getBoolean("unDisguiseIfInOwnTerritory")) {
        		if(fplayer.isInOwnTerritory()) {
        			
        			MobDisguiseAPI.undisguisePlayer(e.getPlayer());
        			e.getPlayer().sendMessage("You have been un-disguised!");
        			e.setCancelled(true);
        		}
        	}
        
	}
}
