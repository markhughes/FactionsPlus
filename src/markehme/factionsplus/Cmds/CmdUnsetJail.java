package markehme.factionsplus.Cmds;

import java.io.File;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;

import org.bukkit.ChatColor;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;

public class CmdUnsetJail extends FPCommand {
	public CmdUnsetJail() {
		this.aliases.add( "unsetjail" );
		
		this.addRequirements( ReqFactionsEnabled.get() );
		this.addRequirements( ReqIsPlayer.get() );
		this.addRequirements( ReqHasFaction.get() );
		
		this.setHelp("removes the Jail location");
		this.setDesc("removes the Jail location");
	}
	
	@Override
	public void performfp() {
		//TODO: check the set/unsetjail permissions just like we did for ban and jail
		if(!FactionsPlus.permission.has(Utilities.getOnlinePlayerExact(usender), "factionsplus.unsetjail")) {
			msg(ChatColor.RED + "No permission!");
			return;
		}
		
		Faction currentFaction = usender.getFaction();
		
		boolean authallow = ((Config._jails.leadersCanSetJails._) && (Utilities.isLeader( usender ))) 
				|| ((Config._jails.officersCanSetJails._) && (Utilities.isOfficer( usender )))
				|| (Config._jails.membersCanSetJails._);
				
				
		if (!authallow) {
			msg(ChatColor.RED + "Sorry, your faction rank is not allowed to do that!");
			return;
		}
		
		File currentJailFile = new File(Config.folderJails, "loc." + currentFaction.getId());
		
		currentJailFile.delete();
		
		msg("The jail location has been removed.");
	}
	
}
