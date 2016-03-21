package me.markeh.factionsframework.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.markeh.factionsframework.command.requirements.Requirement;
import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.faction.Factions;
import me.markeh.factionsframework.factionsmanager.FactionsManager;
import me.markeh.factionsframework.objs.FPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class FactionsCommand {
	
	// ----------------------------------------
	//  FIELDS
	// ----------------------------------------
	
	// Factions obj
	public Factions factions = FactionsManager.get().fetch();
	
	// ----------------------------------------
	//  COMMAND FIELDS
	// ----------------------------------------
	
	// Required Arguments
	public List<String> requiredArguments = new ArrayList<String>();
	// Optional Arguments
	public HashMap<String, String> optionalArguments = new HashMap<String, String>();
	
	// Aliases of the command
	public List<String> aliases = new ArrayList<String>();
	
	// Description
	public String description = "";
	
	// Help line show in in /f help
	public String helpLine = "";
	
	// TODO: 4 versions after beta 6 remove @Deprecated and make Boolean private 
	@Deprecated 
	public Boolean errorOnTooManyArgs = true;
		
	// ----------------------------------------
	//  RUNTIME FIELDS
	// ----------------------------------------
	
	// Sender running the command
	public CommandSender sender = null;
		
	// FPlayer running the command
	public FPlayer fplayer = null;
	
	// Arguments passed to the command
	public List<String> arguments = new ArrayList<String>();
	
	// Players Faction
	public Faction faction = null;
	
	// If changed to false before calling run() the command isn't called
	public Boolean canRun = true;
	
	// ----------------------------------------
	//  PRIVATE FIELDS
	// ----------------------------------------
	
	private List<Requirement> requirements = new ArrayList<Requirement>();
	
	// ----------------------------------------
	//  METHODS
	// ----------------------------------------
	
	public final void addRequirement(Requirement requirement) {
		requirements.add(requirement);
	}
	
	// Reset values 
	public final FactionsCommand reset() {
		this.sender = null;
		this.faction = null;
		this.fplayer = null;
		this.canRun = true;
		this.arguments.clear();
		
		return this;
	}
	
	// Should we error on too many arguments? 
	public final void setErrorOnTooManyArgs(Boolean value) {
		this.errorOnTooManyArgs = value;
	}
	
	// Called before run() and sets up important fields
	public final void preRun() {
		this.fplayer = FPlayer.get(this.sender);
		
		// Grab Faction
		if (this.sender instanceof Player) {
			this.faction = this.factions.getFactionFor(this.sender);
		} else {
			// Not a player (probably console), default to wilderness 
			this.faction = this.factions.getFactionById(this.factions.getWildernessId());
		}
		
		// Check requirements 
		for (Requirement requirement : this.requirements) {
			if ( requirement.isMet(this)) continue;
			
			this.canRun = false;
			return;
		}
	}
	
	// Simple way to send a message to a sender
	public final String msg(String msg) {
		msg = this.colourise(msg);
		
		this.sender.sendMessage(msg);
		
		return msg;
	}
	
	// Get an argument at an index 
	public final String getArg(int index) {
		if (this.arguments.size() >= index+1) {
			return(this.arguments.get(index));
		}
		
		return null;
	}
	
	// Get an argument at an index as a Faction obj
	public final Faction getArgAs(Class<? extends Faction> type, int index, Faction defaultValue) {
		if (this.arguments.size() >= index+1) {
			Faction f = this.factions.getFactionById(this.arguments.get(index));
			
			return f;
		}
		
		return defaultValue;
	}
	
	// Get an argument at an index as an FPlayer obj
	public final FPlayer getArgAs(Class<? extends FPlayer> type, int index, FPlayer defaultValue) {
		if (this.arguments.size() >= index+1) {
			FPlayer f = FPlayer.get(Bukkit.getPlayer(this.arguments.get(index)));
			
			return f;
		}
		
		return defaultValue;
	}
	
	// Get an argument at an index as a boolean
	public final Boolean getArgAs(Class<? extends Boolean> type, int index, Boolean defaultValue) {
		if (this.arguments.size() >= index+1) {
			String v = this.arguments.get(index).toLowerCase().trim();
			if (v.startsWith("y") || v.startsWith("allow")) return true;
			if (v.startsWith("n") || v.startsWith("deny")) return false;
			
			return Boolean.valueOf(v);
		}
		
		return defaultValue;
	}
	
	// Get an argument at an index as an Long 
	public final Long getArgAs(Class<? extends Long> type, int index, Long defaultValue) {
		if (this.arguments.size() >= index+1) {
			return Long.valueOf(this.arguments.get(index));
		}
		
		return defaultValue;
	}
	
	// Get an argument at an index as an Integer
	public final Integer getArgAs(Class<? extends Integer> type, int index, Integer defaultValue) {
		if (this.arguments.size() >= index+1) {
			return Integer.valueOf(this.arguments.get(index));
		}
		
		return defaultValue;
	}
	
	// Get arguments concated from an index (set to 0 for all arguments) 
	public final String getArgsConcated(int indexFrom) {
		int i = 0;
		String argLine = "";
		
		for (String arg : this.arguments) if (i >= indexFrom) argLine += " " + arg;
		
		return argLine;
	}
	
	// Determine if the sender is a player
	public final boolean isPlayer() {
		return (this.sender instanceof Player);
	}
	
	// Colours a string 
	public final String colourise(String msg) {
		for (ChatColor colour : ChatColor.values()) msg = msg.replaceAll("<"+colour.name().toLowerCase()+">", colour+"");
		
		return msg;
	}
	
	// Get the CommandSender 
	public final CommandSender getSender() {
		return this.sender;
	}
	
	// Get the FPlayer
	public final FPlayer getFPlayer() {
		return this.fplayer;
	}
	
	// Get Player (if is a player)
	public final Player getPlayer() {
		if (this.isPlayer()) return (Player) this.sender;
		
		return null;
	}
	
	// Get the Faction
	public final Faction getFaction() {
		return this.faction;
	}
	
	// Get the help line
	public final String getHelpLine() {
		return this.helpLine;
	}
	
	// Get the command description 
	public final String getDescription() {
		return this.description;
	}
	
	// Check if we error on too many arguments 
	public final Boolean doErrorOnTooManyArgs() {
		return this.errorOnTooManyArgs;
	}
	
	// ----------------------------------------
	//  ABSTRACTS / OVERRIDEABLES
	// ----------------------------------------

	// Must be overridden, is called on run
	public abstract void run();
	
	// Fetchs a wrapper used by the command managers 
	public Object getWrapper() {
		throw new Error("Attempting to getWrapper when none has been set!");
	}
	
}
