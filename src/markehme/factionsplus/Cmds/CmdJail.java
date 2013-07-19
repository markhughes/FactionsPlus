package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlusJail;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;
import markehme.factionsplus.config.sections.Section_Jails;

import org.bukkit.ChatColor;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;

public class CmdJail extends FPCommand {
	public CmdJail() {
		this.aliases.add( "jail" );
		
		this.requiredArgs.add( "player" );
		this.errorOnToManyArgs = false;
		
		this.addRequirements( ReqFactionsEnabled.get() );
		this.addRequirements( ReqIsPlayer.get() );
		this.addRequirements( ReqHasFaction.get() );
		
		this.setHelp( "send a player to jail" );
		this.setDesc( "send a player to jail" );
		
	}

	@Override
	public void performfp() {
		String playerToJail = this.arg(0);

		if ( Section_Jails.canJailUnjail( usender )) {
			
			if ( ( !Config._jails.canJailOnlyIfIssuerIsInOwnTerritory._ ) || ( usender.isInOwnTerritory() ) ) {
				
				FactionsPlusJail.sendToJail( playerToJail, Utilities.getOnlinePlayerExact( usender ), -1 );
				
			} else {
				
				msg( ChatColor.RED + "You must be in your own faction territory to jail someone." );
				
			}
			return;
		}
		
		msg( ChatColor.RED+ "No permission to jail!" );
	}
	
	
}
