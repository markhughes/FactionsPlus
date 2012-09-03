package markehme.factionsplus.listeners;

import java.io.*;

import markehme.factionsplus.*;
import markehme.factionsplus.config.*;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

import com.massivecraft.factions.*;

public class AnnounceListener implements Listener{
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		FPlayer me = FPlayers.i.get(player);

		if(Config._announce.showLastAnnounceOnLogin._) {
			File fAF = new File(Config.folderAnnouncements, me.getFactionId());
			if(fAF.exists()) {
				try {
					event.getPlayer().sendMessage(Utilities.readFileAsString(fAF));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerMove(PlayerMoveEvent event) {
		if(event.isCancelled()) {
			return;
		}
		if(Config._announce.showLastAnnounceOnLandEnter._) {
			if (event.getFrom().equals(event.getTo())) return;

			Player player = event.getPlayer();
			FPlayer me = FPlayers.i.get(player);
			Faction factionHere = Board.getFactionAt(new FLocation(event.getTo()));

			if (Board.getFactionAt(new FLocation(event.getFrom())) != Board.getFactionAt(new FLocation(event.getTo()))) {
				if(factionHere.getId().equals(me.getFactionId())) {
					File fAF=new File(Config.folderAnnouncements, me.getFactionId());
					if(fAF.exists()) {
						try {
							event.getPlayer().sendMessage(Utilities.readFileAsString(fAF));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

}
