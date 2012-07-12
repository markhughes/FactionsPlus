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
		if ( Config._jails.officersCanJail._ && Utilities.isOfficer( fme )
			|| ( Config._jails.leadersCanJail._ && Utilities.isLeader( fme ) ) 
			|| ( Utilities.isOp( fme ) )
			|| ( FactionsPlus.permission.playerHas( fme.getPlayer(), "factionsplus.unjail" ) ) )
		{
			
			FactionsPlusJail.sendToJail( playerToJail, fme.getPlayer(), -1 );
			return;
		}
		fme.sendMessage(ChatColor.RED+ "No permission to jail!" );
	}
	
	
}
