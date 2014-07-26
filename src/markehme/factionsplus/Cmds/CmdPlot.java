package markehme.factionsplus.Cmds;

import org.bukkit.entity.Player;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.ps.PS;

public class CmdPlot extends FPCommand {
	public CmdPlot() {
		this.aliases.add( "plot" );
		
		this.fpidentifier = "plot";
		
		this.errorOnToManyArgs = false;
		
		this.addRequirements( ReqFactionsEnabled.get() );
		this.addRequirements( ReqIsPlayer.get() );
		this.addRequirements( ReqHasFaction.get() );
		
		this.setHelp( "plot management for Factions" );
		this.setDesc( "plot management for Factions" );
	}

	@Override
	public void performfp() {
		
		/*if( ! usender.isInOwnTerritory() ) {
			msg( ChatColor.RED + "You must be in your own territory to run this command." );
			return;
		}*/
		

	}
}
