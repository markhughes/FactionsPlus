package me.markeh.factionsplus.commands;

import org.bukkit.ChatColor;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.objs.Perm;
import me.markeh.factionsplus.conf.FactionData;
import me.markeh.factionsplus.conf.Texts;
import me.markeh.factionsplus.conf.types.TLoc;
import me.markeh.factionsplus.conf.types.TMap;

public class CmdWarps extends FactionsCommand {
	public CmdWarps() {
		this.aliases.add("warps");
		this.description = "List available warps";
				
		this.requiredPermissions.add(Perm.get("factionsplus.warp", "You don't have permission to do that."));
		
		this.mustHaveFaction = true;
		
	}

	@Override
	public void run() {
		if ( ! this.isPlayer() ) {
			msg(Texts.playerOnlyCommand);
			return; 
		}
		
		FactionData fdata = FactionData.get(this.faction);
		
		if (fdata.warpLocations == null) fdata.warpLocations = new TMap<String, TLoc>();
		if (fdata.warpPasswords == null) fdata.warpPasswords = new TMap<String, String>();
		
		if (fdata.warpLocations.isEmpty()) {
			msg("<red>Your faction has no warps!");
			return;
		}
		
		String warpString = "";
		
		boolean isFirst = true;
		for (String warp : fdata.warpLocations.getKeys()) {
			if (isFirst) {
				isFirst = false;
			} else warpString += ChatColor.WHITE + ", ";
			
			ChatColor colour;
			
			if (fdata.warpPasswords.get(warp) == null) {
				colour = ChatColor.GREEN;
			} else {
				colour = ChatColor.DARK_GREEN;
			}
			
			warpString += colour + warp;
		}
		
		// TODO: if warp string is too long, cut it up?
		
		msg(ChatColor.WHITE + "" + ChatColor.BOLD + "Warps: " + ChatColor.RESET + warpString);

	}
}
