package me.markeh.factionsframework.events.listeners;

import me.markeh.factionsframework.events.FactionCreateEvent;
import me.markeh.factionsframework.events.FactionDisbandEvent;
import me.markeh.factionsframework.events.FactionJoinEvent;
import me.markeh.factionsframework.events.FactionRenameEvent;
import me.markeh.factionsframework.events.LandChangeEvent;
import me.markeh.factionsframework.events.LandChangeEvent.ChangeType;
import me.markeh.factionsframework.objs.FPlayer;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.ps.PS;

public class FFListenerFactions2 implements Listener {

	@EventHandler
	public void onFactionCreate(com.massivecraft.factions.event.EventFactionsCreate factionsEvent) {
		FactionCreateEvent event = new FactionCreateEvent(
			factionsEvent.getFactionName(),
			FPlayer.get(factionsEvent.getMSender().getPlayer())
		); 
			
		Bukkit.getServer().getPluginManager().callEvent(event);
			
		if(event.isCancelled()) {
			factionsEvent.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onLandChange(com.massivecraft.factions.event.EventFactionsChunksChange factionsEvent) {
		ChangeType changeType = ChangeType.Unclaim;
		
		if ( ! factionsEvent.getNewFaction().isNone()) {
			changeType = ChangeType.Claim;
		}
				
		
		for(PS key : factionsEvent.getOldChunkFaction().keySet()) {
			LandChangeEvent event = new LandChangeEvent(
				factionsEvent.getOldChunkFaction().get(key).getId(),
				factionsEvent.getNewFaction().getId(),
				factionsEvent.getMSender().getPlayer(),
				key.asBukkitChunk(),
				changeType
			);
			
			Bukkit.getServer().getPluginManager().callEvent(event);
			
			// if any of the chunks get canceled on, we will stop it all 
			if(event.isCancelled()) {
				factionsEvent.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onFactionJoin(com.massivecraft.factions.event.EventFactionsMembershipChange factionsEvent) {
		// TODO: Move to FactionMembershipChangeEvent 
		
		if (factionsEvent.getReason() == MembershipChangeReason.JOIN) {
			FactionJoinEvent event = new FactionJoinEvent(
				factionsEvent.getMPlayer().getFactionId(),
				factionsEvent.getNewFaction().getId(),
				FPlayer.get(factionsEvent.getMPlayer().getPlayer())
			);
			
			Bukkit.getServer().getPluginManager().callEvent(event);
			
			if(event.isCancelled()) {
				factionsEvent.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onFactionDisband(com.massivecraft.factions.event.EventFactionsDisband factionsEvent) {
		FactionDisbandEvent event = new FactionDisbandEvent(
			factionsEvent.getFactionId(),
			FPlayer.get(factionsEvent.getMSender().getPlayer())
		);
		
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		if(event.isCancelled()) {
			factionsEvent.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onFactionRename(com.massivecraft.factions.event.EventFactionsNameChange factionsEvent) {
		FactionRenameEvent event = new FactionRenameEvent(
				factionsEvent.getFaction().getId(),
				FPlayer.get(factionsEvent.getMSender().getPlayer()),
				factionsEvent.getFaction().getName(),
				factionsEvent.getNewName()
			);
			
			Bukkit.getServer().getPluginManager().callEvent(event);
			
			if(event.isCancelled()) factionsEvent.setCancelled(true);
			
	}
}
