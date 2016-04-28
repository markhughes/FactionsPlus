package me.markeh.factionsframework.objs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.markeh.factionsframework.FactionsFramework;
import me.markeh.factionsframework.IdUtil;
import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.faction.Factions;
import me.markeh.factionsframework.factionsmanager.FactionsManager;
import me.markeh.factionsplus.conf.types.TLoc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FPlayer {
	private static FPlayer consoleFPlayer;
	
	private static HashMap<String, FPlayer> fplayerMap = new HashMap<String, FPlayer>();
	public static FPlayer get(Player player) {
		if ( ! fplayerMap.containsKey(IdUtil.get(player))) {
			fplayerMap.put(IdUtil.get(player), new FPlayer(player));
		}
		
		return fplayerMap.get(player.getUniqueId());
	}
	
	public static FPlayer get(CommandSender sender) {
		if (sender instanceof Player) {
			return get((Player) sender);
		} else {
			if (consoleFPlayer == null) consoleFPlayer = new FPlayer(null, true);
			return consoleFPlayer;
		}
	}
	
	public static FPlayer get(UUID uuid) {
		if (fplayerMap.containsKey(uuid.toString())) return fplayerMap.get(uuid.toString());
				
		return get(Bukkit.getPlayer(uuid));
	}
	
	private Factions factions = FactionsManager.get().fetch();
	private Player player;
	private Boolean isConsole = false;
	public FPlayer(Player player, Boolean isConsole) {
		this.isConsole = isConsole;
		
		if ( ! isConsole) this.player = player;
	}
	
	public FPlayer(Player player) {
		this.isConsole = false;
		this.player = player;
	}
	
	public Player getPlayer() { return this.player; }
	public Faction getFaction() {
		if (this.isConsole) return factions.getFactionById(factions.getWildernessId());
		
		return factions.getFactionFor(this.player);
	}
	
	public Boolean hasFaction() {
		if (this.getFaction() == null) return false;
		if (this.getFaction().getID() == "none") return false;
		if (this.getFaction().isWilderness()) return false;
		
		return true;
	}
	
	public Boolean isLeader() { return this.getFaction().getLeader() == this; }
	public Boolean isOfficer() { return this.getFaction().getOfficers().contains(this); }
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
		for (ChatColor colour : ChatColor.values()) {
			String name = "<"+colour.name().toLowerCase()+">";
			
			msg = msg.replace(name, colour.toString());
		}
		
		return msg;
	}
	
	public Loc getLocation() {
		return new Loc(this.player.getLocation());
	}

	public World getWorld() {
		// TODO: console - use spawn world 
		return this.player.getWorld();
	}

	public String getName() {
		if (this.isConsole) return "@console";
		return this.player.getName();
	}

	public UUID getUUID() {
		// TODO: console - get console UUID
		return this.player.getUniqueId();
	}

	public boolean isOnline() {
		if (this.isConsole) return true;
		if (this.player == null) return false;
		
		return this.player.isOnline();
	}

	public void teleport(Location bukkitLocation) {
		this.player.teleport(bukkitLocation);
	}
	
	public void teleport(Loc loc) {
		this.teleport(loc.asBukkitLocation());
	}

	public void teleport(TLoc jailLoc) {
		this.teleport(jailLoc.getBukkitLocation());
	}
}
