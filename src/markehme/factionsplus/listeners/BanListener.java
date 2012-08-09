package markehme.factionsplus.listeners;

import java.io.File;

import markehme.factionsplus.config.*;

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
			event.setCancelled(true);
			event.getFPlayer().leave(false);//XXX: this doesn't seem to be needed, I guess unless that faction was deleted and 
			//then you tried to create it which causes join event which cannot be cancelled, but you were still banned in that faction
			//FIXME: maybe delete the ban file if creator created the faction in which he was banned? meh, so many unhandled cases
			return;
		}


	}
}
