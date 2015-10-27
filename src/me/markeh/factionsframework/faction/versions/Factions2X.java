package me.markeh.factionsframework.faction.versions;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;

import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.faction.Factions;

public class Factions2X extends Factions {
	
	private HashMap<String, Faction2X> factionMap = new HashMap<String, Faction2X>();
	
	@Override
	public Faction getFactionByID(String id) {
		id = id.toLowerCase();
		
		if( ! factionMap.containsKey(id)) {
			factionMap.put(id, new Faction2X(id));
		}
		
		return (factionMap.get(id));
	}

	@Override
	public Faction getFactionAt(Location location) {
		return (this.getFactionByID(BoardColl.get().getFactionAt(PS.valueOf(location)).getId()));
	}

	@Override
	public Faction getFactionFor(Player player) {
		return (this.getFactionByID(MPlayer.get(player).getFactionId()));
	}

	@Override
	public String getWildernessID() {
		return FactionColl.get().getNone().getId();
	}

	@Override
	public String getWarZoneID() {
		return FactionColl.get().getWarzone().getId();
	}
}
