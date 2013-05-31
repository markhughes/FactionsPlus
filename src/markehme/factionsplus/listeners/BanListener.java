package markehme.factionsplus.listeners;

import java.io.File;

import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;
import markehme.factionsplus.references.FPP;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.massivecraft.factions.event.FactionsEventMembershipChange;
import com.massivecraft.factions.event.FactionsEventMembershipChange.MembershipChangeReason;

public class BanListener implements Listener {
	
	@EventHandler
	public void onFactionsMembershipChangeEvent(FactionsEventMembershipChange event) {
		if(event.isCancelled()) {
			return;
		}
		
		if(event.getReason() == MembershipChangeReason.JOIN) {
			
			File banFile = new File(Config.folderFBans, event.getUPlayer().getFactionId() + "." + event.getUPlayer().getName().toLowerCase());
			
			if(banFile.exists()) {
				if(Utilities.isLeader(event.getUPlayer())) {
					if(banFile.delete()) {
						FPP.info("Removed old ban file from previous faction for user " +event.getUPlayer().getName()+ " of Faction id " + event.getUPlayer().getFactionId());
					}
				} else {
					event.getUPlayer().msg("You can't join this Faction as you have been banned!");
					event.setCancelled(true);
					event.getUPlayer().leave();
				}
	
				return;
			}
		}


	}
	

}
