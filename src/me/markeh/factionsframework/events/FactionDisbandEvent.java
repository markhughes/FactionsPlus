package me.markeh.factionsframework.events;

import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.faction.Factions;
import me.markeh.factionsframework.objs.FPlayer;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FactionDisbandEvent extends Event implements Cancellable {
	
	// ------------------------------
	//  Fields
	// ------------------------------
	
	private static final HandlerList handlers = new HandlerList();
	private final String factionID;
	private FPlayer player;
	
	private Boolean cancelled = false;
	
	// ------------------------------
	//  Constructor 
	// ------------------------------
	
	public FactionDisbandEvent(String factionID, FPlayer fPlayer) {
		this.factionID = factionID;
		this.player = fPlayer;
	}
	
	// ------------------------------
	//  Methods
	// ------------------------------
	
	// Gets the Faction that has just been created 
	public String getFactionID() { return this.factionID; }
	public FPlayer getFPlayer() { return this.player; }
	public Faction getFaction() { return Factions.get().getFactionById(this.factionID); }
	
	// Cancel Stuff...	
	public boolean isCancelled() { return this.cancelled; }
	public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
	
	// Handler stuff...
	public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
}
