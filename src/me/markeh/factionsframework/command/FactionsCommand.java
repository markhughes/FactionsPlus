package me.markeh.factionsframework.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.markeh.factionsframework.command.requirements.Requirement;
import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.faction.Factions;
import me.markeh.factionsframework.factionsmanager.FactionsManager;
import me.markeh.factionsframework.objs.FPlayer;
import me.markeh.factionsframework.objs.Perm;
import me.markeh.factionsplus.conf.Texts;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
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

	
	// Required Arguments
	public List<String> requiredArguments = new ArrayList<String>();
	// Optional Arguments
	public HashMap<String, String> optionalArguments = new HashMap<String, String>();
	
	// Aliases of the command
	public List<String> aliases = new ArrayList<String>();
	
	// Description
	public String description;
	
	// Help line show in in /f help
	public String helpLine = "";
	
	public Boolean errorOnTooManyArgs = true;
	
	// Deprecated - use ReqHasFaction 
	@Deprecated
	public Boolean mustHaveFaction = false;
	
	// Deprecated - use ReqPermission
	@Deprecated
	public List<Perm> requiredPermissions = new ArrayList<Perm>();

	// Deprecated - use sender / getSender 
	@Deprecated
	public Player player = null;
	
	// ------------------------------
	//  Runtime Fields
	// ------------------------------
	
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
		this.sender = null;
		this.faction = null;
		this.fplayer = null;
		this.canRun = true;
	}
	
	// Called before run() and sets up important fields
	public final void preRun() {
		
		// Player specific 
		if (this.sender instanceof Player) {
			// Fetch Faction
			this.faction = this.factions.getFactionFor(this.sender);
			
			this.fplayer = FPlayer.get(this.sender);
			
			this.player = (Player) this.sender;
			
			for (Perm perm : this.requiredPermissions) {
				if ( ! perm.has(this.fplayer.getPlayer(), true)) this.canRun = false;
			}
		} else {
			this.faction = this.factions.getFactionById(this.factions.getWildernessId());
		}
		
		// Check requirements 
		for (Requirement requirement : this.requirements) {
			if ( ! requirement.isMet(this)) {
				this.canRun = false;
				return;
			}
		}
		
		if (this.mustHaveFaction) {
			if (this.faction == null || this.faction.isWilderness() || ! this.fplayer.hasFaction()) {
				msg(Texts.playerMustHaveFaction);
				this.canRun = false;
				return;
			}
		}
	}
	
	// Simple way to send a message to a player
	public final String msg(String msg) {
		msg = this.colourise(msg);
		
		this.sender.sendMessage(msg);
		
		return msg;
	}
	
	public final String getArg(int index) {
		if (this.arguments.size() >= index+1) {
			return(this.arguments.get(index));
		}
		
		return null;
	}
	
	public final boolean isPlayer() {
		return (this.sender instanceof Player);
	}
	
	public final String colourise(String msg) {
		for (ChatColor colour : ChatColor.values()) msg = msg.replace("<"+colour.name().toLowerCase()+">", colour+"");
		
		return msg;
	}
	
	public final CommandSender getSender() {
		return this.sender;
	}
	
	public final FPlayer getFPlayer() {
		return this.fplayer;
	}
	
	public final Player getPlayer() {
		return (Player) this.sender;
	}
	
	public final Faction getFaction() {
		return this.faction;
	}
	
	// Must be overridden, is called on run
	public abstract void run();
	
	// Fetchs a wrapper used by the command managers 
	public Object getWrapper() { throw new Error("Attempting to getWrapper when none has been set!"); }
}
