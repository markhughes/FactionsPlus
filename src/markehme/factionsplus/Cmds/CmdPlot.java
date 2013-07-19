package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore.ps.PS;

public class CmdPlot extends FPCommand {
	public CmdPlot() {
		this.aliases.add( "plot" );
		
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
		
		Player p = usender.getPlayer();
		
		PS currentLocation = PS.valueOf( p.getLocation().getChunk() );
		
		msg( currentLocation.getChunk().getBlockX() + " " + currentLocation.getChunk().getBlockY() );
	}
}
