package me.markeh.factionsplus.commands;

import java.util.UUID;

import org.bukkit.Bukkit;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.objs.Perm;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.conf.FactionData;
import me.markeh.factionsplus.conf.Texts;

public class CmdSetJail extends FactionsCommand {
	
	// ----------------------------------------
	// Constructor
	// ----------------------------------------
	
	public CmdSetJail() {
		this.aliases.add("setjail");
		
		this.description = "Set your faction jail location";
		
		this.requiredPermissions.add(Perm.get("factionsplus.jail", Texts.cmdSetJail_noPermission));
		
		this.mustHaveFaction = true;
		
	}
	
	// ----------------------------------------
	// Methods
	// ----------------------------------------
	
	@Override
	public void run() {
		if ( ! this.isPlayer() ) {
			msg(Texts.playerOnlyCommand);
			return; 
		}
		
		if ( ! Config.get().enableJails) {
			msg(Texts.jails_notEnabled);
			return;
		}

		if ( ! fplayer.isLeader() && !fplayer.isOfficer()) {
			msg(Texts.cmdSetJail_badRank);
			return;
		}
		
		if (factions.getFactionAt(player.getLocation()) != faction) {
			msg(Texts.cmdSetJail_notInLand);
			return;
		}
		
		FactionData fdata = FactionData.get(faction.getID());
		
		fdata.jailLoc = player.getLocation();
		
		for(String pid : fdata.jailedPlayers) {
			if(Bukkit.getPlayer(UUID.fromString(pid)).isOnline()) {
				Bukkit.getPlayer(UUID.fromString(pid)).teleport(fdata.jailLoc);
			}
		}
		
		msg(Texts.cmdSetJail_jailSet);
	}
}
