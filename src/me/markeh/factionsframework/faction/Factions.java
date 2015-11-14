package me.markeh.factionsframework.faction;

import me.markeh.factionsframework.objs.Loc;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

// Use FactionsManager.get().fetch() to get this
public abstract class Factions {
	
	// Get Faction by their ID
	public abstract Faction getFactionByID(String id);
	
	// Get Faction at a location
	public abstract Faction getFactionAt(Location location);
	
	public Faction getFactionAt(Loc location) {
		return this.getFactionAt(location.asBukkitLocation());
	}
		
	// Get Faction for a player
	public abstract Faction getFactionFor(Player player);
	
	// Get Wilderness ID
	public abstract String getWildernessID();
	
	// Get WarZone ID
	public abstract String getWarZoneID();
	
	// Check if factions is enabeld 
	public abstract boolean isFactionsEnabled(World world);
}
