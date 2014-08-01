package markehme.factionsplus.listeners;

import markehme.factionsplus.Utilities;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.MCore.FactionData;
import markehme.factionsplus.MCore.FactionDataColls;
import markehme.factionsplus.MCore.LConf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.util.Txt;

/**
 * The JailListener adds features for FactionsPlus jails.
 *
 */
public class JailListener implements Listener{
	
	/**
	 * Send a player to jail if they join a Faction they're in a jail of
	 * 
	 */
	@EventHandler
	public void onPlayerJoinFactionEvent(EventFactionsMembershipChange event) {
		if(!FPUConf.get(event.getUPlayer().getUniverse()).enabled) return; // universe support 

		FactionData fData = FactionDataColls.get().getForUniverse(event.getUPlayer().getUniverse()).get(event.getNewFaction());
		
		if(fData == null) return;
		
		if(event.getReason() == MembershipChangeReason.JOIN && fData.jailedPlayerIDs.containsKey(event.getUPlayer().getPlayer().getUniqueId().toString()) ) {
			event.getUPlayer().getPlayer().teleport(fData.jailLocation.asBukkitLocation());
			event.getUPlayer().msg(Txt.parse(LConf.get().jailsSpawnedIntoJail));
		}
	}
	
	/**
	 * Send a player to jail if they respawn while jailed
	 * 
	 */
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		UPlayer uPlayer = UPlayer.get(event.getPlayer());

		if(!FPUConf.get(uPlayer.getUniverse()).enabled) return; // universe support
		
		FactionData fData = FactionDataColls.get().getForUniverse(uPlayer.getUniverse()).get(uPlayer.getFaction());
		
		if(fData == null) return;
		
		if(fData.jailedPlayerIDs.containsKey(uPlayer.getPlayer().getUniqueId().toString())) {
			event.setRespawnLocation(fData.jailLocation.asBukkitLocation());
			uPlayer.msg(Txt.parse(LConf.get().jailsSpawnedIntoJail));
		}
	}
	
	/**
	 * Send a player to jail if they join while jailed
	 * 
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		UPlayer uPlayer = UPlayer.get(event.getPlayer());

		if(!FPUConf.get(uPlayer.getUniverse()).enabled) return; // universe support

		FactionData fData = FactionDataColls.get().getForUniverse(uPlayer.getUniverse()).get(uPlayer.getFaction());
		
		if(fData == null) return;

		if(fData.jailedPlayerIDs.containsKey(uPlayer.getPlayer().getUniqueId().toString())) {
			uPlayer.getPlayer().teleport(fData.jailLocation.asBukkitLocation());
		}
	}
	
	/**
	 * Disallow block breaking while jailed
	 * 
	 */
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		UPlayer uPlayer = UPlayer.get(event.getPlayer());
		
		if(!FPUConf.get(uPlayer.getUniverse()).enabled) return; // universe support

		FactionData fData = FactionDataColls.get().getForUniverse(uPlayer.getUniverse()).get(uPlayer.getFaction());
		
		if(fData == null) return;
		
		if(fData.isJailed(uPlayer)) {
			event.setCancelled(true);
			uPlayer.msg(Txt.parse(LConf.get().jailsCantDoThatInJail));
		}
	}
	
	/**
	 * Disallow breaking blocks while jailed
	 * 
	 * @param event
	 */
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {

		UPlayer uPlayer = UPlayer.get(event.getPlayer());
		if(!FPUConf.get(uPlayer.getUniverse()).enabled) return; // universe support
		
		FactionData fData = FactionDataColls.get().getForUniverse(uPlayer.getUniverse()).get(uPlayer.getFaction());
		if(fData == null) return;
		
		if(fData.isJailed(uPlayer)) {
			event.setCancelled(true);
			uPlayer.msg(Txt.parse(LConf.get().jailsCantDoThatInJail));
		}
	}
	
	/**
	 * Disallow chat while jailed
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		
		UPlayer uPlayer = UPlayer.get(event.getPlayer());
		if(!FPUConf.get(uPlayer.getUniverse()).enabled) return; // universe support
		if(!FPUConf.get(uPlayer.getUniverse()).denyChatWhileJailed) return; // best feature in 0.7.x, right? 

		FactionData fData = FactionDataColls.get().getForUniverse(uPlayer.getUniverse()).get(uPlayer.getFaction());
		if(fData == null) return;
		
		if(fData.isJailed(uPlayer)) {
			event.setCancelled(true);
			uPlayer.msg(Txt.parse(LConf.get().jailsCantDoThatInJail));
		}

	}
	
	/**
	 * Disallow player movement while jailed
	 * 
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event) {
		
		UPlayer uPlayer = UPlayer.get(event.getPlayer());
		if(!FPUConf.get(uPlayer.getUniverse()).enabled) return; // universe support
		
		FactionData fData = FactionDataColls.get().getForUniverse(uPlayer.getUniverse()).get(uPlayer.getFaction());
		if(fData == null) return;

		// Allow the player to look around but not move
		if(fData.isJailed(uPlayer) && FPUConf.get(uPlayer.getUniverse()).denyMovementWhileJailed && !Utilities.isJustLookingAround(event.getFrom(), fData.jailLocation.asBukkitLocation())) {
			event.setTo(Utilities.setLocationExceptEye(event.getFrom(), fData.jailLocation.asBukkitLocation()));
		}
	}
	
	/**
	 * Disallow player teleport while jailed
	 * 
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		UPlayer uPlayer = UPlayer.get(event.getPlayer());
		if(!FPUConf.get(uPlayer.getUniverse()).enabled) return; // universe support
		
		FactionData fData = FactionDataColls.get().getForUniverse(uPlayer.getUniverse()).get(uPlayer.getFaction());
		if(fData == null) return;
		
		if(fData.isJailed(uPlayer)){
			if(!Utilities.isJustLookingAround(fData.jailLocation.asBukkitLocation(), event.getTo())) {
				event.setCancelled(true);
				
				event.setTo(Utilities.setLocationExceptEye(event.getFrom(), fData.jailLocation.asBukkitLocation()));
				
				// We send an alternative message while they're trying to teleport jailed. 
				uPlayer.msg(Txt.parse(LConf.get().jailsCantTeleportInJail));
				return;
			}
			
		}
	}
	
	/**
	 * Some servers may want to remove jail data when they leave
	 * 
	 */
	@EventHandler
	public void onFPlayerLeaveEvent(EventFactionsMembershipChange event) {
		if(event.isCancelled() || event.getReason() != MembershipChangeReason.LEAVE ) return;
		
		UPlayer uPlayer = event.getUPlayer();
		if(!FPUConf.get(uPlayer.getUniverse()).enabled) return; // universe support
		
		FactionData fData = FactionDataColls.get().getForUniverse(uPlayer.getUniverse()).get(uPlayer.getFaction());
		if(fData == null) return;
		
		if(FPUConf.get(uPlayer.getUniverse()).removeJailDataOnLeave) {
			if(fData.jailedPlayerIDs.containsKey(uPlayer.getPlayer().getUniqueId().toString())) {
				fData.jailedPlayerIDs.remove(uPlayer.getPlayer().getUniqueId().toString());
			}
		}
	}
}
