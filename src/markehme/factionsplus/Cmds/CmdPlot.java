package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;

public class CmdPlot extends FPCommand {
	public CmdPlot() {
		this.aliases.add( "plot" );
		
		this.errorOnToManyArgs = false;
		
		this.addRequirements( ReqFactionsEnabled.get() );
		this.addRequirements( ReqIsPlayer.get() );
		
		this.setHelp( "plot management for Factions" );
		this.setDesc( "From FactionsPlus, plot management for Factions." );
	}

	@Override
	public void performfp() {
		if(! (FactionsPlus.isWorldEditEnabled && FactionsPlus.isWorldGuardEnabled)) {
			msg("This server does not have WorldGuard and WorldEdit enabled, and therefore this command can not be used.");
			return;
		}
		
		Player player = Utilities.getOnlinePlayerExact(usender);
		
		Location pointA = FactionsPlus.worldEditPlugin.getSelection(player).getMaximumPoint();
		Location pointB = FactionsPlus.worldEditPlugin.getSelection(player).getMinimumPoint();
		
		
		
		// TODO: Is all in Faction land
	}
}
