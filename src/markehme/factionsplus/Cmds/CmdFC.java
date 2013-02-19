package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsBridge.FactionsAny;

public class CmdFC extends BaseCmdChatMode  {
	public CmdFC() {
		super(FactionsAny.ChatMode.FACTION,"factionsplus.factionchatcommand", "fc","cf" );
	}

}
