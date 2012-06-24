package markehme.factionsplus.Cmds;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;

public class CmdMoneyTop extends FCommand {
	public CmdMoneyTop() {
		this.aliases.add("top");
		
		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		this.setHelpShort("show the highest ranked Factions by money");
		
		senderMustBePlayer = false;
		senderMustBeMember = false;
	}
	
	@Override
	public void perform() {
		Factions.i.getByTag("");
		fme.sendMessage("On the to do list!");
		
		
	}
}
