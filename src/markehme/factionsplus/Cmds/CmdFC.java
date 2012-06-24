package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.extras.*;

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
	
	@Override
	public void perform() {
		if(!FactionsPlus.permission.has(fme.getPlayer(), "factionsplus.factionchatcommand")) {
			fme.msg("No permission!");
			return;
		}
		
//		FactionsPlus.info(""+Bridge.factions.getRelationTo( fme, fme ));//remove this, it's for tests
		if ( Bridge.factions.setChatMode( FactionsAny.ChatMode.FACTION ) ) {
			// fme.setChatMode(com.massivecraft.factions.struct.ChatMode.FACTION);
			fme.msg( "Your chat mode has been changed to Faction-only." );
		} else {
			fme.msg( "This version of Factions does not support changing chat mode." );
		}
		
	}

}
