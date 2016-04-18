package me.markeh.factionsplus.commands;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.command.requirements.ReqHasFaction;
import me.markeh.factionsframework.command.requirements.ReqIsPlayer;
import me.markeh.factionsframework.command.requirements.ReqPermission;
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
				
		this.description = Texts.get().cmdJail_description;
		
		this.addRequirement(ReqHasFaction.get());
		this.addRequirement(ReqIsPlayer.get());
		this.addRequirement(ReqIsLeader.get());
		this.addRequirement(ReqIsOfficer.get());
		this.addRequirement(ReqPermission.get(Perm.get("factionsplus.jail", Texts.get().cmdJail_noPermission)));

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

		if ( ! getFPlayer().isLeader() && !getFPlayer().isOfficer()) {
			msg(Texts.get().cmdJail_badRank);
			return;
		}
		
		FactionData fdata = FactionData.get(getFaction());
		
		if (fdata.jailLoc == null) {
			msg(Texts.get().cmdJail_notSet);
			return;
		}
		
		Faction factionAt = factions.getFactionAt(fdata.jailLoc.getBukkitLocation());
		
		if (factionAt.getID() != faction.getID()) {
			msg(Texts.get().cmdJail_notInLand);
			return;
		}
		
		FPlayer jailingPlayer = this.getArgAs(FPlayer.class, 0, null);
		
		if (jailingPlayer == null) {
			msg(Texts.get().playerNotFound);
			return;
		}
				
		if (jailingPlayer.isLeader() || jailingPlayer.isOfficer()) {
			msg("You cant jail this player because they're too high of a rank.");
			return;
		}
		
		if (jailingPlayer.getFaction().getID() != faction.getID()) {
			msg(String.format(Texts.get().playerNotInYourFaction, jailingPlayer.getName())); 
			return;
		}
		
		fdata.jailedPlayers.add(jailingPlayer.getUUID().toString());
		
		if (jailingPlayer.isOnline()) jailingPlayer.teleport(fdata.jailLoc);
		
		msg(String.format(Texts.get().cmdJail_sent, jailingPlayer.getName())); 
	}
}
