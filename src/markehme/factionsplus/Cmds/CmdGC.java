package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.extras.*;

import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;

public class CmdGC extends CmdChatModeBase  {
	public CmdGC() {
		super(FactionsAny.ChatMode.PUBLIC,"factionsplus.factionglobalchatcommand", "gc","cg","cp" );
	}
	
}