package markehme.factionsplus.listeners;

import java.io.*;

import markehme.factionsplus.config.*;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import com.massivecraft.factions.*;
import com.massivecraft.factions.event.*;

public class CoreListener implements Listener{
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if(event.isCancelled()) {
			return;
		}

		Player player = event.getPlayer();
		String filterRow = null;

		//FPlayer fplayer = FPlayers.i.get(player.getName());

//		if ((event.getMessage().equalsIgnoreCase("/f reload")) || (event.getMessage().toLowerCase().startsWith("/f reload"))) {
//			event.getPlayer().sendMessage("Yo yo, lets reload FactionsPlus? ;)");
//		}this had no effect
		Faction factionHere = Board.getFactionAt(new FLocation(player.getLocation()));
//FIXME: lots to be fixed here: ie. warzone check and cache those commands from file instead of open/close on every command
		if(factionHere.getTag().trim().equalsIgnoreCase("WarZone")) {

			if (!player.isOp()) {
				BufferedReader buff=null;
				try {
					buff = new BufferedReader(new FileReader(Config.fileDisableInWarzone));

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
	public void onFactionDisband(FactionDisbandEvent event){
		// Clean up old files used by faction
		// Announcements, bans, rules, jails, warps, etc
		Faction faction = event.getFaction();
		
		// Annoucements
		File tempFile = new File(Config.folderAnnouncements, faction.getId());
		if(tempFile.exists()){
			tempFile.delete();
		}
		tempFile = null;
		
		// Bans
		File tempDir =Config.folderFBans;
		if(tempDir.isDirectory()){
			for(File file : tempDir.listFiles()){
				if(file.getName().startsWith(faction.getId() + ".")){
					file.delete();
				}
			}
		}
		tempDir = null;
		
		// Rules
		tempFile = new File(Config.folderFRules, faction.getId());
		if(tempFile.exists()){
			tempFile.delete();
		}
		tempFile = null;
		
		// Jailed Players and Jail locations
		tempDir =Config.folderJails;
		if(tempDir.isDirectory()){
			for(File file : tempDir.listFiles()){
				if(file.getName().startsWith("jaildata." + faction.getId() + ".")){
					file.delete();
				} else if (file.getName().equals("loc." + faction.getId())){
					file.delete();
				}
			}
		}
		
		// Warps
		tempFile = new File(Config.folderWarps,  faction.getId());
		if(tempFile.exists()){
			tempFile.delete();
		}
		tempFile = null;
	}

}
