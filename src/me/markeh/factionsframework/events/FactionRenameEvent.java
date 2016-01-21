package me.markeh.factionsframework.events;

import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.faction.Factions;
import me.markeh.factionsframework.objs.FPlayer;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FactionRenameEvent extends Event implements Cancellable {

	// ------------------------------
	//  Fields
	// ------------------------------
	
	private static final HandlerList handlers = new HandlerList();
	private final String factionid;
	private final FPlayer renamer;
	private final String newName;
	private final String oldName;
	private Boolean cancelled = false;
	
	// ------------------------------
	//  Constructor 
	// ------------------------------
	
	public FactionRenameEvent(String factionid, FPlayer renamer, String oldName, String newName) {
		this.factionid = factionid;
		this.renamer = renamer;
		this.newName = newName;
		this.oldName = oldName;
	}
	
	// ------------------------------
	//  Methods
	// ------------------------------
	
	// Gets the Faction that has just been created 
	public Faction getFaction() { return Factions.get().getFactionById(this.factionid); }
	public FPlayer getRenamer() { return this.renamer; }
	public String getNewName() { return this.newName; }
	public String getOldName() { return this.oldName; } 
	
	// Cancel Stuff...	
	public boolean isCancelled() { return this.cancelled; }
	public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
	
	// Handler stuff...
	public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
}
