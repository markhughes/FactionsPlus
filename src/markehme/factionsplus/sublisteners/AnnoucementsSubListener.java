package markehme.factionsplus.sublisteners;

import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.MCore.FactionData;
import markehme.factionsplus.MCore.FactionDataColls;
import markehme.factionsplus.MCore.LConf;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;

public class AnnoucementsSubListener {
	
	/**
	 * Shows the announcement (if one exists, and is configured so) to a player on login
	 * 
	 * @param event
	 * @return
	 */
	public PlayerJoinEvent playerJoinEvent(PlayerJoinEvent event) {
		UPlayer uPlayer = UPlayer.get(event.getPlayer());
		
		FactionData fData = FactionDataColls.get().getForUniverse(uPlayer.getUniverse()).get(uPlayer.getFactionId());
		
		if(fData == null) return event;
		if(fData.announcement == null) return event;
		if(fData.announcer == null) fData.announcer = "someone"; 
		
		if(FPUConf.get(uPlayer.getUniverse()).showAnnouncement.get("onlogin")) {
			uPlayer.msg(Txt.parse(LConf.get().announcementNotify, fData.announcer, fData.announcement));
		}
		
		return event;
		
	}
	
	/**
	 * Shows the announcement (if one exists, and is configured so) to a player on territory enter
	 *  
	 * @param event
	 * @return
	 */
	public PlayerMoveEvent playerMoveEvent(PlayerMoveEvent event) {
		if(event.getFrom().equals(event.getTo())) return event;
		
		Player player = event.getPlayer();
		UPlayer me = UPlayer.get(player);
		
		// If the configuration wants to show it on territory enter
		if(FPUConf.get(me.getUniverse()).showAnnouncement.get("onterritoryenter")) {
			Faction factionHere = BoardColls.get().getFactionAt(PS.valueOf(event.getTo()));
			
			if(BoardColls.get().getFactionAt(PS.valueOf(event.getFrom())).getId() != BoardColls.get().getFactionAt(PS.valueOf(event.getTo())).getId()) {
				
				// Make sure it is our faction land, of course 
				if(factionHere.getId().equals(me.getFactionId())) {
					FactionData fData = FactionDataColls.get().getForUniverse(me.getUniverse()).get(me.getFactionId());
					
					if(fData == null) return event;
					if(fData.announcement == null) return event;
					if(fData.announcer == null) fData.announcer = "someone"; 
					
					me.msg(Txt.parse(LConf.get().announcementNotify, fData.announcer, fData.announcement));
					
					return event;
				}
			}
		}
		
		return event;
	}
}
