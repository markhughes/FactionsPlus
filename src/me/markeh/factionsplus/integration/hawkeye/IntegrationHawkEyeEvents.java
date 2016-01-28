package me.markeh.factionsplus.integration.hawkeye;

import me.markeh.factionsframework.events.FactionDisbandEvent;
import me.markeh.factionsframework.events.FactionJoinEvent;
import me.markeh.factionsframework.events.LandChangeEvent;
import me.markeh.factionsframework.events.LandChangeEvent.ChangeType;
import me.markeh.factionsplus.FactionsPlus;
import me.markeh.factionsplus.integration.IntegrationEvents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import uk.co.oliwali.HawkEye.util.HawkEyeAPI;

public class IntegrationHawkEyeEvents extends IntegrationEvents implements Listener {
	
	@Override
	public void enable() {
		FactionsPlus.get().addListener(this);
	}

	@Override
	public void disable() {
		FactionsPlus.get().removeListener(this);
	}
	
	@EventHandler
	public void onLandUnClaim(LandChangeEvent event) {
		if (event.getChangeType() == ChangeType.Unclaim) {
			HawkEyeAPI.addCustomEntry(FactionsPlus.get(), "Unclaim Land", event.getPlayer(), event.getPlayer().getLocation(), event.getNewFaction().getID());
		}
		if (event.getChangeType() == ChangeType.Claim) {
			HawkEyeAPI.addCustomEntry(FactionsPlus.get(), "Claim Land", event.getPlayer(), event.getPlayer().getLocation(), event.getOldFaction().getID());
		}
	}
	
	@EventHandler
	public void onJoinFaction(FactionJoinEvent event) {
		HawkEyeAPI.addCustomEntry(FactionsPlus.get(), "Join Faction", event.getFPlayer().getPlayer(), event.getFPlayer().getLocation().asBukkitLocation(), event.getNewFaction().getID());
	}
	
	@EventHandler
	public void onDisbandFaction(FactionDisbandEvent event) {
		HawkEyeAPI.addCustomEntry(FactionsPlus.get(), "Disband Faction", event.getFPlayer().getPlayer(), event.getFPlayer().getLocation().asBukkitLocation(), event.getFaction().getID());
	}
}
