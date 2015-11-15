package me.markeh.factionsframework.objs;

import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.factionsmanager.FactionsManager;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class Loc {
	public static Loc from(Location loc) { return new Loc(loc); }
	public static Loc from(Block block) { return new Loc(block.getLocation()); }
	
	// constructor
	protected Loc(Location loc) {
		this.location = loc;
		this.factionHere = FactionsManager.get().fetch().getFactionAt(this);
	}
	
	// fields
	private Location location;
	private Faction factionHere;
	
	// methods
	public Location asBukkitLocation() {
		return this.location;
	}
	
	public boolean isWilderness() {
		return this.factionHere.isWilderness();
	}
	
	public boolean isOwnedBy(Faction faction) {
		return this.factionHere.getID().equals(faction.getID());
	}
	
	public Faction getFactionHere() {
		return this.factionHere;
	}
}
