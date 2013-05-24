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

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.event.FPlayerJoinEvent;
import com.massivecraft.factions.event.FPlayerLeaveEvent;
import com.massivecraft.factions.event.FactionDisbandEvent;

public class CoreListener implements Listener{
	public static Server fp;
	
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if(event.isCancelled()) {
			return;
		}

		Player player = event.getPlayer();

		//FPlayer fplayer = FPlayers.i.get(player.getName());

//		if ((event.getMessage().equalsIgnoreCase("/f reload")) || (event.getMessage().toLowerCase().startsWith("/f reload"))) {
//			event.getPlayer().sendMessage("Yo yo, lets reload FactionsPlus? ;)");
//		}this had no effect
		Faction factionHere = Board.getFactionAt(new FLocation(player.getLocation()));
//FIXME: lots to be fixed here: ie. cache those commands from file instead of open/close on every command, and test timestamp to know when to reload for changes, or only when /f reloadfp
		if(Utilities.isWarZone( factionHere)) {

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
	public void onFPlayerJoin(FPlayerJoinEvent event) {
		if(Config._rules.onFirstJoinFactionShowRules._) {
			if ( new File(Config.folderFRules+File.separator+event.getFPlayer().getFactionId()+".rules").exists() ) {
				FactionsPlusRules.sendRulesToPlayer(event.getFPlayer());
			}
		}
		
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onFPlayerLeave(FPlayerLeaveEvent event){
		if (event.isCancelled()){
			return;
		}
		//this is called on /f disband for every player, or on player /f leave  or on autoLeaveOnInactivityRoutine
		//but FPlayer.leave() method is not called on /f disband
		
		Faction faction = event.getFaction();
//		faction.sendMessage( "players: "+faction.getFPlayers().size() );
		if (faction.getFPlayers().size() == 1) {
			//then the last player is about to leave which means faction will get disbanded
			//we then remove all FP data for it
			//NOTE: this won't trigged on /f disband  aka FactionDisbandEvent
			removeFPData(faction);
		}
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onFactionDisband(FactionDisbandEvent event){
		//XXX: Factions doesn't call this event on autodisband faction(ie. when all players auto leave after a time) the data below remains
		//actually this is a good thing, if they want to recreate the faction next time; but a bad thing if someone else recreates it, they
		//can then use the warps (if any)
		//XXX: Factions doesn't call this event when the last person(aka faction admin) leaves the faction via /f leave
		
		// Clean up old files used by faction
		// Announcements, bans, rules, jails, warps, etc
		Faction faction = event.getFaction();
		
		removeFPData(faction);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		
		final Player currentPlayer = event.getEntity();
		
		FPlayer currentFPlayer = FPlayers.i.get(currentPlayer);
		
		if(Utilities.isWarZone(Board.getFactionAt(currentPlayer.getLocation()))) {
			
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
