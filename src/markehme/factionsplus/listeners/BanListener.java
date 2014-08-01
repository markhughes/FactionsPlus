package markehme.factionsplus.listeners;

import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.MCore.FactionData;
import markehme.factionsplus.MCore.FactionDataColls;
import markehme.factionsplus.MCore.LConf;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;

public class BanListener implements Listener {
	
	/**
	 * Restricts a player from joining a Faction when they attempt to join.
	 * @param event
	 */
	@EventHandler
	public void onFactionsMembershipChangeEvent(EventFactionsMembershipChange event) {
		if(!FPUConf.get(event.getUPlayer().getUniverse()).enabled) return;
		
		// Only check if they're actually joining
		if(event.getReason() == MembershipChangeReason.JOIN) {
			
			// Fetch the Faction Data 
			FactionData fData = FactionDataColls.get().getForUniverse(event.getUPlayer().getUniverse()).get(event.getNewFaction().getId());
			
			// Check if they're in the list of bannedPlayerIDs
			if(fData.bannedPlayerIDs.containsKey(event.getUPlayer().getPlayer().getUniqueId())) {
				if(event.getUPlayer().getRole() == Rel.LEADER) {
					// If you're joining, but banned, but are a leader - then there is an issue. Remove them from the ban list.
					fData.bannedPlayerIDs.remove(event.getUPlayer().getPlayer().getUniqueId());
				} else {
					// They're banned - let them know, and cancel the join
					event.getUPlayer().msg(LConf.get().banYouAreBanned);
					event.setCancelled(true);
					event.getUPlayer().leave();
				}
			}
		}
	}
}
