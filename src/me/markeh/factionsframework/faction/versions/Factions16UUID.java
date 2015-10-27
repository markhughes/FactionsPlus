package me.markeh.factionsframework.faction.versions;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayers;

import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.faction.Factions;

public class Factions16UUID extends Factions {
	
	// Collection of Faction objects we've already created 
	private HashMap<String, Faction> knownFactions = new HashMap<String, Faction>();
	
	@Override
	public final Faction getFactionByID(String id) {
		id = id.toLowerCase();
		
		if( ! knownFactions.containsKey(id)) {
			knownFactions.put(id, new Faction16UUID(id));
		}
		
		return(knownFactions.get(id));
	}

	@Override
	public final Faction getFactionAt(Location location) {
		String id = Board.getInstance().getFactionAt(new FLocation(location)).getId();
		
		return(this.getFactionByID(id));
	}

	@Override
	public final Faction getFactionFor(Player player) {
		String id = FPlayers.getInstance().getByPlayer(player).getFaction().getId();
		
		return(this.getFactionByID(id));
	}

	@Override
	public final String getWildernessID() {
		return com.massivecraft.factions.Factions.getInstance().getWilderness().getId();
	}

	@Override
	public final String getWarZoneID() {
		return com.massivecraft.factions.Factions.getInstance().getWarZone().getId();
	}
}
