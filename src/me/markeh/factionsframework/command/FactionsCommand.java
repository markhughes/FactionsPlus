package me.markeh.factionsframework.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.faction.Factions;
import me.markeh.factionsframework.factionsmanager.FactionsManager;
import me.markeh.factionsframework.objs.FPlayer;
import me.markeh.factionsframework.objs.Perm;
import me.markeh.factionsplus.conf.Texts;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class FactionsCommand {
	
	// ------------------------------
	//  Fields
	// ------------------------------
	
	// Factions obj
	public Factions factions = FactionsManager.get().fetch();
	
	// ------------------------------
	//  Command Fields
	// ------------------------------

	// Required Permissions
	public List<Perm> requiredPermissions = new ArrayList<Perm>();
	
	// Required Arguments
	public List<String> requiredArguments = new ArrayList<String>();
	// Optional Arguments
	public HashMap<String, String> optionalArguments = new HashMap<String, String>();
	
	// Aliases of the command
	public List<String> aliases = new ArrayList<String>();
	
	// Description
	public String description;
	
	public Boolean mustHaveFaction = false;
	
	public Boolean errorOnTooManyArgs = true;
	
	// ------------------------------
	//  Runtime Fields
	// ------------------------------
	
	// Player running the command
	public Player player = null;
	// FPlayer running the command
	public FPlayer fplayer = null;
	// Arguments passed to the command
	public List<String> arguments = new ArrayList<String>();
	// Players Faction
	public Faction faction = null;
	// IF change to false before calling run() the command isn't called
	public Boolean canRun = true;
	// Help line show in in /f help
	public String helpLine = "";
	
	// ------------------------------
	//  Methods
	// ------------------------------
	
	public final void reset() {
		this.player = null;
		this.faction = null;
		this.fplayer = null;
		this.canRun = true;
	}
	
	// Called before run() and sets up important fields
	public final void preRun() {
		
		// Player specific 
		if(this.player != null) {
			// Fetch Faction
			this.faction = this.factions.getFactionFor(this.player);
			
			this.fplayer = FPlayer.get(this.player);
			
			for(Perm perm : this.requiredPermissions) {
				if(!perm.has(this.player, true)) this.canRun = false;
			}
		}
		
		if (this.mustHaveFaction) {
			if(this.player == null || this.faction == null) {
				msg(Texts.playerMustHaveFaction);
				this.canRun = false;
				return;
			} else if( ! this.fplayer.hasFaction()) {
				msg(Texts.playerMustHaveFaction);
				this.canRun = false;
				return;
			}
		}
	}
	
	// Simple way to send a message to a player
	public final String msg(String msg) {
		msg = this.colourise(msg);
		
		if (this.player == null) {
			Bukkit.getLogger().log(Level.INFO, msg);
		} else {
			this.player.sendMessage(msg);
		}
		
		return msg;
	}
	
	public final String getArg(int index) {
		if (arguments.size() >= index+1) {
			return(arguments.get(index));
		}
		
		return null;
	}
	
	// fplayer is null if it's not a player 
	public final boolean isPlayer() {
		return this.fplayer != null;
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
	
	// Must be overridden, is called on run
	public abstract void run();
	
	// Fetchs a wrapper used by the command managers 
	public Object getWrapper() { throw new Error("Attempting to getWrapper when none has been set!"); }
}
