package me.markeh.factionsframework.objs;

import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.factionsmanager.FactionsManager;

import org.bukkit.Location;

public class Loc {
	// constructor
	public Loc(Location loc) {
		
	}
	
	// fields
	private Location location;
	
	// methods
	public Location asBukkitLocation() {
		return this.location;
	}
	
	public boolean isWilderness() {
		return FactionsManager.get().fetch().getFactionAt(this).isWilderness();
	}
	
	public boolean isOwnedBy(Faction faction) {
		return FactionsManager.get().fetch().getFactionAt(this) == faction;
	}
}
