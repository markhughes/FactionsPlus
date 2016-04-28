package me.markeh.factionsframework.faction.versions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.IdUtil;

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

	@Override
	public Faction getFactionFor(Player player) {
		try {
			return this.getFactionById(UPlayer.get(player).getFactionId());
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
			// Requires reflection, not in latest MConf - ref is broken 
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
	
	public static Set<UPlayer> getClaimInformees(UPlayer usender, com.massivecraft.factions.entity.Faction oldFaction, com.massivecraft.factions.entity.Faction newFaction, Class<?> clazz) {
		Set<UPlayer> ret = new HashSet<UPlayer>();
		
		if (usender != null) ret.add(usender);
		
		for (com.massivecraft.factions.entity.Faction faction : com.massivecraft.factions.entity.BoardColls.get().get2(usender).getFactionToChunks().keySet())
		{
			if (faction == null) continue;
			if (faction.isNone()) continue;
			ret.addAll(getUPlayersIn(faction, clazz));
		}
		
		if (MConf.get().logLandClaims)
		{
			ret.add(UPlayer.get(IdUtil.getConsole()));
		}
		
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public static List<UPlayer> getUPlayersIn(com.massivecraft.factions.entity.Faction faction, Class<?> clazz) {
		try { 
			List<UPlayer> uplayers = (List<UPlayer>) faction.getClass().getMethod("getUPlayers").invoke(clazz);
			return uplayers;
		} catch(Exception e) { }
		
		return new ArrayList<UPlayer>();
	}
	
}
