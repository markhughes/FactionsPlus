package markehme.factionsplus.Cmds;

import markehme.factionsplus.*;
import markehme.factionsplus.FactionsBridge.*;
import markehme.factionsplus.config.*;

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
//		FPlayer fPlayerBanThisPlayer = FPlayers.i.get(playerToJail);
		
		if ( FactionsPlus.permission.playerHas( fme.getPlayer(), "factionsplus.unjail" ) ) {
			if ( Config._jails.officersCanJail._ && Utilities.isOfficer( fme ) 
					|| ( Config._jails.leadersCanJail._ && Utilities.isLeader( fme ) )
					|| (Utilities.isOp( fme ))) {
				if ( FactionsPlusJail.sendToJail( playerToJail,  fme.getPlayer(), -1 ) ) {
					fme.sendMessage( playerToJail + " has been removed from jail." );
				} else {
					fme.sendMessage( playerToJail + " is not jailed." );
				}
				return;
			}
		}
		
		fme.sendMessage( "As a "+Bridge.factions.getRole( fme )+" you have No permission to jail!" );
		
	}
}
