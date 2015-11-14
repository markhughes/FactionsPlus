package me.markeh.factionsplus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class FactionsPlusPlugin<T> extends JavaPlugin {

	// ----------------------------------------
	//  Methods
	// ----------------------------------------

	// Add a listener
	public final void addListener(Listener listener) {
		this.getServer().getPluginManager().registerEvents(listener, this);
	}
	
	// Remove a listener 
	public final void removeListener(Listener listener) {
		HandlerList.unregisterAll(listener);
	}
	
	// onEnable
	@Override
	public final void onEnable() {
		try { 
			// Ensure our data folder exists
			if ( ! this.getDataFolder().exists()) this.getDataFolder().mkdir();
			
			// Call the enable method 
			enable();
		} catch (Throwable e) {
			this.logError(e);;
		}
	}
	
	// onDisable
	@Override
	public final void onDisable() {
		try {
			// Unregister all events 
			HandlerList.unregisterAll(this);
			
			// Call the disable method
			disable();
		} catch(Throwable e) {
			this.logError(e);
		}
	}
	
	// log method
	@SuppressWarnings("unchecked")
	public final T log(String msg) {
		getServer().getConsoleSender().sendMessage("" + ChatColor.BOLD + "" + ChatColor.DARK_AQUA + "[" + this.getDescription().getName() + "]" + ChatColor.RESET + " " + ChatColor.WHITE + this.colourise(msg));
		return (T) this;
	}
	
	// log method
	@SuppressWarnings("unchecked")
	public final T log(String msg, String... params) {
		msg = this.colourise(msg);
		
		for (String param : params) msg = msg.replaceFirst("\\<\\?\\>", param);
		
		getServer().getConsoleSender().sendMessage("" + ChatColor.BOLD + "" + ChatColor.DARK_AQUA + "[" + this.getDescription().getName() + "]" + ChatColor.RESET + " " + ChatColor.WHITE + msg);
		
		return (T) this;
	}
	
	// log method
	@SuppressWarnings("unchecked")
	public final T debug(String msg, String... params) {
		msg = this.colourise(msg);
			
		for (String param : params) msg = msg.replaceFirst("<?>", param);
			
		getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA +"" + ChatColor.BOLD + "[" + this.getDescription().getName() + "] " + ChatColor.GOLD + "[Debug] " + ChatColor.RESET + ChatColor.WHITE + msg);
			
		return (T) this;
	}
		
		
	
	// debug method 
	@SuppressWarnings("unchecked")
	public final T debug(String msg) {
		getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA +"" + ChatColor.BOLD + "[" + this.getDescription().getName() + "] " + ChatColor.GOLD + "[Debug] " + ChatColor.RESET + ChatColor.WHITE + this.colourise(msg));
		return (T) this;
	}
	
	// colourise a string 
	public String colourise(String msg) {
		for (ChatColor colour : ChatColor.values()) {
			msg = msg.replace("<"+colour.name().toLowerCase()+">", colour+"");
		}
		
		return msg;
	}
	
	// ----------------------------------------
	//  Abstract Methods
	// ----------------------------------------
	
	// Our enable
	public abstract void enable();
	// Our disable
	public abstract void disable();
	

	// ----------------------------------------
	//  Error Management 
	// ----------------------------------------
	
	@SuppressWarnings("unchecked")
	public final T logError(Throwable e) {

		File logFolder = new File(this.getDataFolder(), "logs");
		File errorLog = new File(logFolder, new Date().getTime() + ".errorlog");
		
		if ( ! logFolder.exists()) logFolder.mkdirs();
		
		PrintWriter writer = null;
		
		try {
			writer = new PrintWriter(errorLog, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e2) {
			e2.printStackTrace();
		} finally { 

			log(ChatColor.RED + "[Error]" + ChatColor.DARK_PURPLE + " Oh no, an internal error has occurred! :-(");
			e.printStackTrace();

			if (writer == null) return (T) this;
			log(ChatColor.RED + "[Error]" + ChatColor.DARK_PURPLE + " It has been saved to " + errorLog.getPath());
			log(ChatColor.RED + "[Error]" + ChatColor.DARK_PURPLE + " Please upload to pastebin.com and include in any error reports.");

			writer.println("----------------------------------------");
			writer.println("Error Log started on " + new Date().toString());
			writer.println("----------------------------------------");
			writer.println("Server Version: " + this.getServer().getVersion());
			writer.println("FactionsPlus Version: " + this.getDescription().getVersion());
			
			try {
				writer.println("Factions Version: " + this.getServer().getPluginManager().getPlugin("Factions").getDescription().getVersion());
			} catch(Exception e3) { }

			try {
				writer.println("MassiveCore Version: " + this.getServer().getPluginManager().getPlugin("MassiveCore").getDescription().getVersion());
			} catch(Exception e3) { }
			
			writer.println("----------------------------------------");
			writer.println("Error:" + e.getMessage());
			writer.println("----------------------------------------");
			
			e.printStackTrace(writer);
			
			writer.println("----------------------------------------");
							
			for (Player p : this.getServer().getOnlinePlayers()) if (p.isOp()) p.sendMessage(ChatColor.RED + "An internal error has occured inside FactionsPlus. Please check console.");	
		}
		
		writer.close();
		
		return (T) this;
	}
}
