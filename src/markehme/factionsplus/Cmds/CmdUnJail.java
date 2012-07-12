package markehme.factionsplus.Cmds;

import org.bukkit.*;

import markehme.factionsplus.*;
import markehme.factionsplus.FactionsBridge.*;
import markehme.factionsplus.config.*;

import com.massivecraft.factions.*;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;



public class CmdUnJail extends FCommand {
	
	public CmdUnJail() {
		this.aliases.add( "unjail" );
		
		this.requiredArgs.add( "player" );
		
		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = false;
		
		this.setHelpShort( "removes a player from jail" );
	}
	
	
	@Override
	public void perform() {
		String playerToUnjail = this.argAsString( 0 );
		
		if ( Config._jails.officersCanJail._ && Utilities.isOfficer( fme )
				|| ( Config._jails.leadersCanJail._ && Utilities.isLeader( fme ) ) 
				|| ( Utilities.isOp( fme ) )
				|| ( FactionsPlus.permission.playerHas( fme.getPlayer(), "factionsplus.unjail" ) ) )
		{
			
			if ( !FPlayers.i.exists( playerToUnjail ) ) {
				fme.sendMessage( ChatColor.RED + "That player does not exist on this server" );
				return;
			}
			
			FPlayer fp = FPlayers.i.get( playerToUnjail );// never null
			
			if ( !fme.getFactionId().equals( fp.getFactionId() ) ) {
				fme.sendMessage( ChatColor.RED + "That player is not in your faction" );
				return;
			}
			
			if ( FactionsPlusJail.removeFromJail( playerToUnjail, fp.getFactionId() ) ) {
				fme.sendMessage( playerToUnjail + " has been removed from jail." );
			} else {
				fme.sendMessage( playerToUnjail + " is not jailed." );
			}
			return;
		}
		fme.sendMessage(ChatColor.RED+ "No permission to unjail!" );
		
	}
}
