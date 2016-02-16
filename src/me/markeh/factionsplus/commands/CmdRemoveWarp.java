package me.markeh.factionsplus.commands;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.command.requirements.ReqHasFaction;
import me.markeh.factionsframework.command.requirements.ReqIsPlayer;
import me.markeh.factionsframework.command.requirements.ReqPermission;
import me.markeh.factionsframework.objs.Perm;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.conf.FactionData;
import me.markeh.factionsplus.conf.Texts;

public class CmdRemoveWarp extends FactionsCommand {

	public CmdRemoveWarp() {
		this.aliases.add("removewarp");
		this.requiredArguments.add("name");
		
		this.description = "Remove a faction warp";
				
		this.addRequirement(ReqHasFaction.get());
		this.addRequirement(ReqIsPlayer.get());
		this.addRequirement(ReqPermission.get(Perm.get("factionsplus.managewarps", "You don't have permission to remove warps.")));

	}
	
	@Override
	public void run() {
		if ( ! Config.get().enableWarps) {
			msg(Texts.warps_notEnabled);
			return;
		}
		
		if ( ! this.fplayer.isLeader() && !this.fplayer.isOfficer()) {
			msg("<red>Your rank is not high enough to remove warps");
			return;
		}
		
		String warpName = getArg(0).toLowerCase();
		
		FactionData fdata = FactionData.get(this.faction.getID());
		
		if (!fdata.warpLocations.containsKey(warpName)) {
			msg("<red>That warp does not exist.");
			return;
		}
		
		fdata.warpLocations.remove(warpName);
		fdata.warpPasswords.remove(warpName);
		
		msg("<green>Warp removed!");
		
		if (Config.get().warpsAnnounce) {
			this.faction.msg("<green><?> removed the warp <gold><?>", this.sender.getName(), warpName);
		}
	}
}
