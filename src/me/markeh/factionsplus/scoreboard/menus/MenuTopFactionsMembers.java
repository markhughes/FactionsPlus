package me.markeh.factionsplus.scoreboard.menus;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.scheduler.BukkitRunnable;

import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.faction.Factions;
import me.markeh.factionsplus.Utils;
import me.markeh.factionsplus.scoreboard.obj.SBMenu;

public class MenuTopFactionsMembers extends SBMenu {

	@Override
	public String getTitle() {
		return "Top Factions (members)";
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
				Map<Faction, Integer> unsortedFactions = new HashMap<Faction, Integer>();
				
				for (Faction faction : Factions.get().getAll()) unsortedFactions.put(faction, faction.getMembers().size());
				
				// Sort all entries by their value
				Map<Faction, Integer> sortedFactions = (Map<Faction, Integer>) Utils.get().entriesSortedByValues(unsortedFactions);
				
				// Grab the first 10 and store in a HashMap
				HashMap<Double, String> tempResult = new HashMap<Double, String>();
				for (Faction faction : sortedFactions.keySet()) {
					tempResult.put(Double.valueOf(faction.getMembers().toString()), faction.getName());
					
					if (tempResult.size() >= 10) break;
				}
				
				result = tempResult;
			}
		};
	}
}
