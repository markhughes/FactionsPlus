package me.markeh.factionsframework.objs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.markeh.factionsframework.FactionsFramework;
import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.faction.Factions;
import me.markeh.factionsframework.factionsmanager.FactionsManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FPlayer {
	private static HashMap<UUID, FPlayer> fplayerMap = new HashMap<UUID, FPlayer>();
	public static FPlayer get(Player player) {
		if ( ! fplayerMap.containsKey(player.getUniqueId())) {
			fplayerMap.put(player.getUniqueId(), new FPlayer(player));
		}
		
		return fplayerMap.get(player.getUniqueId());
	}
	
	public static FPlayer get(UUID playerUUID) {
		if (fplayerMap.containsKey(playerUUID)) return fplayerMap.get(playerUUID);
		
		return get(Bukkit.getPlayer(playerUUID));
	}

	
	private Factions factions;
	private Player player;
	
	public FPlayer(Player player) {
		this.factions = FactionsManager.get().fetch();
		this.player = player;
	}
	
	public Player getPlayer() { return this.player; }
	public Faction getFaction() { return factions.getFactionFor(this.player); }
	
	public Boolean hasFaction() {
		if (this.getFaction() == null) return false;
		if (this.getFaction().getID() == "none") return false;
		if (this.getFaction().getID() == FactionsManager.get().fetch().getWildernessID()) return false;
		
		return true;
	}
	
	public Boolean isLeader() { return this.getFaction().getLeader() == this.player; }
	public Boolean isOfficer() { return this.getFaction().getOfficers().contains(this.player); }
	public String getFactionID() { return this.getFaction().getID(); }
	
	private HashMap<Integer, Runnable> warmUpTasks = new HashMap<Integer, Runnable>();
	
	private HashMap<String, Integer> taskIDs = new HashMap<String, Integer>();
	
	public void warmUpTask(Integer seconds, Runnable warmedUpTask, Runnable killTask) {
		this.warmUpTask(Long.valueOf(seconds), warmedUpTask, killTask);
	}
	
	public void warmUpTask(Long seconds, Runnable warmedUpTask, Runnable killTask) {
		final FPlayer fplayer = this;
		
		String taskID = new Date().toString() + "_" + Math.random();
		
		final int task = FactionsFramework.get().getParent().getServer().getScheduler().scheduleSyncDelayedTask(
			FactionsFramework.get().getParent(),
			new Runnable() {
				@Override
				public void run() {
					fplayer.clearWarmUpTask(taskID);
					warmedUpTask.run();
				}
			},
			seconds*20
		);
		
		taskIDs.put(taskID, task);
		
		this.warmUpTasks.put(task, killTask);
	}
	
	public void clearWarmUpTask(String taskID) {
		this.warmUpTasks.remove(this.taskIDs.get(taskID));
	}
	
	public void killWarmUpTasks() {
		if (this.warmUpTasks.isEmpty()) return;
		
		List<Integer> tasksToRemove = new ArrayList<Integer>();
		
		for (Integer task : this.warmUpTasks.keySet()) {
			FactionsFramework.get().getParent().getServer().getScheduler().cancelTask(task);
			this.warmUpTasks.get(task).run();
			
			tasksToRemove.add(task);
		}
		
		// We cant alter the array right now 
		for (Integer task : tasksToRemove) {
			this.warmUpTasks.remove(task);
		}
		
		// earlier garbage collection
		this.warmUpTasks.clear();
	}
	
	
	public void msg(String msg) {
		this.player.sendMessage(this.colourise(msg));
	}
	
	public void msg(String msg, String... params) {
		msg = this.colourise(msg);
		
		for (String param : params) msg = msg.replaceFirst("\\<\\?\\>", param);
		
		this.player.sendMessage(msg);
	}
	
	public String colourise(String msg) {
		msg = msg.replace("<aqua>", ChatColor.AQUA + "");
		msg = msg.replace("<blue>", ChatColor.BLUE + "");
		msg = msg.replace("<bold>", ChatColor.BOLD + "");
		msg = msg.replace("<darkaqua>", ChatColor.DARK_AQUA + "");
		msg = msg.replace("<darkblue>", ChatColor.DARK_BLUE + "");
		msg = msg.replace("<darkgray>", ChatColor.DARK_GRAY + "");
		msg = msg.replace("<darkgreen>", ChatColor.DARK_GREEN + "");
		msg = msg.replace("<darkpurple>", ChatColor.DARK_PURPLE + "");
		msg = msg.replace("<darkred>", ChatColor.DARK_RED + "");
		msg = msg.replace("<gold>", ChatColor.GOLD + "");
		msg = msg.replace("<gray>", ChatColor.GRAY + "");
		msg = msg.replace("<green>", ChatColor.GREEN + "");
		msg = msg.replace("<italic>", ChatColor.ITALIC + "");
		msg = msg.replace("<lightpurple>", ChatColor.LIGHT_PURPLE + "");
		msg = msg.replace("<magic>", ChatColor.MAGIC + "");
		msg = msg.replace("<red>", ChatColor.RED + "");
		msg = msg.replace("<reset>", ChatColor.RESET + "");
		msg = msg.replace("<strikethrough>", ChatColor.STRIKETHROUGH + "");
		msg = msg.replace("<underline>", ChatColor.UNDERLINE + "");
		msg = msg.replace("<white>", ChatColor.WHITE + "");
		msg = msg.replace("<yellow>", ChatColor.YELLOW + "");
		
		return msg;
	}
	
	public Loc getLocation() {
		return new Loc(this.player.getLocation());
	}
}
