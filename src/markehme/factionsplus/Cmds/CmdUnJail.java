package markehme.factionsplus.Cmds;

import markehme.factionsplus.*;
import markehme.factionsplus.config.*;

import org.bukkit.*;

import com.massivecraft.factions.cmd.*;
import com.massivecraft.factions.struct.*;



public class CmdUnJail extends FCommand {
	
	public CmdUnJail() {
		this.aliases.add( "unjail" );
		
		this.requiredArgs.add( "player" );
		
		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = true;
		
		this.setHelpShort( "removes a player from jail" );
	}
	
	
	@Override
	public void perform() {
		String playerToUnjail = this.argAsString( 0 );
		
		if (Config._jails.canJailUnjail( fme ))
		{
			
			FactionsPlusJail.removeFromJail( playerToUnjail, fme);
			
			return;
		}
		fme.sendMessage(ChatColor.RED+ "No permission to unjail!" );
		
	}
}
