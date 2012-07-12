package markehme.factionsplus.Cmds;

import org.bukkit.*;

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
//		System.out.println(Bridge.factions.getRole( fme));
		if ( FactionsPlus.permission.playerHas( fme.getPlayer(), "factionsplus.unjail" ) ) {
			if ( Config._jails.officersCanJail._ && Utilities.isOfficer( fme ) 
					|| ( Config._jails.leadersCanJail._ && Utilities.isLeader( fme ) )
					|| (Utilities.isOp( fme ))) {
				FactionsPlusJail.sendToJail( playerToJail,  fme.getPlayer(), -1 );
//					fme.sendMessage( playerToJail + " has been jailed." );
//				} else {
//					fme.sendMessage( playerToJail + " is not jailed." );
//				}
				return;
			}
			fme.sendMessage(ChatColor.RED+ "As a "+Bridge.factions.getRole( fme )+" you have No permission to jail!" );
		}else{
			fme.sendMessage(ChatColor.RED+ "You don't have the required permission node to jail!" );
		}
		
		
		
	}
}
