package markehme.factionsplus.listeners;

import java.io.File;

import markehme.factionsplus.FactionsPlusPlugin;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.massivecraft.factions.event.FPlayerJoinEvent;

public class BanListener implements Listener{
	@EventHandler
	public void onFPlayerJoinEvent(FPlayerJoinEvent event) {
		if(event.isCancelled()) {
			return;
		}
		File banFile = new File(Config.folderFBans, event.getFaction().getId() + "." + event.getFPlayer().getName().toLowerCase());
		
		if(banFile.exists()) {
			if(Utilities.isLeader(event.getFPlayer())) {
				if(banFile.delete()) {
					FactionsPlusPlugin.info("Removed old ban file from previous faction for user " +event.getFPlayer().getName()+ " of Faction id " + event.getFaction().getId());
				}
			} else {
				event.getFPlayer().msg("You can't join this Faction as you have been banned!");
				event.setCancelled(true);
				event.getFPlayer().leave(false);
			}

			return;
		}


	}
}
