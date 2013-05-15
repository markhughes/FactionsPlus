package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlusJail;
import markehme.factionsplus.config.Config;

import org.bukkit.ChatColor;

import com.massivecraft.factions.struct.Permission;



public class CmdUnJail extends FPCommand {
	
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
	public void performfp() {
		String playerToUnjail = this.argAsString( 0 );
		
		if (Config._jails.canJailUnjail( fme ))
		{
			
			FactionsPlusJail.removeFromJail( playerToUnjail, fme, true);
			
			return;
		}
		fme.sendMessage(ChatColor.RED+ "No permission to unjail!" );
		
	}
}
