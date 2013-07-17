package markehme.factionsplus.listeners;

import java.io.File;

import markehme.factionsplus.FactionsPlusJail;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
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

import com.massivecraft.factions.event.FactionsEventMembershipChange;
import com.massivecraft.factions.event.FactionsEventMembershipChange.MembershipChangeReason;

public class JailListener implements Listener{
	@EventHandler
	public void onFPlayerJoinEvent(FactionsEventMembershipChange event) {
		if(event.isCancelled()) {
			return;
		}
		
		if( event.getReason() == MembershipChangeReason.JOIN && Utilities.isJailed( event.getUPlayer().getPlayer() ) ) {
			
			// If player is still jailed, SEND THEM TO THE BRIG!
			event.getUPlayer().getPlayer().teleport(FactionsPlusJail.getJailLocation(event.getUPlayer().getPlayer()));
		}
	}
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();

		if (Utilities.isJailed(player)) {
			event.setRespawnLocation(FactionsPlusJail.getJailLocation(player));
			player.sendMessage("You're currently in Jail!");
		}
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (Utilities.isJailed(player)) {
			player.teleport(FactionsPlusJail.getJailLocation(player));
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if(event.isCancelled()) {
			return;
		}

		Player player = event.getPlayer();

		if(Utilities.isJailed(player)) {
			event.setCancelled(true);
			player.sendMessage("You're currently in Jail! You can't do that!");
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if(event.isCancelled()) {
			return;
		}

		Player player = event.getPlayer();

		if(Utilities.isJailed(player)) {
			event.setCancelled(true);
			player.sendMessage("You're currently in Jail! You can't do that!");
		}

	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		if(event.isCancelled()) {
			return;
		}

		Player player = event.getPlayer();

		if(Utilities.isJailed(player)) {
			event.setCancelled(true);
			player.sendMessage("You're currently in Jail! You can't do that!");
		}

	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event) {
		if(event.isCancelled()) {
			return;
		}
		Player playa = event.getPlayer();
		if ( (Utilities.isJailed(playa)) && (Config._jails.denyMovementWhileJailed._) ) {
			
			Location jailLocation = FactionsPlusJail.getJailLocation(playa);
			if (Utilities.isJustLookingAround(event.getFrom(),jailLocation)) {
				//allow looking around
				return;
			}else {
				//but don't allow moving out
				jailLocation=Utilities.setLocationExceptEye(event.getFrom(),jailLocation);
				event.setTo(jailLocation);
				event.setCancelled( true );
			}
		
			return;
		}
		
	}

	
	@EventHandler(priority = EventPriority.LOWEST)//aka execute before all others that have higher prios
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		if (Utilities.isJailed(player )){
			Location jailLocation = FactionsPlusJail.getJailLocation(player);
			if (!Utilities.isJustLookingAround(jailLocation , event.getTo() )) {
				event.setCancelled( true );		
				jailLocation=Utilities.setLocationExceptEye(event.getFrom(),jailLocation);
				event.setTo(jailLocation);//just in case others want to ignore the cancelled
				player.sendMessage( ChatColor.RED+"You may not teleport away while in faction jail!");
//						+jailLocation+" now="+event.getFrom() );
				return;
			}else{
				//this is reached only when PlayerMoveEvent is NOT cancelled but it's set to move to certain location
//				player.sendMessage("allowed");
			}
			
		}
	}
	
	
	@EventHandler
	public void onFPlayerLeaveEvent(FactionsEventMembershipChange event) {
		if( event.isCancelled() || event.getReason() != MembershipChangeReason.LEAVE ) {
			return;
		}
		
		if(Config._jails.removeOwnJailDataWhenLeavingFaction._) {
			
			File jailDataFile = new File(Config.folderJails,"jaildata." + event.getUPlayer().getFactionId() + "." + event.getUPlayer().getName());
			
			if( jailDataFile.exists() ) {
				FactionsPlusJail.removeFromJail( event.getUPlayer().getName(), event.getUPlayer(), true);
			}
			
		}
		
		return;
	}
}
