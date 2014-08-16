package markehme.factionsplus.sublisteners;

import markehme.factionsplus.MCore.FactionData;
import markehme.factionsplus.MCore.FactionDataColls;
import markehme.factionsplus.MCore.LConf;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.util.Txt;

public class BanSubListener {
	
	public EventFactionsMembershipChange eventFactionsMembershipChange(EventFactionsMembershipChange event) {
		if(event.getReason() != MembershipChangeReason.JOIN) return event;
			
		// Fetch the Faction Data 
		FactionData fData = FactionDataColls.get().getForUniverse(event.getUPlayer().getUniverse()).get(event.getNewFaction().getId());
		
		// Check if they're in the list of bannedPlayerIDs
		if(fData.bannedPlayerIDs.containsKey(event.getUPlayer().getPlayer().getUniqueId())) {
			if(event.getUPlayer().getRole() == Rel.LEADER) {
				// If you're joining, but banned, but are a leader - then there is an issue. Remove them from the ban list.
				fData.bannedPlayerIDs.remove(event.getUPlayer().getPlayer().getUniqueId());
			} else {
				// They're banned - let them know, and cancel the join
				event.getUPlayer().msg(Txt.parse(LConf.get().banYouAreBanned));
				event.setCancelled(true);
				event.getUPlayer().leave();
			}
		}
		
		return event;
	}
}
