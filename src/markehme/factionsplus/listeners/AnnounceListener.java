package markehme.factionsplus.listeners;

import markehme.factionsplus.MCore.FactionData;
import markehme.factionsplus.MCore.FactionDataColls;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.MCore.FPUConf;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;


public class AnnounceListener implements Listener {
	
	/**
	 * Show the announcement (if they have a faction, and it exists) when a player
	 * joins the server.
	 * @param event
	 */
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		if(!FPUConf.get(UPlayer.get(event.getPlayer()).getUniverse()).enabled) return;
		if(!FPUConf.get(UPlayer.get(event.getPlayer()).getUniverse()).announcementsEnabled) return;
		
		Player player = event.getPlayer();
		UPlayer uPlayer = UPlayer.get(player);
		
		FactionData fData = FactionDataColls.get().getForUniverse(uPlayer.getUniverse()).get(uPlayer.getFactionId());
		
		if(fData == null) return;
		if(fData.announcement == null) return;
		
		if(FPUConf.get(uPlayer.getUniverse()).showAnnouncement.get("onlogin")) {
			uPlayer.msg(Txt.parse(LConf.get().announcementNotify, fData.announcement));
		}
	}
	
	/**
	 * Show the announcement  (if they have a faction, and it exists) when a player
	 * enters their faction land.
	 * @param event
	 */
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.getFrom().equals(event.getTo())) return;
		
		if(!FPUConf.get(UPlayer.get(event.getPlayer()).getUniverse()).enabled) return;
		if(!FPUConf.get(UPlayer.get(event.getPlayer()).getUniverse()).announcementsEnabled) return;
		
		Player player = event.getPlayer();
		UPlayer me = UPlayer.get(player);
		
		// If the configuration wants to show it on territory enter
		if(FPUConf.get(me.getUniverse()).showAnnouncement.get("onterritoryenter")) {
			Faction factionHere = BoardColls.get().getFactionAt(PS.valueOf(event.getTo()));
			
			if(BoardColls.get().getFactionAt(PS.valueOf(event.getFrom())).getId() != BoardColls.get().getFactionAt(PS.valueOf(event.getTo())).getId()) {
				
				// Make sure it is our faction land, of course 
				if(factionHere.getId().equals(me.getFactionId())) {
					FactionData fData = FactionDataColls.get().getForUniverse(me.getUniverse()).get(me.getFactionId());
					
					if(fData == null) return;
					if(fData.announcement == null) return;
					
					me.msg(Txt.parse(LConf.get().announcementNotify, fData.announcement));
				}
			}
		}
	}
}
