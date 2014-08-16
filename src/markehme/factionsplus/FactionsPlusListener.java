package markehme.factionsplus;

import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.sublisteners.*;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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
	AnnoucementsSubListener annoucementsSubListener = new AnnoucementsSubListener();
	
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
	
	/*
	 * Start all the event handlers 
	 */
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void playerFishEvent(PlayerFishEvent event) {
		if(!isEnabled(UPlayer.get(event.getPlayer()).getUniverse())) return;
		//FPUConf fpuconf = FPUConf.get(UPlayer.get(event.getPlayer()).getUniverse());
		
		// Animal Damage Events
		event = animalDamageSubListener.playerFishEvent(event);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if(!isEnabled(event.getDamager().getWorld())) return;
		//FPUConf fpuconf = FPUConf.get(event.getDamager().getWorld());
		
		// Animal Damage Events
		event = animalDamageSubListener.entityDamageByEntityEvent(event);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void playerJoinEvent(PlayerJoinEvent event) {
		if(!isEnabled(event.getPlayer().getWorld())) return;
		FPUConf fpuconf = FPUConf.get(event.getPlayer().getWorld());
		
		// Announcements Events
		if(fpuconf.announcementsEnabled) event = annoucementsSubListener.playerJoinEvent(event);
	}
	
	public void playerMoveEvent(PlayerMoveEvent event) {
		if(!isEnabled(UPlayer.get(event.getPlayer()).getUniverse())) return;
		FPUConf fpuconf = FPUConf.get(UPlayer.get(event.getPlayer()).getUniverse());
		
		if(fpuconf.announcementsEnabled) event = annoucementsSubListener.playerMoveEvent(event);

	}
}
