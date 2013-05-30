package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlusJail;
import markehme.factionsplus.config.Config;

import org.bukkit.ChatColor;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;



public class CmdUnJail extends FPCommand {
	
	public CmdUnJail() {
		
		this.aliases.add( "unjail" );
		
		this.requiredArgs.add( "player" );
		this.errorOnToManyArgs = false;
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		
		this.setHelp( "removes a player from jail" );
		this.setDesc( "From FactionsPlus, it sends a player to jail" );
		
	}
	
	
	@Override
	public void performfp() {
			
		String playerToUnjail = this.arg( 0 );
		
		if (Config._jails.canJailUnjail( usender )) {
			
			FactionsPlusJail.removeFromJail( playerToUnjail, usender, true);
			
			return;
			
		}
		
		msg(ChatColor.RED+ "No permission to unjail!" );
		
	}
}
