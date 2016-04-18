package me.markeh.factionsplus.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.command.requirements.ReqHasFaction;
import me.markeh.factionsframework.command.requirements.ReqIsPlayer;
import me.markeh.factionsframework.command.requirements.ReqPermission;
import me.markeh.factionsframework.objs.Perm;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.conf.FactionData;
import me.markeh.factionsplus.conf.Texts;

public class CmdUnjail extends FactionsCommand {
	
	// ----------------------------------------
	// Constructor
	// ----------------------------------------
	
	public CmdUnjail() {
		this.aliases.add("unjail");
		this.requiredArguments.add("player");
		
		this.description = "Release a player from jail";
				
		this.addRequirement(ReqHasFaction.get());
		this.addRequirement(ReqIsPlayer.get());
		this.addRequirement(ReqIsLeader.get());
		this.addRequirement(ReqIsOfficer.get());
		this.addRequirement(ReqPermission.get(Perm.get("factionsplus.jail", Texts.get().cmdUnjail_noPermission)));

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
		
		if (!fplayer.isLeader() && !fplayer.isOfficer()) {
			msg(Texts.get().cmdUnjail_badRank);
			return;
		}
		
		FactionData fdata = FactionData.get(faction.getID());
		
		Player player = Bukkit.getPlayer(getArg(0));
		
		if (player == null) {
			msg(Texts.get().playerNotFound);
			return;
		}
		
		if ( ! fdata.jailedPlayers.contains(player.getUniqueId().toString())) {
			msg(String.format(Texts.get().cmdUnjail_notJailed, player.getName()));
			return;
		}
		
		fdata.jailedPlayers.remove(player.getUniqueId().toString());
		player.sendMessage(Texts.get().cmdUnjail_notifyJailedPlayer);
		
		msg(String.format(Texts.get().cmdUnJail_notifyUnjailer, getArg(0)));
	}
}
