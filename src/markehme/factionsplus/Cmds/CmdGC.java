package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;

import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;

public class CmdGC extends FCommand  {
	public CmdGC() {
		this.aliases.add("gc");
		
		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = true;
		
		this.setHelpShort("set your chat to global (public)");
	}
	
	public void perform() {
		if(!FactionsPlus.permission.has(fme.getPlayer(), "factionsplus.factionglobalchatcommand")) {
			fme.msg("No permission!");
			return;
		}
		
		// TODO: Find solution to this! 
		
		if(!FactionsPlus.FactionsVersion.startsWith("1.7")) {
			//fme.setChatMode(com.massivecraft.factions.struct.ChatMode.PUBLIC);
			
			fme.msg("Your chat mode has been changed to global.");
		} else {
			fme.msg("This version of Factions does not support changing chat mode.");
		}
		fme.msg("Your chat mode has been changed to global.");
	}

}