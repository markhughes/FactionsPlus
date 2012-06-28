package markehme.factionsplus.listeners;

import java.io.File;

import markehme.factionsplus.*;

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
			event.getFPlayer().msg("You can't join this Faction as you have been banned!");
			event.getFPlayer().leave(true);
			event.setCancelled(true);
			return;
		}


	}
}
