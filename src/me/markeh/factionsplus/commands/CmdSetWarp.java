package me.markeh.factionsplus.commands;

import org.bukkit.Location;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.objs.Perm;
import me.markeh.factionsplus.Utils;
import me.markeh.factionsplus.conf.FactionData;
import me.markeh.factionsplus.conf.Texts;

public class CmdSetWarp extends FactionsCommand {
	
	public CmdSetWarp() {
		this.aliases.add("setwarp");
		this.description = "Set a warp location at your current position.";
		
		this.requiredArguments.add("warpname");
		this.optionalArguments.put("password", "none");
		
		this.requiredPermissions.add(Perm.get("factionsplus.setwarp", "You don't have permission to set warps!"));
		
		this.mustHaveFaction = true;
		
	}
	
	@Override
	public void run() {
		if ( ! this.isPlayer() ) {
			msg(Texts.playerOnlyCommand);
			return; 
		}
		
		String warpPassword = null;
		String warpName = getArg(0).toLowerCase();
		
		if (!this.fplayer.isLeader() && !this.fplayer.isOfficer()) {
			msg("Your rank is not high enough to set warps.");
			return;
		}
		
		if (getArg(1) != null) {
			warpPassword = Utils.get().MD5(getArg(1).toLowerCase());
			
			if (warpPassword.length() < 3) {
				msg("The warp password must be at least 3 characters.");
				return;
			}
		}
		
		FactionData fdata = FactionData.get(this.faction.getID());
		
		if (fdata.warpLocations.containsKey(warpName)) {
			msg("That warp already exists!");
			return;
		}
		
		Location warpLocation = this.player.getLocation();	
		
		if (this.factions.getFactionAt(warpLocation).getID() != this.faction.getID()) {
			msg("You can only set warps in your faction land.");
			return;
		}
		
		fdata.warpLocations.put(warpName, warpLocation);
		fdata.warpPasswords.put(warpName, warpPassword);
		
		msg("Warp set!");
		
	}

}
