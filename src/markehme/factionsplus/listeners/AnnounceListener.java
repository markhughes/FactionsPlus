package markehme.factionsplus.listeners;

import java.io.File;

import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.massivecraft.factions.entity.Board;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.ps.PSBuilder;
import com.massivecraft.mcore.ps.PSFormat;


public class AnnounceListener implements Listener {
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		UPlayer me = UPlayer.get(player);

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
			UPlayer me = UPlayer.get(player);
			
				Faction factionHere = BoardColls.get().getFactionAt(PS.valueOf(event.getTo()));
			
			if ( BoardColls.get().getFactionAt(PS.valueOf(event.getFrom())) != BoardColls.get().getFactionAt(PS.valueOf(event.getTo())) ) {
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
