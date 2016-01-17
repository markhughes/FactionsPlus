package me.markeh.factionsframework.events.listeners;

import me.markeh.factionsframework.objs.FPlayer;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FFListenerGlobal implements Listener {

	// Warmup Kill Events
	
	@EventHandler
	public void killWarmUpOnMove(PlayerMoveEvent event) {
		if (event.getFrom().getX() == event.getTo().getX() &&
			event.getFrom().getY() == event.getTo().getY() &&
			event.getFrom().getZ() == event.getTo().getZ()) return;
		
		FPlayer.get(event.getPlayer()).killWarmUpTasks();
	}
	
	@EventHandler
	public void killWarmUpOnSneak(PlayerToggleSneakEvent event) { FPlayer.get(event.getPlayer()).killWarmUpTasks(); }
	
	@EventHandler
	public void killWarmUpOnDamage(EntityDamageByEntityEvent event) {
		
		if (event.getEntityType() == EntityType.PLAYER) {
			FPlayer.get((Player) event.getEntity()).killWarmUpTasks();
		}
		
		if (event.getDamager().getType() == EntityType.PLAYER) {
			FPlayer.get((Player) event.getDamager()).killWarmUpTasks();
		}
	}

}
