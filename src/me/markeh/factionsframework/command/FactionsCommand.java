package me.markeh.factionsframework.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import me.markeh.factionsframework.command.requirements.Requirement;
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
	
	@Deprecated
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
	//  Private Fields
	// ------------------------------
	
	private List<Requirement> requirements = new ArrayList<Requirement>();
	
	// ------------------------------
	//  Methods
	// ------------------------------
	
	
	public final void addRequirement(Requirement requirement) {
		requirements.add(requirement);
	}
	
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
		
		// Check requirements 
		for (Requirement requirement : this.requirements) {
			if ( ! requirement.isMet(this)) {
				this.canRun = false;
				return;
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
	
	public final String colourise(String msg) {
		for (ChatColor colour : ChatColor.values()) msg = msg.replace("<"+colour.name().toLowerCase()+">", colour+"");
		
		return msg;
	}
	
	// Must be overridden, is called on run
	public abstract void run();
	
	// Fetchs a wrapper used by the command managers 
	public Object getWrapper() { throw new Error("Attempting to getWrapper when none has been set!"); }
}
