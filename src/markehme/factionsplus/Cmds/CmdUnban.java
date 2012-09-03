package markehme.factionsplus.Cmds;

import java.io.*;

import markehme.factionsplus.*;
import markehme.factionsplus.config.*;
import markehme.factionsplus.config.sections.*;

import org.bukkit.*;

import com.massivecraft.factions.*;
import com.massivecraft.factions.cmd.*;
import com.massivecraft.factions.struct.*;

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
		if ((Config._banning.furtherRestrictBanUnBanToThoseThatHavePermission._)
				&&(!FactionsPlus.permission.has(sender, Section_Banning.banUnBanPermissionNodeName))) {
			sender.sendMessage(ChatColor.RED + "You don't have the required permission node!");
			return;
		}
		
		String unbanningThisPlayer = this.argAsString(0);
		Faction pFaction = fme.getFaction();
		
		boolean authallow = false;
		
		if(Config._banning.leadersCanFactionBanAndUnban._ && Utilities.isLeader(fme)){
			authallow = true;
		} else if(Config._banning.officersCanFactionBanAndUnban._ && Utilities.isOfficer(fme)){
			authallow = true;
		}
		
		if(!authallow){
			fme.msg(ChatColor.RED + "Sorry, your ranking is not high enough to do that!");
			return;
		}
		
		
		File banFile = new File(Config.folderFBans, pFaction.getId() + "." + unbanningThisPlayer.toLowerCase());
		
		if(banFile.exists()){
			boolean deleted=banFile.delete();
			me.sendMessage(unbanningThisPlayer + " has "+(deleted?"":"not")+" been unbanned from the Faction!");
		} else {
			me.sendMessage("This user isn't banned from the Faction!");
		}
		
		
	}
}
