package markehme.factionsplus.sublisteners;

import markehme.factionsplus.Utilities;
import markehme.factionsplus.MCore.FPUConf;

import com.massivecraft.factions.FFlag;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;

public class PeacefulSubListener {
	
	public EventFactionsMembershipChange eventFactionsMembershipChange(EventFactionsMembershipChange event) {
		if(event.getReason() != MembershipChangeReason.JOIN) return event;
		
		double boostValue = FPUConf.get(event.getUSender().getUniverse()).peacefulPowerBoost;
		
		if(boostValue> 0 && (event.getUSender().getFaction().getFlag(FFlag.PEACEFUL))) {
			Utilities.addPower(event.getUSender(), boostValue);
		}
		
		return event;
	}
}
