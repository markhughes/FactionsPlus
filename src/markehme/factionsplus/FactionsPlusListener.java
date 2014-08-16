package markehme.factionsplus;

import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.sublisteners.*;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerFishEvent;

import com.massivecraft.factions.entity.UConf;
import com.massivecraft.factions.entity.UPlayer;

/**
 * Because of the nature of Factions, different worlds may or may not use features. So therefore, we create
 * our own sub-listeners, that we only call if in that world it is needed.
 * 
 * @author MarkehMe <mark@markeh.me>
 *
 */
public class FactionsPlusListener implements Listener {
	
	AnimalDamageSubListener animalDamageSubListener = new AnimalDamageSubListener();
	
	/**
	 * isEnabled ensures Factions and FactionsPlus are enabled in that world.
	 * 
	 * @param oid
	 * @return
	 */
	private boolean isEnabled(Object oid) {
		if(!UConf.get(oid).enabled || !FPUConf.get(oid).enabled) return false;		
		
		return true;
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void playerFishEvent(PlayerFishEvent event) {
		if(!isEnabled(UPlayer.get(event.getPlayer()).getUniverse())) return;
		
		// Animal Damage Events
		event = animalDamageSubListener.playerFishEvent(event);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if(!isEnabled(event.getDamager().getWorld())) return;
		
		// Animal Damage Events
		event = animalDamageSubListener.entityDamageByEntityEvent(event);
	}
}
