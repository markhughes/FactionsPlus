package me.markeh.factionsplus.commands;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.objs.Perm;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.conf.FactionData;
import me.markeh.factionsplus.conf.Texts;

public class CmdRemoveWarp extends FactionsCommand {

	public CmdRemoveWarp() {
		this.aliases.add("removewarp");
		this.requiredArguments.add("name");
		
		this.description = "Remove a faction warp";
		
		this.requiredPermissions.add(Perm.get("factionsplus.managewarps", "You don't have permission to remove warps."));
		
		this.mustHaveFaction = true;
		
	}
	
	@Override
	public void run() {
		// TODO: check warps enabled
		
		if ( ! Config.get().enableWarps) {
			msg(Texts.warps_notEnabled);
			return;
		}
		
		if ( ! this.isPlayer() ) {
			msg(Texts.playerOnlyCommand);
			return; 
		}
		
		if ( ! this.fplayer.isLeader() && !this.fplayer.isOfficer()) {
			msg("Your rank is not high enough to remove warps");
			return;
		}
		
		String warpName = getArg(0).toLowerCase();
		
		FactionData fdata = FactionData.get(this.faction.getID());
		
		if (!fdata.warpLocations.containsKey(warpName)) {
			msg("That warp does not exist.");
			return;
		}
		
		fdata.warpLocations.remove(warpName);
		fdata.warpPasswords.remove(warpName);
		
		msg("Warp removed!");
		
		// TODO: add configuration option to announce the warp was removed to the entire faction 
	}
}
