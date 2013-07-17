package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;

import org.bukkit.ChatColor;

import com.massivecraft.factions.cmd.FCommand;


public abstract class FPCommand extends FCommand{

	protected abstract void performfp();
	
	@Override
	public final void perform() {//XXX: final to avoid overriding the wrong one
		if (!FactionsPlus.instance.isEnabled()) {
			sender.sendMessage( ChatColor.RED + "This command is unavailable while FactionsPlus is not enabled." );
			return;
		}
		performfp();
	}
	
}
