package me.markeh.factionsframework.events;

import me.markeh.factionsframework.objs.FPlayer;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FactionCreateEvent extends Event implements Cancellable {

	// ------------------------------
	//  Fields
	// ------------------------------
	
	private static final HandlerList handlers = new HandlerList();
	private final String factionName;
	private final FPlayer creator;
	private Boolean cancelled = false;
	
	// ------------------------------
	//  Constructor 
	// ------------------------------
	
	public FactionCreateEvent(String factionName, FPlayer creator) {
		this.factionName = factionName;
		this.creator = creator;
	}
	
	// ------------------------------
	//  Methods
	// ------------------------------
	
	// Gets the Faction that has just been created 
	public String getName() { return this.factionName; }
	public FPlayer getCreator() { return this.creator; }
	
	// Cancel Stuff...	
	public boolean isCancelled() { return this.cancelled; }
	public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
	
	// Handler stuff...
	public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
}
