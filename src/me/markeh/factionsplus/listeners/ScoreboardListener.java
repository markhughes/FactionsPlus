package me.markeh.factionsplus.listeners;

import me.markeh.factionsframework.events.FactionDisbandEvent;
import me.markeh.factionsframework.events.FactionRenameEvent;
import me.markeh.factionsplus.scoreboard.FactionsPlusScoreboard;
import me.markeh.factionsplus.scoreboard.obj.SBMenu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ScoreboardListener implements Listener {
	
	private static ScoreboardListener instance;
	public static ScoreboardListener get() {
		if (instance == null) instance = new ScoreboardListener();
		return instance;
	}
	
	// Remove faction from scoreboard on disband
	@EventHandler
	public void onDisbandRemoveFromScoreboard(FactionDisbandEvent event) {
		if ( ! FactionsPlusScoreboard.get().isEnabled()) return;
		for (SBMenu<?> menu : FactionsPlusScoreboard.get().getMenus()) {
			if (menu.getObjective().getScore(event.getFaction().getName()) == null) continue;
			menu.getScoreboard().resetScores(event.getFaction().getName());
		}
	}
	
	// Remove faction from scoreboard on rename 
	@EventHandler
	public void onFactionNameChange(FactionRenameEvent event) {
		if ( ! FactionsPlusScoreboard.get().isEnabled()) return;
		for (SBMenu<?> menu : FactionsPlusScoreboard.get().getMenus()) {
			if (menu.getObjective().getScore(event.getFaction().getName()) == null) continue;
			menu.getScoreboard().resetScores(event.getFaction().getName());
		}
	}

}
