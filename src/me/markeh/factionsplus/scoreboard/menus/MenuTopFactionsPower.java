package me.markeh.factionsplus.scoreboard.menus;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.faction.Factions;
import me.markeh.factionsplus.scoreboard.obj.SBMenu;
import me.markeh.factionsplus.util.Utils;

public class MenuTopFactionsPower extends SBMenu<MenuTopFactionsPower> {
	
	@Override
	public String getTitle() {
		return "Top Factions (power)";
	}
	
	@Override
	public HashMap<Double, String> getLines() {
		return this.result;
	}
	
	private HashMap<Double, String> result = new HashMap<Double, String>();
	
	@Override
	public BukkitRunnable getRoutine() {
		return new BukkitRunnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {

				// Grab all factions and put them into a hashmap with their power
				Map<Faction, Double> unsortedFactions = new HashMap<Faction, Double>();
				
				for (Faction faction : Factions.get().getAll()) unsortedFactions.put(faction, faction.getPower());
				
				// Sort all entries by their value
				Map<Faction, Double> sortedFactions = (Map<Faction, Double>) Utils.get().entriesSortedByValues(unsortedFactions);
				
				// Grab the first 10 and store in a HashMap
				HashMap<Double, String> tempResult = new HashMap<Double, String>();
				for (Faction faction : sortedFactions.keySet()) {
					tempResult.put(faction.getPower(), faction.getName());
					
					if (tempResult.size() >= 10) break;
				}
				
				result = tempResult;
			}
		};
	}

	@Override
	public Scoreboard getScoreboard() {
		return null;
	}

	@Override
	public Objective getObjective() {
		return null;
	}
}
