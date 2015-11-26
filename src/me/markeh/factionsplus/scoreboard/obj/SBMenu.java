package me.markeh.factionsplus.scoreboard.obj;

import java.util.HashMap;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public abstract class SBMenu<T> {
	public abstract String getTitle();
	public abstract HashMap<Double, String> getLines();
	public abstract BukkitRunnable getRoutine();
	public abstract Scoreboard getScoreboard();
	public abstract Objective getObjective();
}
