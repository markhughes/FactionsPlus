package markehme.factionsplus.listeners;

import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;

public class PeacefulListener implements Listener{
	@EventHandler
	public void onFPlayerJoinEvent(EventFactionsMembershipChange event) {
		if(event.isCancelled() || event.getReason() != MembershipChangeReason.JOIN) {
			return;
		}
		
		int boostValue = Config._peaceful.powerBoostIfPeaceful._ ;
		
		if(boostValue> 0) {
			if(Utilities.isPeaceful( event.getUSender().getFaction() )) {
				Utilities.addPower(event.getUSender(),boostValue);
			}
		}
	}

}
