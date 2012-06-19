package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;

import org.bukkit.Location;

import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;

public class CmdPlot extends FCommand {
	public CmdPlot() {
		this.aliases.add("plot");
		
		//this.optionalArgs.put("on/off", "flip");
		
		this.errorOnToManyArgs = false;
		
		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = false;
		
		this.setHelpShort("plot management for Factions");
	}

	public void perform() {
		if(! (FactionsPlus.isWorldEditEnabled && FactionsPlus.isWorldGuardEnabled)) {
			fme.msg("This server does not have WorldGuard and WorldEdit enabled, and therefore this command can not be used.");
			return;
		}
		
		Location pointA = FactionsPlus.worldEditPlugin.getSelection(fme.getPlayer()).getMaximumPoint();
		Location pointB = FactionsPlus.worldEditPlugin.getSelection(fme.getPlayer()).getMinimumPoint();
		
		
		
		// TODO: Is all in Faction land
	}
}
