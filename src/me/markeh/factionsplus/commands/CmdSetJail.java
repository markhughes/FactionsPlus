package me.markeh.factionsplus.commands;

import java.util.UUID;

import org.bukkit.Bukkit;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.command.requirements.ReqHasFaction;
import me.markeh.factionsframework.command.requirements.ReqIsPlayer;
import me.markeh.factionsframework.command.requirements.ReqPermission;
import me.markeh.factionsframework.objs.Perm;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.conf.FactionData;
import me.markeh.factionsplus.conf.Texts;
import me.markeh.factionsplus.conf.types.TLoc;

public class CmdSetJail extends FactionsCommand {
	
	// ----------------------------------------
	// Constructor
	// ----------------------------------------
	
	public CmdSetJail() {
		this.aliases.add("setjail");
		
		this.description = "Set your faction jail location";
				
		this.addRequirement(ReqHasFaction.get());
		this.addRequirement(ReqIsPlayer.get());
		this.addRequirement(ReqIsLeader.get());
		this.addRequirement(ReqIsOfficer.get());
		this.addRequirement(ReqPermission.get(Perm.get("factionsplus.jail", Texts.get().cmdSetJail_noPermission)));
	}
	
	// ----------------------------------------
	// Methods
	// ----------------------------------------
	
	@Override
	public void run() {
		if ( ! Config.get().enableJails) {
			msg(Texts.get().jails_notEnabled);
			return;
		}

		if ( ! fplayer.isLeader() && !fplayer.isOfficer()) {
			msg(Texts.get().cmdSetJail_badRank);
			return;
		}
		
		if (factions.getFactionAt(this.fplayer.getPlayer().getLocation()) != faction) {
			msg(Texts.get().cmdSetJail_notInLand);
			return;
		}
		
		FactionData fdata = FactionData.get(faction.getID());
		
		fdata.jailLoc = new TLoc(this.fplayer.getPlayer().getLocation());
		
		for(String pid : fdata.jailedPlayers) {
			if(Bukkit.getPlayer(UUID.fromString(pid)).isOnline()) {
				Bukkit.getPlayer(UUID.fromString(pid)).teleport(fdata.jailLoc.getBukkitLocation());
			}
		}
		
		msg(Texts.get().cmdSetJail_jailSet);
	}
}
