package markehme.factionsplus.Cmds;

import org.bukkit.*;

import markehme.factionsplus.*;
import markehme.factionsplus.FactionsBridge.*;
import markehme.factionsplus.config.*;
import markehme.factionsplus.config.sections.Section_Jails;

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
		if (fme.getName() == playerToJail) {
			//May seem redundant, but this is necessary to prevent people from using /f jail to escape enemy territory.
			fme.sendMessage(ChatColor.RED + "You cannot jail yourself!");
		}
		if (Section_Jails.canJailUnjail( fme ))
		{
			FactionsPlusJail.sendToJail( playerToJail, Utilities.getOnlinePlayerExact(fme), -1 );
			return;
		}
		fme.sendMessage(ChatColor.RED+ "No permission to jail!" );
	}
	
	
}
