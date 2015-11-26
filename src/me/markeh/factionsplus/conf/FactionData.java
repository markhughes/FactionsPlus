package me.markeh.factionsplus.conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsplus.conf.obj.Configuration;
import me.markeh.factionsplus.conf.obj.Option;
import me.markeh.factionsplus.conf.types.TLoc;
import me.markeh.factionsplus.conf.types.TMap;

public class FactionData extends Configuration<FactionData> {
	
	// ----------------------------------------
	// Fields
	// ----------------------------------------
	
	// Identifier for the Faction
	public final String id;
	
	// ----------------------------------------
	// Temporary Data ( don't save this )
	// ----------------------------------------
	
	public long lastAnnouncement = 0;
	
	// ----------------------------------------
	// Options
	// ----------------------------------------
	
	@Option({"jails", "players", "Jailed Players"})
	public List<String> jailedPlayers = new ArrayList<String>();

	@Option({"jails", "location", "Jail Location"})
	public TLoc jailLoc = null;
	
	@Option({"rules", "rules", "Rules"})
	public List<String> rules = new ArrayList<String>();
	
	@Option({"warps", "locations", "Warp Locations"})
	public TMap<String, TLoc> warpLocations = new TMap<String, TLoc>();
		
	@Option({"warps", "passwords", "Warp Passwords"})
	public TMap<String, String> warpPasswords = new TMap<String, String>();
	
	@Option({"fchest", "location", "Faction Chest"})
	public TLoc factionChest = null;
	
	// ----------------------------------------
	//  Static Fields and Methods
	// ----------------------------------------
	
	// Collection of all data
	private static HashMap<String, FactionData> dataMap = new HashMap<String, FactionData>();
	
	// Fetch FactionData based on id
	public static FactionData get(String id) {
		id = id.toLowerCase();
		
		if ( ! dataMap.containsKey(id)) dataMap.put(id, new FactionData(id));
		
		return dataMap.get(id);
	}
	
	// Fetch FactionData based on faction object 
	public static FactionData get(Faction faction) {
		return get(faction.getID());
	}
	
	// Returns all data in the map 
	public static List<FactionData> getAll() {
		List<FactionData> dataCollection = new ArrayList<FactionData>();
		
		for (FactionData dataSet : dataMap.values()) dataCollection.add(dataSet);
		
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
}
