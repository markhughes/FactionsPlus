package markehme.factionsplus.listeners;

import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.MCore.LConf;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.util.Txt;

import de.robingrether.idisguise.api.DisguiseEvent;

/**
 * iDisguise Listener
 * 
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
		if(!FPUConf.get(uPlayer.getUniverse()).enabled) return;

    	if(FPUConf.get(uPlayer.getUniverse()).disguiseRemoveIfInEnemyTerritory) {
    		if(uPlayer.isInEnemyTerritory()) {
    			uPlayer.msg(Txt.parse(LConf.get().disguisesCantDisguiseInEnemyTerritory));
    			e.setCancelled(true);
    		}
    	}
    		
    	if(FPUConf.get(uPlayer.getUniverse()).disguiseRemoveIfInOwnTerritory) {
    		if(uPlayer.isInOwnTerritory()) {
    			uPlayer.msg(Txt.parse(LConf.get().disguisesCantDisguiseInOwnTerritory));
    			e.setCancelled(true);
    		}
    	}
	}
}
