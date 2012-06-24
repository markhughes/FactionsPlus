package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlusJail;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;

public class CmdJail extends FCommand {
	public CmdJail() {
		this.aliases.add("jail");
		
		this.requiredArgs.add("player");
		
		this.errorOnToManyArgs = false;
		
		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = true;
		
		this.setHelpShort("send a player to jail!");
	}

	@Override
	public void perform() {
		String playerToJail = this.argAsString(0);
		FPlayer fPlayerBanThisPlayer = FPlayers.i.get(playerToJail);
		
		if(!fPlayerBanThisPlayer.getFactionId().equalsIgnoreCase(fme.getFactionId())) {
			fme.msg("You can only jail players in your Faction.");
			return;
		}
		
		FactionsPlusJail.sendToJail(playerToJail, fme.getPlayer(), -1);
		
	}
}
