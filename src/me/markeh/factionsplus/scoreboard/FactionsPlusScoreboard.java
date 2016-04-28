package me.markeh.factionsplus.scoreboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.markeh.factionsframework.command.FactionsCommandManager;
import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.faction.Factions;
import me.markeh.factionsframework.objs.FPlayer;
import me.markeh.factionsframework.objs.Rel;
import me.markeh.factionsplus.FactionsPlus;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.scoreboard.commands.CmdScoreboard;
import me.markeh.factionsplus.scoreboard.compatibility.ScoreboardCompatibility;
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
	
	private List<SBMenu<?>> scoreboardMenus = new ArrayList<SBMenu<?>>();
	
	public MenuTopFactionsMembers menuTopFactionsMembers = new MenuTopFactionsMembers();
	public MenuTopFactionsPower menuTopFactionsPower = new MenuTopFactionsPower();
	
	private HashMap<SBMenu<?>, BukkitRunnable> routines = new HashMap<SBMenu<?>, BukkitRunnable>();
	private int masterRoutineTask = -1;
	
	private HashMap<Player, SBMenu<?>> currentMenu = new HashMap<Player, SBMenu<?>>();
	
	private CmdScoreboard scoreboardCommand = new CmdScoreboard();
	
	// ------------------------------ 
	// Methods
	// ------------------------------ 
	
	// Returns a read-only list of the menus
	public List<SBMenu<?>> getMenus() {
		return Collections.unmodifiableList(this.scoreboardMenus);
	}
	
	// Add a menu 
	public void addMenu(SBMenu<?> menu) {
		if (this.scoreboardMenus.contains(menu)) return;
		
		this.scoreboardMenus.add(menu);
		this.routines.put(menu, menu.getRoutine());
	}
	
	// Remove a menu
	public void removeMenu(SBMenu<?> menu) {
		this.routines.remove(menu);
		this.scoreboardMenus.remove(menu);
	}
	
	// Add default menus
	public void addDefaults() {
		if (Config.get().scoreboard_menus.contains("TopMembers")) this.addMenu(this.menuTopFactionsMembers);
		if (Config.get().scoreboard_menus.contains("TopPower")) this.addMenu(this.menuTopFactionsPower);		
	}
	
	// Remove default menus
	public void removeDefaults() {
		if (this.getMenus().contains(this.menuTopFactionsMembers)) this.removeMenu(this.menuTopFactionsMembers);
		if (this.getMenus().contains(this.menuTopFactionsPower)) this.removeMenu(this.menuTopFactionsPower);
	}
	
	// Starts a master routine 
	public void startMasterRoutine() {
		if (masterRoutineTask == -1) return;
		
		for (Player player : this.currentMenu.keySet()) {
			if ( ! player.isOnline()) {
				this.currentMenu.remove(player);
				return;
			}
						
			if ( ! this.getMenus().contains(this.currentMenu.get(player))) this.setNext(player);
			
		}
		
		masterRoutineTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(FactionsPlus.get(), new Runnable() {
			@Override
			public void run() {
				for (SBMenu<?> menu : FactionsPlusScoreboard.get().getMenus()) menu.getRoutine().runTaskAsynchronously(FactionsPlus.get());
			}
		}, 0L, 25 * 20L);
	}
	
	// Stop the master routine 
	public void stopMasterRoutine() {
		Bukkit.getScheduler().cancelTask(masterRoutineTask);
		masterRoutineTask = -1;
	}
	
	// Starts/stops the scoreboard as required
	public void init() {
		if (Config.get().scoreboard_enabled) {
			if (this.isEnabled()) return;
			this.enable();
		} else {
			if ( ! this.isEnabled()) return;
			this.disable();
		}
	}
	
	// Enable the scoreboard 
	public void enable() {
		this.addDefaults();
		this.startMasterRoutine();
		
		FactionsCommandManager.get().addCommand(this.scoreboardCommand);
		
		this.enabled = true;
	}
	
	// Disable the scoreboard
	public void disable() {
		this.enabled = false;
		
		FactionsCommandManager.get().removeCommand(this.scoreboardCommand);
		
		this.stopMasterRoutine();
		this.removeDefaults();
	}
	
	// Check if the scoreboard is enabled 
	public boolean isEnabled() {
		return this.enabled;
	}
	
	// Find a menu for the request 
	public SBMenu<?> findMenu(String request) {
		request = request.toLowerCase().replaceAll(" ", "");
		
		if (request == "hide") {
			return null;
		}
		
		for (SBMenu<?> menu : this.scoreboardMenus) {
			String basicTitle = menu.getTitle().toLowerCase().replaceAll(" ", "");
			
			if (basicTitle.startsWith(request) || basicTitle.endsWith(request)) {
				return menu;
			}
		}
		
		return null;
		
	}

	// Set a menu for a player 
	public void setFor(Player player, SBMenu<?> menu) {
		this.currentMenu.put(player, menu);
	}
	
	// Set a menu for the player to as the next in the menu list
	public SBMenu<?> setNext(Player player) {
		if ( ! this.currentMenu.containsKey(player)) {
			this.currentMenu.put(player, this.getMenus().get(0));
		} else {
			int at = 0;
			
			// If we fail to do this, we'll set 'at' to -1 so it then changes to '0'
			// and will then be reset 
			try {
				at = this.getMenus().indexOf(this.currentMenu.get(player));
			} catch (Exception e) {
				at = -1;
			}
			
			SBMenu<?> newMenu = null;
			
			try { 
				// Attempt to grab the next menu
				newMenu = this.getMenus().get(at++);
			} catch (Exception e) {
				newMenu = this.getMenus().get(0);
			}
			
			this.currentMenu.put(player, newMenu);
		}
		
		return this.currentMenu.get(player);
	}
	
	public void updateScoreboard(Player player, SBMenu<?> menu) {
		if ( ! ScoreboardCompatibility.get().showForPlayer(player)) return;
		player.setScoreboard(this.scoreboardAlter(menu.getScoreboard(), player));
	}
	
	// Apply an alterations to the scoreboard before we send it out
	@SuppressWarnings("deprecation")
	private Scoreboard scoreboardAlter(Scoreboard scoreboard, Player player) {
		if (Config.get().scoreboard_teams) {
			for (Faction faction : Factions.get().getAll()) {
				Rel rel = faction.getRealtionshipTo(player);
				
				ChatColor color = null;
				if (rel == Rel.ALLY) color = ChatColor.GREEN;
				if (rel == Rel.ENEMY) color = ChatColor.DARK_RED;
				
				if (faction.getMembers().contains(player)) color = ChatColor.BLUE;
				
				Team team = scoreboard.getTeam(faction.getID());
				
				if (team == null) team = scoreboard.registerNewTeam(faction.getID());
				
				team.setDisplayName(color + faction.getName());
				// team.setNameTagVisibility(NameTagVisibility.ALWAYS);
				for (FPlayer member : faction.getMembers()) if (member.isOnline()) team.addPlayer(member.getPlayer());
				
			}
		}
		return scoreboard;
	}
}
