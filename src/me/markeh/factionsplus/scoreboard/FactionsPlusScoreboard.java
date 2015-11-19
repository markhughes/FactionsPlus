package me.markeh.factionsplus.scoreboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import me.markeh.factionsplus.FactionsPlus;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.scoreboard.menus.MenuTopFactionsMembers;
import me.markeh.factionsplus.scoreboard.menus.MenuTopFactionsPower;
import me.markeh.factionsplus.scoreboard.obj.SBMenu;

public class FactionsPlusScoreboard {
	
	// ------------------------------ 
	// Singleton
	// ------------------------------ 
	
	private static FactionsPlusScoreboard instance = null;
	public static FactionsPlusScoreboard get() {
		if (instance == null) instance = new FactionsPlusScoreboard();
		
		return instance;
	}
	
	// ------------------------------ 
	// Fields
	// ------------------------------ 
	
	private boolean enabled = false;
	
	private List<SBMenu> scoreboardMenus = new ArrayList<SBMenu>();
	
	public MenuTopFactionsMembers menuTopFactionsMembers = new MenuTopFactionsMembers();
	public MenuTopFactionsPower menuTopFactionsPower = new MenuTopFactionsPower();
	
	private HashMap<SBMenu, BukkitRunnable> routines = new HashMap<SBMenu, BukkitRunnable>();
	private int masterRoutineTask = -1;
	
	// ------------------------------ 
	// Methods
	// ------------------------------ 
	
	// Returns a read-only list of the menus
	public List<SBMenu> getMenus() {
		return Collections.unmodifiableList(this.scoreboardMenus);
	}
	
	// Add a menu 
	public void addMenu(SBMenu menu) {
		if (this.scoreboardMenus.contains(menu)) return;
		
		this.scoreboardMenus.add(menu);
		this.routines.put(menu, menu.getRoutine());
	}
	
	// Remove a menu
	public void removeMenu(SBMenu menu) {
		this.routines.remove(menu);
		this.scoreboardMenus.remove(menu);
	}
	
	// Add default menus
	public void addDefaults() {
		this.addMenu(this.menuTopFactionsMembers);
		this.addMenu(this.menuTopFactionsPower);		
	}
	
	// Remove default menus
	public void removeDefaults() {
		if (Config.get().scoreboard_menus.contains("TopMembers")) this.removeMenu(this.menuTopFactionsMembers);
		if (Config.get().scoreboard_menus.contains("TopPower")) this.removeMenu(this.menuTopFactionsPower);
	}
	
	// Starts a master routine 
	public void startMasterRoutine() {
		if (masterRoutineTask == -1) return;
		
		masterRoutineTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(FactionsPlus.get(), new Runnable() {
			@Override
			public void run() {
				for (SBMenu menu : FactionsPlusScoreboard.get().getMenus()) menu.getRoutine().runTaskAsynchronously(FactionsPlus.get());
			}
		}, 0L, 25 * 20L);
	}
	
	// Stop the master routine 
	public void stopMasterRoutine() {
		Bukkit.getScheduler().cancelTask(masterRoutineTask);
		masterRoutineTask = -1;
	}
	
	// Enable the scoreboard 
	public void enable() {
		this.addDefaults();
		this.startMasterRoutine();
		
		this.enabled = true;
	}
	
	// Disable the scoreboard
	public void disable() {
		this.enabled = false;
		
		this.stopMasterRoutine();
		this.removeDefaults();
	}
	
	// Check if the scoreboard is enabled 
	public boolean isEnabled() {
		return this.enabled;
	}
	
}
