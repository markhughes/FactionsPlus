package me.markeh.factionsframework.events.listeners;

import me.markeh.factionsframework.events.FactionCreateEvent;
import me.markeh.factionsframework.events.FactionDisbandEvent;
import me.markeh.factionsframework.events.FactionJoinEvent;
import me.markeh.factionsframework.events.LandChangeEvent;
import me.markeh.factionsframework.events.LandChangeEvent.ChangeType;
import me.markeh.factionsframework.objs.FPlayer;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.massivecraft.factions.Board;

public class FFListenerFactions16UUID implements Listener {
	
	@EventHandler
	public void onFactionCreate(com.massivecraft.factions.event.FactionCreateEvent factionsEvent) {
		FactionCreateEvent event = new FactionCreateEvent(
			factionsEvent.getFactionTag(),
			FPlayer.get(factionsEvent.getFPlayer().getPlayer())
		); 
		
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		if(event.isCancelled()) {
			factionsEvent.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onLandClaim(com.massivecraft.factions.event.LandClaimEvent factionsEvent) {
		int locationX = (int) Math.floor(factionsEvent.getLocation().getX());
		int locationZ = (int) Math.floor(factionsEvent.getLocation().getZ());

		LandChangeEvent event = new LandChangeEvent(
			Board.getInstance().getFactionAt(factionsEvent.getLocation()).getId(),
			factionsEvent.getFaction().getId(), 
			factionsEvent.getfPlayer().getPlayer(),
			factionsEvent.getLocation().getWorld().getChunkAt(locationX, locationZ),
			ChangeType.Claim
		);
			
				
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		if(event.isCancelled()) {
			factionsEvent.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onLandUnclaim(com.massivecraft.factions.event.LandUnclaimEvent factionsEvent) {
		int locationX = (int) Math.floor(factionsEvent.getLocation().getX());
		int locationZ = (int) Math.floor(factionsEvent.getLocation().getZ());

		LandChangeEvent event = new LandChangeEvent(
			Board.getInstance().getFactionAt(factionsEvent.getLocation()).getId(),
			factionsEvent.getFaction().getId(), 
			factionsEvent.getfPlayer().getPlayer(),
			factionsEvent.getLocation().getWorld().getChunkAt(locationX, locationZ),
			ChangeType.Unclaim
		);
		
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		if(event.isCancelled()) {
			factionsEvent.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onFactionDisband(com.massivecraft.factions.event.FactionDisbandEvent factionsEvent) {
		FactionDisbandEvent event = new FactionDisbandEvent(
			factionsEvent.getFaction().getId(),
			FPlayer.get(factionsEvent.getPlayer())
		); 
		
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		if(event.isCancelled()) {
			factionsEvent.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onFactionJoin(com.massivecraft.factions.event.FPlayerJoinEvent factionsEvent) {
		// TODO: Move to FactionMembershipChangeEvent 
		
		FactionJoinEvent event = new FactionJoinEvent(
			factionsEvent.getfPlayer().getFactionId(),
			factionsEvent.getFaction().getId(),
			FPlayer.get(factionsEvent.getfPlayer().getPlayer())
		);
		
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		if(event.isCancelled()) {
			factionsEvent.setCancelled(true);
		}
	}
}
