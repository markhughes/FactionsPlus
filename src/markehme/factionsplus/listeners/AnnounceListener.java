package markehme.factionsplus.listeners;

import java.io.File;

import markehme.factionsplus.*;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;

public class AnnounceListener implements Listener{
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		FPlayer me = FPlayers.i.get(player);

		if(Config.config.getBoolean(Config.str_showLastAnnounceOnLogin)) {
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
		if(Config.config.getBoolean(Config.str_showLastAnnounceOnLandEnter)) {
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
