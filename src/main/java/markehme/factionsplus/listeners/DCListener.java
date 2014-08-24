package markehme.factionsplus.listeners;

import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.MCore.LConf;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.util.Txt;

import pgDev.bukkit.DisguiseCraft.api.PlayerDisguiseEvent;

/**
 * DisguiseCraft Listener
 * 
 * http://dev.bukkit.org/server-mods/disguisecraft/
 *
 */
public class DCListener implements Listener  { 
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerDisguise(PlayerDisguiseEvent e) {
		if (e.isCancelled()){
			return;
		}
		
    	UPlayer uPlayer = UPlayer.get( e.getPlayer() );
    	
		if(!FPUConf.get(uPlayer.getUniverse()).enabled) return;

    	if (!uPlayer.hasFaction()){
    		return;
    	}
		
    	if(FPUConf.get(UPlayer.get(e.getPlayer()).getUniverse()).disguiseRemoveIfInEnemyTerritory) {
    		if(uPlayer.isInEnemyTerritory()) {
    			uPlayer.msg(Txt.parse(LConf.get().disguisesCantDisguiseInEnemyTerritory));
    			e.setCancelled(true);
    		}
    	}
    		
    	if(FPUConf.get(UPlayer.get(e.getPlayer()).getUniverse()).disguiseRemoveIfInOwnTerritory) {
    		if(uPlayer.isInOwnTerritory()) {
    			uPlayer.msg(Txt.parse(LConf.get().disguisesCantDisguiseInOwnTerritory));
    			e.setCancelled(true);
    		}
    	}
   	}
}
