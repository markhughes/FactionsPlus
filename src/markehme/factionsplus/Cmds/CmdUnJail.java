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
				|| ( FactionsPlus.permission.playerHas( Utilities.getPlayerExact(fme), "factionsplus.unjail" ) ) )
		{
			
			FactionsPlusJail.removeFromJail( playerToUnjail, fme);
			
			return;
		}
		fme.sendMessage(ChatColor.RED+ "No permission to unjail!" );
		
	}
}
