package markehme.factionsplus.listeners;

import markehme.factionsplus.references.FPP;

import me.desmin88.mobdisguise.api.event.DisguiseEvent;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * It appears MobDisguise is inactive and no longer functioning.
 * DisguiseCraft should be used instead. 
 *
 */
@Deprecated
public class MDListener implements Listener  {
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onDisguiseEvent(DisguiseEvent e) {
		if (e.isCancelled()){
			return;
		}
		
		e.getPlayer().sendMessage(ChatColor.RED+"MobDisguise is deprecated from FactionsPlus.");
		e.getPlayer().sendMessage(ChatColor.RED+"Request that the server use DisguiseCraft instead.");
		
		// Send some annoying messages - they'll get the picture. 
		FPP.severe("MobDisguise DisguiseEvent detected");
		FPP.warn("Warning: MobDisguise is deprecated from FactionsPlus.");
		FPP.warn("MobDisguise is deprecated from FactionsPlus, and DisguiseCraft should be used instead.");
		
		FPP.info("If MobDisguise is back in development, notify FactionsPlus developers ASAP.");
		
		return;
		
		/*
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
        */
	}
}
