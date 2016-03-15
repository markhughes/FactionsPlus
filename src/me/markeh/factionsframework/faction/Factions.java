package me.markeh.factionsframework.faction;

import java.util.List;

import me.markeh.factionsframework.factionsmanager.FactionsManager;
import me.markeh.factionsframework.objs.FPlayer;
import me.markeh.factionsframework.objs.Loc;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Factions {
	
	// ----------------------------------------
	// Static Getters
	// ----------------------------------------
	
	private static Factions factions = null;
	public static Factions get() {
		if (factions == null) factions = FactionsManager.get().fetch();
		
		return factions;
	}

	public static Faction get(String id) {
		return get().getFactionById(id);
	}
	
	public static Faction get(Loc loc) {
		return get().getFactionAt(loc);
	}
	
	public static Faction get(Player player) {
		return get().getFactionFor(player);
	}
	
	public static Faction get(FPlayer player) {
		return get(player.getPlayer());
	}
	
	// ----------------------------------------
	// Abstract Methods
	// ----------------------------------------
	
	// Get Faction by their ID
	public abstract Faction getFactionById(String id);
	
	// Get Faction at a location
	public abstract Faction getFactionAt(Location location);
	
	public Faction getFactionAt(Loc location) {
		return this.getFactionAt(location.asBukkitLocation());
	}
		
	// Get Faction for a player
	public abstract Faction getFactionFor(Player player);
	
	// Get Faction for a sender
	public Faction getFactionFor(CommandSender sender) {
		if (sender instanceof Player) return this.getFactionFor((Player) sender);
		
		// Must be a console sender - assume wilderness 
		return this.getFactionById(this.getWildernessId());
	}
	
	// Get Wilderness ID
	public abstract String getWildernessId();
	
	// Get Safezone ID
	public abstract String getSafeZoneId();
		
	// Get WarZone ID
	public abstract String getWarZoneId();
	
	// Check if factions is enabeld 
	public abstract boolean isFactionsEnabled(World world);
	
	public abstract List<Faction> getAll();
	
	// ----------------------------------------
	// Deprecated Methods 
	// ----------------------------------------
			
	// EOL: 4 Releases after 2.0.0-beta4
	@Deprecated
	public String getWildernessID() { return this.getWildernessId(); }
	
	// EOL: 4 Releases after 2.0.0-beta4
	@Deprecated
	public String getSafezoneID()  { return this.getSafeZoneId(); }
		
	// EOL: 4 Releases after 2.0.0-beta4
	@Deprecated
	public String getWarZoneID()  { return this.getWarZoneId(); }
	
	// EOL: 4 Releases after 2.0.0-beta4
	@Deprecated
	public Faction getFactionByID(String id) { return this.getFactionById(id); }

}
