package markehme.factionsplus.listeners;

import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.MCore.LConf;
import me.libraryaddict.disguise.events.DisguiseEvent;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.util.Txt;

/**
 * Lib's Disguises Listener
 * 
 * http://dev.bukkit.org/bukkit-plugins/libs-disguises/
 *
 */
public class LDListener implements Listener {
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerDisguise(DisguiseEvent e){
		if(e.getEntity().getType() != EntityType.PLAYER ) return;

    	UPlayer uPlayer = UPlayer.get((Player) e.getEntity());
    	
		if(!FPUConf.get(uPlayer.getUniverse()).enabled) return;

		if (!uPlayer.hasFaction()) return;
    	
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
