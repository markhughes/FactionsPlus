package me.markeh.factionsframework.faction.versions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.faction.Factions;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.ps.PS;

public class Factions2_6 extends Factions {
	
	private HashMap<String, Faction2_6> factionMap = new HashMap<String, Faction2_6>();
	
	@Override
	public Faction getFactionById(String id) {
		id = id.toLowerCase();
		
		if( ! factionMap.containsKey(id)) {
			factionMap.put(id, new Faction2_6(id));
		}
		
		return (factionMap.get(id));
	}

	@Override
	public Faction getFactionAt(Location location) {
		return (this.getFactionById(BoardColls.get().getFactionAt(PS.valueOf(location)).getId()));
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public Faction getFactionFor(Player player) {
		try {
			Class UPlayer = Class.forName("com.massivecraft.factions.entity.UPlayer");
			
			return (this.getFactionById((String) UPlayer.getMethod("get", Object.class).invoke(this, player).getClass().getMethod("getFactionId").invoke(this)));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private String _wilderness = null;
	@Override
	public String getWildernessId() {
		if (this._wilderness == null) {
			for (FactionColl fc : FactionColls.get().getColls()) {
				for (com.massivecraft.factions.entity.Faction f : fc.getAll()) {
					if (f.isNone()) this._wilderness = f.getId();
				}
			}
		}
		
		return this._wilderness;
	}

	private String _warzone = null;
	@Override
	public String getWarZoneId() {
		if (this._warzone == null) {
			for (FactionColl fc : FactionColls.get().getColls()) {
				for (com.massivecraft.factions.entity.Faction f : fc.getAll()) {
					if (f.getId() == fc.getWarzone().getId()) this._warzone = f.getId();
				}
			}
		}
		
		return this._warzone;
	}
	
	private String _safezone = null;
	@Override
	public String getSafeZoneId() {
		if (this._safezone == null) {
			for (FactionColl fc : FactionColls.get().getColls()) {
				for (com.massivecraft.factions.entity.Faction f : fc.getAll()) {
					if (f.getId() == fc.getSafezone().getId()) this._safezone = f.getId();
				}
			}
		}
		
		return this._safezone;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isFactionsEnabled(World world) {
		Set<String> worldsNoClaiming = null;
		
		try {
			worldsNoClaiming = (Set<String>) MConf.get().getClass().getField("worldsNoClaiming").get(this);
		} catch (Exception e) { }
		
		return worldsNoClaiming.contains(world.getName());
	}

	@Override
	public List<Faction> getAll() {
		List<Faction> factions = new ArrayList<Faction>();
		
		for (FactionColl fc : FactionColls.get().getColls()) {
			for (com.massivecraft.factions.entity.Faction f : fc.getAll()) {
				factions.add(Factions.get().getFactionById(f.getId()));
			}
		}
		
		return factions;
	}
}
