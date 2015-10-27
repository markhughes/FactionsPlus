package me.markeh.factionsframework.events;

import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.factionsmanager.FactionsManager;
import me.markeh.factionsframework.objs.FPlayer;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

// TODO: Move to FactionMembershipChangeEvent 

public class FactionJoinEvent extends Event implements Cancellable {

	// ------------------------------
	// Fields
	// ------------------------------

	private static final HandlerList handlers = new HandlerList();
	private final Faction fromFaction;
	private final Faction toFaction;
	private final FPlayer player;

	private Boolean cancelled = false;

	// ------------------------------
	// Constructor
	// ------------------------------

	public FactionJoinEvent(String factionFromID, String factionToID,
			FPlayer player) {
		this.fromFaction = FactionsManager.get().fetch()
				.getFactionByID(factionFromID);
		this.toFaction = FactionsManager.get().fetch()
				.getFactionByID(factionToID);
		this.player = player;
	}

	// ------------------------------
	// Methods
	// ------------------------------

	// Gets the Faction that has just been created
	public Faction getNewFaction() {
		return this.toFaction;
	}

	public Faction getFromFaction() {
		return this.fromFaction;
	}

	public FPlayer getFPlayer() {
		return this.player;
	}

	// Cancel Stuff...
	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	// Handler stuff...
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
