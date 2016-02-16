package me.markeh.factionsframework.objs;

import java.util.HashMap;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Perm {
	private static HashMap<String, Perm> permissionCollection = new HashMap<String, Perm>();
	public static Perm get(String permission, String message) {
		if ( ! permissionCollection.containsKey(permission.toLowerCase())) {
			permissionCollection.put(permission.toLowerCase(), new Perm(permission, message));
		}
		
		return permissionCollection.get(permission.toLowerCase());
	}
	
	private String permission;
	private String message;
	
	public Perm(String permission, String message) {
		this.permission = permission;
		this.message = message;
	}
	
	public boolean has(Player player) {
		return(player.hasPermission(this.permission));
	}
	
	public boolean has(Player player, Boolean notify) {
		if (notify && ! player.hasPermission(this.permission)) player.sendMessage(ChatColor.RED + this.message);
		
		return(player.hasPermission(this.permission));
	}

	public boolean has(CommandSender sender) {
		return(sender.hasPermission(this.permission));
	}
	
	public boolean has(CommandSender sender, boolean notify) {
		if (notify && ! sender.hasPermission(this.permission)) sender.sendMessage(ChatColor.RED + this.message);
		
		return(sender.hasPermission(this.permission));
	}
}
