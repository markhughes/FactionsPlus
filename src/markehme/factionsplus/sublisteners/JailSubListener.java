package markehme.factionsplus.sublisteners;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import markehme.factionsplus.Utilities;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.MCore.FactionData;
import markehme.factionsplus.MCore.FactionDataColls;
import markehme.factionsplus.MCore.LConf;

import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.util.Txt;

public class JailSubListener {

	public EventFactionsMembershipChange eventFactionsMembershipChange(EventFactionsMembershipChange event) {
		FactionData fData = FactionDataColls.get().getForUniverse(event.getUPlayer().getUniverse()).get(event.getNewFaction());
		if(fData == null) return event;
		
		if(event.getReason() == MembershipChangeReason.JOIN && fData.jailedPlayerIDs.containsKey(event.getUPlayer().getPlayer().getUniqueId().toString()) ) {
			event.getUPlayer().getPlayer().teleport(fData.jailLocation.asBukkitLocation());
			event.getUPlayer().msg(Txt.parse(LConf.get().jailsSpawnedIntoJail));
		} else if(event.getReason() == MembershipChangeReason.LEAVE) {
			if(FPUConf.get(event.getUPlayer().getUniverse()).removeJailDataOnLeave) {
				if(fData.jailedPlayerIDs.containsKey(event.getUPlayer().getPlayer().getUniqueId().toString())) {
					fData.jailedPlayerIDs.remove(event.getUPlayer().getPlayer().getUniqueId().toString());
				}
			}
		}
		
		return event;
	}
	
	public PlayerRespawnEvent playerRespawnEvent(PlayerRespawnEvent event) { 
		UPlayer uPlayer = UPlayer.get(event.getPlayer());
		
		FactionData fData = FactionDataColls.get().getForUniverse(uPlayer.getUniverse()).get(uPlayer.getFaction());
		
		if(fData == null) return event;
		
		if(fData.jailedPlayerIDs.containsKey(uPlayer.getPlayer().getUniqueId().toString())) {
			event.setRespawnLocation(fData.jailLocation.asBukkitLocation());
			uPlayer.msg(Txt.parse(LConf.get().jailsSpawnedIntoJail));
		}
		
		return event;
	}
	
	public PlayerJoinEvent playerJoinEvent(PlayerJoinEvent event) {
		UPlayer uPlayer = UPlayer.get(event.getPlayer());
		
		FactionData fData = FactionDataColls.get().getForUniverse(uPlayer.getUniverse()).get(uPlayer.getFaction());
		
		if(fData == null) return event;

		if(fData.jailedPlayerIDs.containsKey(uPlayer.getPlayer().getUniqueId().toString())) {
			uPlayer.getPlayer().teleport(fData.jailLocation.asBukkitLocation());
		}
		return event;
	}
	
	public BlockBreakEvent blockBreakEvent(BlockBreakEvent event) { 
		UPlayer uPlayer = UPlayer.get(event.getPlayer());
		
		FactionData fData = FactionDataColls.get().getForUniverse(uPlayer.getUniverse()).get(uPlayer.getFaction());
		
		if(fData == null) return event;
		
		if(fData.isJailed(uPlayer)) {
			event.setCancelled(true);
			uPlayer.msg(Txt.parse(LConf.get().jailsCantDoThatInJail));
		}
		
		return event;
	}
	
	public BlockPlaceEvent blockPlaceEvent(BlockPlaceEvent event) {
		UPlayer uPlayer = UPlayer.get(event.getPlayer());
		
		FactionData fData = FactionDataColls.get().getForUniverse(uPlayer.getUniverse()).get(uPlayer.getFaction());
		if(fData == null) return event;
		
		if(fData.isJailed(uPlayer)) {
			event.setCancelled(true);
			uPlayer.msg(Txt.parse(LConf.get().jailsCantDoThatInJail));
		}
		
		return event;
	}
	
	public AsyncPlayerChatEvent asyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		UPlayer uPlayer = UPlayer.get(event.getPlayer());
		
		FactionData fData = FactionDataColls.get().getForUniverse(uPlayer.getUniverse()).get(uPlayer.getFaction());
		if(fData == null) return event;
		
		if(fData.isJailed(uPlayer)) {
			event.setCancelled(true);
			uPlayer.msg(Txt.parse(LConf.get().jailsCantDoThatInJail));
		}
		
		return event;
	}
	
	public PlayerMoveEvent playerMoveEvent(PlayerMoveEvent event) { 
		UPlayer uPlayer = UPlayer.get(event.getPlayer());
		
		FactionData fData = FactionDataColls.get().getForUniverse(uPlayer.getUniverse()).get(uPlayer.getFaction());
		if(fData == null) return event;

		// Allow the player to look around but not move
		if(fData.isJailed(uPlayer) && FPUConf.get(uPlayer.getUniverse()).denyMovementWhileJailed && !Utilities.isJustLookingAround(event.getFrom(), fData.jailLocation.asBukkitLocation())) {
			event.setTo(Utilities.setLocationExceptEye(event.getFrom(), fData.jailLocation.asBukkitLocation()));
		}
		
		return event;
	}
	
	public PlayerTeleportEvent playerTeleportEvent(PlayerTeleportEvent event) {
		UPlayer uPlayer = UPlayer.get(event.getPlayer());
		
		FactionData fData = FactionDataColls.get().getForUniverse(uPlayer.getUniverse()).get(uPlayer.getFaction());
		if(fData == null) return event;
		
		if(fData.isJailed(uPlayer)){
			if(!Utilities.isJustLookingAround(fData.jailLocation.asBukkitLocation(), event.getTo())) {
				event.setCancelled(true);
				
				event.setTo(Utilities.setLocationExceptEye(event.getFrom(), fData.jailLocation.asBukkitLocation()));
				
				// We send an alternative message while they're trying to teleport jailed. 
				uPlayer.msg(Txt.parse(LConf.get().jailsCantTeleportInJail));
				return event;
			}
			
		}
		
		return event;
	}
}
