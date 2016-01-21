package me.markeh.factionsframework.events;

import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.factionsmanager.FactionsManager;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LandChangeEvent extends Event implements Cancellable {

	// ------------------------------
	//  Enum
	// ------------------------------
	
	public enum ChangeType { Claim, Unclaim }
	
	// ------------------------------
	//  Fields
	// ------------------------------
	
	private static final HandlerList handlers = new HandlerList();
	private final Faction oldFaction;
	private final Faction toFaction;
	private final ChangeType changeType;
	private final Player player;
	private final Chunk chunk;
	
	private Boolean cancelled = false;
	
	// ------------------------------
	//  Constructor 
	// ------------------------------
	
	public LandChangeEvent(String oldFactionID, String factionToID, Player player, Chunk chunk, ChangeType changeType) {
		this.oldFaction = FactionsManager.get().fetch().getFactionById(oldFactionID);
		this.toFaction = FactionsManager.get().fetch().getFactionById(factionToID);
		this.changeType = changeType;
		this.player = player;
		this.chunk = chunk;
	}
	
	// ------------------------------
	//  Methods
	// ------------------------------
	
	// Gets the Faction that has just been created 
	public Faction getNewFaction() { return this.toFaction; }
	public Faction getOldFaction() { return this.oldFaction; }
	public ChangeType getChangeType() { return this.changeType; }
	public Player getPlayer() { return this.player; } 
	public Chunk getChunk () { return this.chunk; }
	
	// Cancel Stuff...	
	public boolean isCancelled() { return this.cancelled; }
	public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
	
	// Handler stuff...
	public HandlerList getHandlers() { return handlers; }

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
