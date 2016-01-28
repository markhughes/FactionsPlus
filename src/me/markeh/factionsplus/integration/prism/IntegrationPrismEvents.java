package me.markeh.factionsplus.integration.prism;

import me.botsko.prism.Prism;
import me.botsko.prism.actionlibs.ActionType;
import me.botsko.prism.events.PrismCustomPlayerActionEvent;
import me.botsko.prism.exceptions.InvalidActionException;
import me.markeh.factionsframework.events.FactionDisbandEvent;
import me.markeh.factionsframework.events.FactionJoinEvent;
import me.markeh.factionsframework.events.LandChangeEvent;
import me.markeh.factionsframework.events.LandChangeEvent.ChangeType;
import me.markeh.factionsplus.FactionsPlus;
import me.markeh.factionsplus.integration.IntegrationEvents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class IntegrationPrismEvents  extends IntegrationEvents implements Listener {
	
	public IntegrationPrismEvents() {
		try {
			Prism.getActionRegistry().registerCustomAction(FactionsPlus.get(), new ActionType("factions-p-claim", false, false, false, "FactionLandClaim", "land claimed"));
			Prism.getActionRegistry().registerCustomAction(FactionsPlus.get(), new ActionType("factions-p-unclaim", false, false, false, "FactionLandUnclaim", "land unclaimed"));
			Prism.getActionRegistry().registerCustomAction(FactionsPlus.get(), new ActionType("factions-p-create", false, false, false, "FactionCreate", "faction created"));
			Prism.getActionRegistry().registerCustomAction(FactionsPlus.get(), new ActionType("factions-p-disband", false, false, false, "FactionDisband", "faction disbanded"));
			Prism.getActionRegistry().registerCustomAction(FactionsPlus.get(), new ActionType("factions-p-join", false, false, false, "FactionJoin", "join faction"));

		} catch (InvalidActionException e) {
			FactionsPlus.get().logError(e);
		}
	}
	
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
			PrismCustomPlayerActionEvent prismEvent = new PrismCustomPlayerActionEvent(FactionsPlus.get(), "factions-p-unclaim", event.getPlayer(), null);
	        FactionsPlus.get().getServer().getPluginManager().callEvent(prismEvent);
		}
		if (event.getChangeType() == ChangeType.Claim) {
			PrismCustomPlayerActionEvent prismEvent = new PrismCustomPlayerActionEvent(FactionsPlus.get(), "factions-p-claim", event.getPlayer(), null);
	        FactionsPlus.get().getServer().getPluginManager().callEvent(prismEvent);
		}
	}
	
	@EventHandler
	public void onJoinFaction(FactionJoinEvent event) {
		PrismCustomPlayerActionEvent prismEvent = new PrismCustomPlayerActionEvent(FactionsPlus.get(), "factions-p-join", event.getFPlayer().getPlayer(), null);
        FactionsPlus.get().getServer().getPluginManager().callEvent(prismEvent);
	}
	
	@EventHandler
	public void onDisbandFaction(FactionDisbandEvent event) {
		PrismCustomPlayerActionEvent prismEvent = new PrismCustomPlayerActionEvent(FactionsPlus.get(), "factions-p-disband", event.getFPlayer().getPlayer(), null);
        FactionsPlus.get().getServer().getPluginManager().callEvent(prismEvent);
	}
}