package markehme.factionsplus.Cmds;

import java.io.File;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;

import org.bukkit.ChatColor;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Permission;

public class CmdUnsetJail extends FPCommand {
	public CmdUnsetJail() {
		this.aliases.add("unsetjail");

		//this.requiredArgs.add("name");
		//this.optionalArgs.put("on/off", "flip");

		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = true;
		
		this.setHelpShort("removes the Jail location");
	}
	
	@Override
	public void performfp() {
		//TODO: check the set/unsetjail permissions just like we did for ban and jail
		if(!FactionsPlus.permission.has(Utilities.getOnlinePlayerExact(fme), "factionsplus.unsetjail")) {
			sender.sendMessage(ChatColor.RED + "No permission!");
			return;
		}
		
		Faction currentFaction = fme.getFaction();
		
		boolean authallow = ((Config._jails.leadersCanSetJails._) && (Utilities.isLeader( fme ))) 
				|| ((Config._jails.officersCanSetJails._) && (Utilities.isOfficer( fme )))
				|| (Config._jails.membersCanSetJails._);
				
				
		if (!authallow) {
			sender.sendMessage(ChatColor.RED + "Sorry, your faction rank is not allowed to do that!");
			//ie. leader maybe can't but officer can, depending on the options set in config (while clearly that's crazy to set,
			//it's possible and up to server admin)
			return;
		}
		
		File currentJailFile = new File(Config.folderJails, "loc." + currentFaction.getId());
		
		currentJailFile.delete();
		
		fme.msg("The jail location has been removed.");
	}
	
}
