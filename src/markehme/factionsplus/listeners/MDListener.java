package markehme.factionsplus.listeners;

import markehme.factionsplus.config.*;
import me.desmin88.mobdisguise.api.*;
import me.desmin88.mobdisguise.api.event.*;

import org.bukkit.*;
import org.bukkit.event.*;

import com.massivecraft.factions.*;

public class MDListener implements Listener  {//http://dev.bukkit.org/server-mods/mobdisguise/
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onDisguiseEvent(DisguiseEvent e) {
		if (e.isCancelled()){
			return;
		}
		
		FPlayer fplayer = FPlayers.i.get(e.getPlayer());
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
