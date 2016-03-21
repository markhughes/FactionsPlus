package me.markeh.factionsframework.faction.versions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayers;

import me.markeh.factionsframework.FactionsFramework;
import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.faction.Factions;
import me.markeh.factionsplus.FactionsPlus;

public class Factions1_6UUID extends Factions {
	
	// Collection of Faction objects we've already created 
	private HashMap<String, Faction> knownFactions = new HashMap<String, Faction>();
	
	@Override
	public final Faction getFactionById(String id) {
		id = id.toLowerCase();
		
		if( ! knownFactions.containsKey(id)) {
			knownFactions.put(id, new Faction1_6UUID(id));
		}
		
		return(knownFactions.get(id));
	}

	@Override
	public final Faction getFactionAt(Location location) {
		String id = Board.getInstance().getFactionAt(new FLocation(location)).getId();
		
		return(this.getFactionById(id));
	}

	@Override
	public final Faction getFactionFor(Player player) {
		String id = FPlayers.getInstance().getByPlayer(player).getFaction().getId();
		
		return(this.getFactionById(id));
	}

	@Override
	public final String getWildernessId() {
		try {
			return ((com.massivecraft.factions.Faction) this.getInstance().getClass().getMethod("getWilderness").invoke(this)).getId();
		} catch (Exception e) {
			e.printStackTrace();
			FactionsFramework.get().logError(e);
			return null;
		}

	}

	@Override
	public final String getWarZoneId() {
		try {
			return ((com.massivecraft.factions.Faction) this.getInstance().getClass().getMethod("getWarZone").invoke(this)).getId();
		} catch (Exception e) {
			e.printStackTrace();
			FactionsFramework.get().logError(e);
			return null;
		}
	}
	
	@Override
	public final String getSafeZoneId() {
		try {
			return ((com.massivecraft.factions.Faction) this.getInstance().getClass().getMethod("getSafeZoneId").invoke(this)).getId();
		} catch (Exception e) {
			e.printStackTrace();
			FactionsFramework.get().logError(e);
			return null;
		}
	}

	@Override
	public boolean isFactionsEnabled(World world) {
		return ! Conf.worldsNoClaiming.contains(world.getName());
	}

	@Override
	public List<Faction> getAll() {
		List<Faction> factions = new ArrayList<Faction>();
		for(com.massivecraft.factions.Faction faction : this.getAllOriginalFactions()) {
			factions.add(Factions.get().getFactionById(faction.getId()));
		}
		
		return factions;
	}
	
	private com.massivecraft.factions.Factions factionsInstance = null;
	
	private com.massivecraft.factions.Factions getInstance() {
		if (this.factionsInstance != null) return factionsInstance;
		
		try {
			factionsInstance = (com.massivecraft.factions.Factions) com.massivecraft.factions.Factions.class.getMethod("getInstance").invoke(this);
		} catch (Exception e) {
			FactionsPlus.get().logError(e);
			return null;
		}
		
		return factionsInstance;
	}
		
	@SuppressWarnings("unchecked")
	private ArrayList<com.massivecraft.factions.Faction> getAllOriginalFactions() {
		try {
			return (ArrayList<com.massivecraft.factions.Faction>) this.getInstance().getClass().getMethod("getAllFactions").invoke(this.getInstance());
		} catch (Exception e) {
			FactionsFramework.get().logError(e);
			return null;
		}
	}
}
