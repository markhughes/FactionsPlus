package me.markeh.factionsplus.commands;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.command.requirements.ReqHasFaction;
import me.markeh.factionsframework.command.requirements.ReqIsPlayer;
import me.markeh.factionsframework.objs.Perm;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.conf.FactionData;
import me.markeh.factionsplus.conf.types.TLoc;
import me.markeh.factionsplus.conf.types.TMap;
import me.markeh.factionsplus.util.Utils;

public class CmdSetWarp extends FactionsCommand {
	
	public CmdSetWarp() {
		this.aliases.add("setwarp");
		this.description = "Set a warp location at your current position.";
		
		this.requiredArguments.add("warpname");
		this.optionalArguments.put("password", "none");
		
		this.requiredPermissions.add(Perm.get("factionsplus.setwarp", "You don't have permission to set warps!"));
		
		this.addRequirement(ReqHasFaction.get());
		this.addRequirement(ReqIsPlayer.get());
	}
	
	@Override
	public void run() {
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
		
		if (fdata.warpLocations == null) fdata.warpLocations = new TMap<String, TLoc>();
		if (fdata.warpPasswords == null) fdata.warpPasswords = new TMap<String, String>();
		
		
		if (fdata.warpLocations.containsKey(warpName)) {
			msg("<red>That warp already exists!");
			return;
		}
		
		TLoc warpLocation =  new TLoc(this.player.getLocation());	
		
		if (this.factions.getFactionAt(warpLocation.getBukkitLocation()).getID() != this.faction.getID()) {
			msg("<gold>You can only set warps in your faction land.");
			return;
		}
		
		fdata.warpLocations.add(warpName, warpLocation);
		fdata.warpPasswords.add(warpName, warpPassword);
		
		msg("<green>Warp set!");
		
		if (Config.get().warpsAnnounce) {
			this.faction.msg("<green><?> set the warp <gold><?>", this.player.getName(), warpName);
		}
	}

}
