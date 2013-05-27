package markehme.factionsplus.listeners;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusRules;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.FactionsEventMembershipChange;
import com.massivecraft.factions.event.FactionsEventMembershipChange.MembershipChangeReason;
import com.massivecraft.mcore.ps.PS;

public class CoreListener implements Listener{
	public static Server fp;
	
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if(event.isCancelled()) {
			return;
		}

		Player player = event.getPlayer();
		
		Faction factionHere = BoardColls.get().getFactionAt( PS.valueOf( player.getLocation() ) );
		
		// TODO: Cache commands, refresh them on reload/restart
		
		if( Utilities.isWarZone( factionHere ) ) {

			if (!player.isOp()) {
				BufferedReader buff=null;
				try {
					buff = new BufferedReader(new FileReader(Config.fileDisableInWarzone));

					String filterRow = null;
					while ((filterRow = buff.readLine()) != null) {
						if ((event.getMessage().equalsIgnoreCase(filterRow)) || (event.getMessage().toLowerCase().startsWith(filterRow + " "))) {
							event.setCancelled(true);
							player.sendMessage("You can't use that command in a WarZone!");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if (null != buff) {
						try {
							buff.close();
						} catch ( IOException e ) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onFactionsMembershipChange(FactionsEventMembershipChange event) {
		if(event.getReason() == MembershipChangeReason.JOIN) {
			if(Config._rules.onFirstJoinFactionShowRules._) {
				if ( new File(Config.folderFRules+File.separator+event.getUPlayer().getFactionId()+".rules").exists() ) {
					FactionsPlusRules.sendRulesToPlayer( event.getUPlayer() );
				}
			}
		}
		
		if(event.getReason() == MembershipChangeReason.LEAVE) { 
			Faction faction = event.getUPlayer().getFaction();
			if (faction.getUPlayers().size() == 1) {
				// Last player, so remove all data
				removeFPData(faction);
			}
		}
		
		if(event.getReason() == MembershipChangeReason.DISBAND) {
			// Cleanup leftover data
			removeFPData( event.getUPlayer().getFaction() );
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		
		final Player currentPlayer = event.getEntity();
		
		UPlayer currentFPlayer = UPlayer.get(currentPlayer);
		
		if(Utilities.isWarZone(BoardColls.get().getFactionAt(PS.valueOf(currentPlayer.getLocation())))) {
			
			if(!FactionsPlus.permission.has(currentPlayer, "factionsplus.keepItemsOnDeathInWarZone")) {
				return;
			} else {
				currentPlayer.sendMessage(ChatColor.RED + "You died in the WarZone, so you get to keep your items.");
			}
			
			final ItemStack[] playersArmor = currentPlayer.getInventory().getArmorContents();
			final ItemStack[] playersInventory = currentPlayer.getInventory().getContents();
			
			// EntityDamageEvent damangeEvent = currentPlayer.getLastDamageCause();
			
			// In the future - maybe only specific death events? e.g. maybe only by mobs/players
			// not from fall damage or sucide. -- configurable of course 
			
			// Players current armor 
			
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(fp.getPluginManager().getPlugin("FactionsPlus"), new Runnable() {
				@Override
				public void run() {
					currentPlayer.getInventory().setArmorContents(playersArmor);
				}
	
			});
	
			for (ItemStack is : playersArmor) {
				event.getDrops().remove(is);
			}
			
			// Players Experience
			event.setDroppedExp(0);
	
			for (int i = 0; i < playersInventory.length; i++) {
				// drop nothing!
				event.getDrops().remove(playersInventory[i]);
	
			}
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(fp.getPluginManager().getPlugin("FactionsPlus"), new Runnable() {
	
				@Override
				public void run() {
					currentPlayer.getInventory().setContents(playersInventory);
				}
	
			});
		}

	}
	
	
	private final void removeFPData( Faction forFaction ) {
		// Annoucements
		File tempFile = new File( Config.folderAnnouncements, forFaction.getId() );
		if ( tempFile.exists() ) {
			tempFile.delete();
		}
		tempFile = null;
		
		// Bans
		File tempDir = Config.folderFBans;
		if ( tempDir.isDirectory() ) {
			for ( File file : tempDir.listFiles() ) {
				if ( file.getName().startsWith( forFaction.getId() + "." ) ) {
					file.delete();
				}
			}
		}
		tempDir = null;
		
		// Rules
		tempFile = new File( Config.folderFRules, forFaction.getId() );
		if ( tempFile.exists() ) {
			tempFile.delete();
		}
		tempFile = null;
		
		// Jailed Players and Jail locations
		tempDir = Config.folderJails;
		if ( tempDir.isDirectory() ) {
			for ( File file : tempDir.listFiles() ) {
				if ( file.getName().startsWith( "jaildata." + forFaction.getId() + "." ) ) {
					file.delete();
				} else
					if ( file.getName().equals( "loc." + forFaction.getId() ) ) {
						file.delete();
					}
			}
		}
		
		// Warps
		tempFile = new File( Config.folderWarps, forFaction.getId() );
		if ( tempFile.exists() ) {
			tempFile.delete();
		}
		tempFile = null;
	}
	

}
