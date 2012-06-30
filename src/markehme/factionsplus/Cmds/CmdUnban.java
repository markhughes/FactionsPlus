package markehme.factionsplus.Cmds;

import java.io.File;

import org.bukkit.ChatColor;

import markehme.factionsplus.*;
import markehme.factionsplus.config.*;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;

public class CmdUnban extends FCommand {
	public CmdUnban() {
		this.aliases.add("unban");
		
		this.requiredArgs.add("player");
		
		this.errorOnToManyArgs = false;
		
		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = true;
		
		this.setHelpShort("unbans a player allowing them to re-join the faction");
	}
	
	@Override
	public void perform(){
		String unbanningThisPlayer = this.argAsString(0);
		Faction pFaction = fme.getFaction();
		
		if(!FactionsPlus.permission.has(sender, "factionsplus.unban")){
			sender.sendMessage(ChatColor.RED + "No permissions!");
			return;
		}
		
		boolean authallow = false;
		
		if(Config.banning.leadersCanFactionUnban && Utilities.isLeader(fme)){
			authallow = true;
		} else if(Config.banning.officersCanFactionUnban && Utilities.isOfficer(fme)){
			authallow = true;
		}
		
		if(!authallow){
			fme.msg(ChatColor.RED + "Sorry, your ranking is not high enought to do that!");
			return;
		}
		
		File banFile = new File(Config.folderFBans, pFaction.getId() + "." + unbanningThisPlayer.toLowerCase());
		
		if(banFile.exists()){
			banFile.delete();
			me.sendMessage(unbanningThisPlayer + " has been unbanned from the Faction!");
			return;
		} else {
			me.sendMessage("This user isn't banned from the Faction!");
			return;
		}
	}
}
