package markehme.factionsplus.Cmds;

import markehme.factionsplus.*;
import markehme.factionsplus.config.*;
import markehme.factionsplus.config.sections.*;

import org.bukkit.*;

import com.massivecraft.factions.cmd.*;
import com.massivecraft.factions.struct.*;

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

		if (Section_Jails.canJailUnjail( fme ))
		{
			if ( ( !Config._jails.canJailOnlyIfIssuerIsInOwnTerritory._ ) || ( fme.isInOwnTerritory() ) ) {
				FactionsPlusJail.sendToJail( playerToJail, Utilities.getOnlinePlayerExact( fme ), -1 );
			} else {
				fme.sendMessage( ChatColor.RED + "You must be in your own faction territory to jail someone." );
			}
			return;
		}
		fme.sendMessage(ChatColor.RED+ "No permission to jail!" );
	}
	
	
}
