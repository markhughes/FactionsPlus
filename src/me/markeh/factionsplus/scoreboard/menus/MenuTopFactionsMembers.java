package me.markeh.factionsplus.scoreboard.menus;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.faction.Factions;
import me.markeh.factionsplus.scoreboard.obj.SBMenu;
import me.markeh.factionsplus.util.Utils;

public class MenuTopFactionsMembers extends SBMenu<MenuTopFactionsMembers> {

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

				if (scoreboard == null) {
					scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
				}
				
				if (objective == null) {
					objective = scoreboard.registerNewObjective("Top Factions by Members", "dummy");
					objective.setDisplayName(ChatColor.AQUA + "Top Factions by Members");
					objective.setDisplaySlot(DisplaySlot.SIDEBAR);
				}

				// Grab all factions and put them into a hashmap with their power
				Map<Faction, Integer> unsortedFactions = new HashMap<Faction, Integer>();
				
				for (Faction faction : Factions.get().getAll()) unsortedFactions.put(faction, faction.getMembers().size());
				
				// Sort all entries by their value
				Map<Faction, Integer> sortedFactions = (Map<Faction, Integer>) Utils.get().entriesSortedByValues(unsortedFactions);
				
				// Grab the first 10 and store in a HashMap
				HashMap<Double, String> tempResult = new HashMap<Double, String>();
				for (Faction faction : sortedFactions.keySet()) {
					tempResult.put(Double.valueOf(faction.getMembers().toString()), faction.getName());
					
					objective.getScore(faction.getName()).setScore(faction.getMembers().size());
					
					if (tempResult.size() >= 10) break;
				}
				
				
				result = tempResult;
			}
		};
	}

	private Scoreboard scoreboard = null;
	private Objective objective = null;
	
	@Override
	public Scoreboard getScoreboard() {
		return this.scoreboard;
	}

	@Override
	public Objective getObjective() {
		return this.objective;
	}
}
