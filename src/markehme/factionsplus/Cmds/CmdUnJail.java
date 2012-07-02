package markehme.factionsplus.Cmds;

import markehme.factionsplus.*;
import markehme.factionsplus.FactionsBridge.*;
import markehme.factionsplus.config.*;

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
		String unJailingPlayer = this.argAsString( 0 );
		
		if ( FactionsPlus.permission.playerHas( fme.getPlayer(), "factionsplus.unjail" ) ) {
			if ( Config._jails.officersCanJail._ && Utilities.isOfficer( fme ) 
					|| ( Config._jails.leadersCanJail._ && Utilities.isLeader( fme ) )
					|| (Utilities.isOp( fme ))) {
				if ( FactionsPlusJail.removeFromJail( unJailingPlayer, fme.getFactionId() ) ) {
					fme.sendMessage( unJailingPlayer + " has been removed from jail." );
				} else {
					fme.sendMessage( unJailingPlayer + " is not jailed." );
				}
				return;
			}
		}
		
		fme.sendMessage( "As a "+Bridge.factions.getRole( fme )+" you have No permission to unjail!" );
	}
}
