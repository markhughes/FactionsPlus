package me.markeh.factionsplus.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.command.requirements.ReqHasFaction;
import me.markeh.factionsframework.command.requirements.ReqIsPlayer;
import me.markeh.factionsframework.faction.Faction;
import me.markeh.factionsframework.objs.FPlayer;
import me.markeh.factionsframework.objs.Perm;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.conf.FactionData;
import me.markeh.factionsplus.conf.Texts;

public class CmdJail extends FactionsCommand {
	
	// ----------------------------------------
	// Constructor
	// ----------------------------------------

	public CmdJail() {
		this.aliases.add("jail");
		this.requiredArguments.add("player");
		
		this.requiredPermissions.add(Perm.get("factionsplus.jail", Texts.cmdJail_noPermission));
		
		this.description = Texts.cmdJail_description;
		
		this.addRequirement(ReqHasFaction.get());
		this.addRequirement(ReqIsPlayer.get());
	}
	
	// ----------------------------------------
	// Methods
	// ----------------------------------------
	
	@Override
	public void run() {
		if ( ! Config.get().enableJails) {
			msg(Texts.jails_notEnabled);
			return;
		}

		if ( ! fplayer.isLeader() && !fplayer.isOfficer()) {
			msg(Texts.cmdJail_badRank);
			return;
		}
		
		FactionData fdata = FactionData.get(faction.getID());
		
		if (fdata.jailLoc == null) {
			msg(Texts.cmdJail_notSet);
			return;
		}
		
		Faction faction = factions.getFactionAt(fdata.jailLoc.getBukkitLocation());
		
		if (faction.getID() != faction.getID()) {
			msg(Texts.cmdJail_notInLand);
			return;
		}
		
		Player jailingPlayer = Bukkit.getPlayer(getArg(0));
		
		if (jailingPlayer == null) {
			msg(Texts.playerNotFound);
			return;
		}
		
		FPlayer fJailingPlayer = FPlayer.get(jailingPlayer);
		
		if (fJailingPlayer.isLeader() || fJailingPlayer.isOfficer()) {
			msg("You cant jail this player because they're too high of a rank.");
			return;
		}
		
		if (factions.getFactionFor(jailingPlayer).getID() != faction.getID()) {
			msg(String.format(Texts.playerNotInYourFaction, jailingPlayer.getName())); 
			return;
		}
		
		fdata.jailedPlayers.add(jailingPlayer.getUniqueId().toString());
		
		if (jailingPlayer.isOnline()) jailingPlayer.teleport(fdata.jailLoc.getBukkitLocation());
		
		msg(String.format(Texts.cmdJail_sent, jailingPlayer.getName())); 
	}
}
