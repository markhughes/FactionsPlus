package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusJail;
import markehme.factionsplus.Utilities;

import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;

public class CmdUnJail extends FCommand {
	public CmdUnJail() {
		this.aliases.add("unjail");
	
		this.requiredArgs.add("player");
		
		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = false;
		
		this.setHelpShort("removes a player from jail");
	}

	@Override
	public void perform() {
		String unJailingPlayer = this.argAsString(0);
		
		if((FactionsPlus.config.getBoolean("officersCanJail") && Utilities.isOfficer(fme))) {
			if(FactionsPlus.permission.playerHas(fme.getPlayer(), "factionsplus.unjail")) {
				if(FactionsPlusJail.removeFromJail(unJailingPlayer, fme.getFactionId())){
					fme.sendMessage(unJailingPlayer + " has been removed from jail.");
				} else {
					fme.sendMessage(unJailingPlayer + " is not jailed.");
				}
				return;
			}
		}
		
		if((FactionsPlus.config.getBoolean("leadersCanJail") && Utilities.isLeader(fme))) {
			if(FactionsPlus.permission.playerHas(fme.getPlayer(), "factionsplus.unjail")) {
				if(FactionsPlusJail.removeFromJail(unJailingPlayer, fme.getFactionId())){
					fme.sendMessage(unJailingPlayer + " has been removed from jail.");
				} else {
					fme.sendMessage(unJailingPlayer + " is not jailed.");
				}
				return;
			}
		}
		
		fme.sendMessage("No permission!");
	}
}
