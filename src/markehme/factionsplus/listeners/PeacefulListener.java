package markehme.factionsplus.listeners;

import markehme.factionsplus.Utilities;
import markehme.factionsplus.MCore.FPUConf;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.massivecraft.factions.event.FactionsEventMembershipChange;
import com.massivecraft.factions.event.FactionsEventMembershipChange.MembershipChangeReason;

public class PeacefulListener implements Listener{
	@EventHandler
	public void onFPlayerJoinEvent(FactionsEventMembershipChange event) {
		if(event.getReason() != MembershipChangeReason.JOIN) return;
		if(!FPUConf.get(event.getUSender().getUniverse()).enabled) return;

		if(!FPUConf.get(event.getUSender().getUniverse()).enablePeacefulBoost) return;
		
		double boostValue = FPUConf.get(event.getUSender().getUniverse()).peacefulPowerBoost;
		
		if(boostValue> 0 && Utilities.isPeaceful( event.getUSender().getFaction() )) {
			Utilities.addPower(event.getUSender(),boostValue);
		}
	}
}
