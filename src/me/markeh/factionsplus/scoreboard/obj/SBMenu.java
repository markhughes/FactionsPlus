package me.markeh.factionsplus.scoreboard.obj;

import java.util.HashMap;

import org.bukkit.scheduler.BukkitRunnable;

public abstract class SBMenu {
	public abstract String getTitle();
	public abstract HashMap<Double, String> getLines();
	public abstract BukkitRunnable getRoutine();
}
