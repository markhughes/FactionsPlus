package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;

import com.avaje.ebean.enhance.asm.commons.Method;
import com.massivecraft.factions.cmd.FCommand;	
import com.massivecraft.factions.struct.Permission;

public class CmdFC extends FCommand  {
	public CmdFC() {
		this.aliases.add("fc");
		
		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = true;
		
		this.setHelpShort("set your chat to Faction-only");
	}
	
	public void perform() {
		if(!FactionsPlus.permission.has(fme.getPlayer(), "factionsplus.factionchatcommand")) {
			fme.msg("No permission!");
			return;
		}
		// TODO: >1.6:
			//fme.setChatMode(com.massivecraft.factions.struct.ChatMode.FACTION);
			fme.msg("Your chat mode has been changed to Faction-only.");
		// TODO: >1.7:
			fme.msg("This version of Factions does not support changing chat mode.");
		
		
	}

}
