package me.markeh.factionsplus.conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.markeh.factionsplus.conf.obj.Configuration;
import me.markeh.factionsplus.conf.obj.Option;

import org.bukkit.Location;

public class FactionData extends Configuration {
	
	// ----------------------------------------
	// Options
	// ----------------------------------------
	
	@Option({"jails", "players", "Jailed Players"})
	public List<String> jailedPlayers = new ArrayList<String>();

	@Option({"jails", "location", "Jail Location"})
	public Location jailLoc = null;
	
	@Option({"rules", "rules", "Rules"})
	public List<String> rules = new ArrayList<String>();
	
	@Option({"warps", "locations", "Warp Locations"})
	public HashMap<String, Location> warpLocations = new HashMap<String, Location>();
	
	@Option({"warps", "passwords", "Warp Passwords"})
	public HashMap<String, String> warpPasswords = new HashMap<String, String>();
	
	@Option({"fchest", "location", "Faction Chest"})
	public Location factionChest = null;
	
	// ----------------------------------------
	// Static Fields and Methods
	// ----------------------------------------
	
	// Collection of all data
	private static HashMap<String, FactionData> dataMap = new HashMap<String, FactionData>();
	
	// Fetches a data collection
	public static FactionData get(String id) {
		if ( ! dataMap.containsKey(id)) dataMap.put(id, new FactionData(id));
		
		return dataMap.get(id);
	}
	
	// Returns all data in the map 
	public static List<FactionData> getAll() {
		List<FactionData> dataCollection = new ArrayList<FactionData>();
		
		for(FactionData dataSet : dataMap.values()) {
			dataCollection.add(dataSet);
		}
		
		return dataCollection;
	}
		
	// ----------------------------------------
	// Constructor
	// ----------------------------------------
	
	public FactionData(String id) {
		this.setSub("fdata");
		this.setName(id);
		
		// Simply store the identifier and load the data
		this.id = id;
		
		this.load().save();
	}
	
	// ----------------------------------------
	// Fields
	// ----------------------------------------
	
	// Identifier for the Faction
	public final String id;
}
