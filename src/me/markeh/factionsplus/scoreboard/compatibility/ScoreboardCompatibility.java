package me.markeh.factionsplus.scoreboard.compatibility;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ScoreboardCompatibility {
	private static ScoreboardCompatibility instance = null;
	public static ScoreboardCompatibility get() {
		if (instance == null) instance = new ScoreboardCompatibility();
		return instance;
	}
	
	public ScoreboardCompatibility() {
		if (this.pluginEnabled("BattleArena")) this.addSBCompatibility(new CompatibilityBattleArena()); 
	}
	
	private boolean pluginEnabled(String plugin) {
		return Bukkit.getPluginManager().isPluginEnabled(plugin);
	}
	
	private List<SBCompatibility> compatibilityChecks = new ArrayList<SBCompatibility>();
	
	public final boolean showForPlayer(Player player) {
		for (SBCompatibility SBc : this.compatibilityChecks) {
			if ( ! SBc.showForPlayer(player)) return false;
		}
		
		return true;
	}
	
	public final void addSBCompatibility(SBCompatibility compatibility) {
		this.compatibilityChecks.add(compatibility);
	}
}
