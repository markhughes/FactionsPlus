package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.extras.*;
import markehme.factionsplus.extras.FactionsAny.ChatMode;

import com.massivecraft.factions.cmd.FCommand;	
import com.massivecraft.factions.struct.Permission;

public class CmdFC extends BaseCmdChatMode  {
	public CmdFC() {
		super(FactionsAny.ChatMode.FACTION,"factionsplus.factionchatcommand", "fc","cf" );
	}

}
