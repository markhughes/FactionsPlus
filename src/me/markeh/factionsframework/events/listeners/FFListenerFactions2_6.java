package me.markeh.factionsframework.events.listeners;

import me.markeh.factionsframework.FactionsFramework;
import me.markeh.factionsframework.events.FactionCreateEvent;
import me.markeh.factionsframework.events.FactionDisbandEvent;
import me.markeh.factionsframework.events.FactionJoinEvent;
import me.markeh.factionsframework.events.FactionRenameEvent;
import me.markeh.factionsframework.events.LandChangeEvent;
import me.markeh.factionsframework.events.LandChangeEvent.ChangeType;
import me.markeh.factionsframework.faction.Factions;
import me.markeh.factionsframework.objs.FPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.massivecraft.factions.event.EventFactionsChunkChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;

public class FFListenerFactions2_6 implements Listener {

	@EventHandler
	public void onFactionCreate(com.massivecraft.factions.event.EventFactionsCreate factionsEvent) {
		try {
			FactionCreateEvent event = new FactionCreateEvent(
				factionsEvent.getFactionName(),
				FPlayer.get(
					(Player) factionsEvent.getClass().getMethod("getUSender").invoke(this).getClass().getMethod("getPlayer").invoke(this)
				)
			); 
			
			Bukkit.getServer().getPluginManager().callEvent(event);
				
			if(event.isCancelled()) {
				factionsEvent.setCancelled(true);
			}
			
		} catch (Exception e) {
			FactionsFramework.get().logError(e);
		}
	}
	
	@EventHandler
	public void onLandChange(EventFactionsChunkChange factionsEvent) {
		ChangeType changeType = ChangeType.Unclaim;
		
		if ( ! factionsEvent.getNewFaction().isNone()) {
			changeType = ChangeType.Claim;
		}
				
		try {
			LandChangeEvent event = new LandChangeEvent(
				Factions.get().getFactionAt(factionsEvent.getChunk().asBukkitLocation()).getID(),
				factionsEvent.getNewFaction().getId(),
				(Player) factionsEvent.getClass().getMethod("getUSender").invoke(this).getClass().getMethod("getPlayer").invoke(this),
				factionsEvent.getChunk().asBukkitChunk(),
				changeType
			);
			
			Bukkit.getServer().getPluginManager().callEvent(event);
			
			// if any of the chunks get canceled on, we will stop it all 
			if(event.isCancelled()) {
				factionsEvent.setCancelled(true);
				return;
			}
		} catch (Exception e) {
			FactionsFramework.get().logError(e);
		}
		
	}
	
	@EventHandler
	public void onFactionJoin(com.massivecraft.factions.event.EventFactionsMembershipChange factionsEvent) {
		// TODO: Move to FactionMembershipChangeEvent 
		
		if (factionsEvent.getReason() == MembershipChangeReason.JOIN) {
			try {
				FactionJoinEvent event = new FactionJoinEvent(
					(String) factionsEvent.getClass().getMethod("getUSender").invoke(this).getClass().getMethod("getFactionId").invoke(this),
					factionsEvent.getNewFaction().getId(),
					FPlayer.get((Player) factionsEvent.getClass().getMethod("getUSender").invoke(this).getClass().getMethod("getPlayer").invoke(this))
				);
				
				Bukkit.getServer().getPluginManager().callEvent(event);
				
				if(event.isCancelled()) {
					factionsEvent.setCancelled(true);
				}
			} catch (Exception e) {
				FactionsFramework.get().logError(e);
			}
		}
	}
	
	@EventHandler
	public void onFactionDisband(com.massivecraft.factions.event.EventFactionsDisband factionsEvent) {
		try { 
			FactionDisbandEvent event = new FactionDisbandEvent(
				factionsEvent.getFactionId(),
				FPlayer.get((Player) factionsEvent.getClass().getMethod("getUSender").invoke(this).getClass().getMethod("getPlayer").invoke(this))
			);
			
			Bukkit.getServer().getPluginManager().callEvent(event);
			
			if(event.isCancelled()) {
				factionsEvent.setCancelled(true);
			}
		} catch (Exception e) {
			FactionsFramework.get().logError(e);
		}
	}
	
	@EventHandler
	public void onFactionRename(com.massivecraft.factions.event.EventFactionsNameChange factionsEvent) {
		
		try {
			FactionRenameEvent event = new FactionRenameEvent(
				factionsEvent.getFaction().getId(),
				FPlayer.get((Player) factionsEvent.getClass().getMethod("getUSender").invoke(this).getClass().getMethod("getPlayer").invoke(this)),
				factionsEvent.getFaction().getName(),
				factionsEvent.getNewName()
			);
			
			Bukkit.getServer().getPluginManager().callEvent(event);
			
			if(event.isCancelled()) factionsEvent.setCancelled(true);
		} catch (Exception e) {
			FactionsFramework.get().logError(e);
		}
	}
}
