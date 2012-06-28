package markehme.factionsplus.Cmds;

import java.io.File;

import markehme.factionsplus.*;

import org.bukkit.ChatColor;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;

public class CmdUnsetJail extends FCommand {
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
	public void perform() {
		if(!FactionsPlus.permission.has(fme.getPlayer(), "factionsplus.unsetjail")) {
			sender.sendMessage(ChatColor.RED + "No permission!");
			return;
		}
		
		Faction currentFaction = fme.getFaction();
		
		boolean authallow = false;
		
		if(Config.config.getBoolean(Config.str_leadersCanSetJails)) {
			if(fme.getRole().toString().contains("admin") || fme.getRole().toString().contains("LEADER")) { // 1.6.x
				authallow = true;
			}
		}
		
		if(Config.config.getBoolean(Config.str_officersCanSetJails)) {
			if(fme.getRole().toString().contains("mod") || fme.getRole().toString().contains("OFFICER")) {
				authallow = true;
			}
		}

		
		if(Config.config.getBoolean(Config.str_membersCanSetJails)) {
			authallow = true;
		}
		
		if(!authallow) {
			sender.sendMessage(ChatColor.RED + "Sorry, your ranking is not high enough to do that!");
			return;
		}		
		
		File currentJailFile = new File(Config.folderJails, "loc." + currentFaction.getId());
		
		currentJailFile.delete();
		
		fme.msg("The jail location has been removed.");
	}
	
}
