package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;

import org.bukkit.Bukkit;

import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;

public class CmdDebug extends FCommand {
	public CmdDebug() {
		this.aliases.add("debug");
		this.errorOnToManyArgs = true;
		
		//this.requiredArgs.add("message");
		//this.optionalArgs.put("on/off", "flip");

		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = false;
		senderMustBeMember = false;
		
		this.setHelpShort("used to debug FactionsPlus");
		
	}
	@Override
	public void perform() {
		if(fme.isOnline()) {
			if(!fme.getPlayer().isOp()) {
				return;
			}
		}
		
		fme.msg("--- START ---");
		fme.msg("Bukkit Version: " + Bukkit.getBukkitVersion());
		fme.msg("Bukkit Version: " + Bukkit.getServer().getVersion());
		fme.msg("Active Workers: " + Bukkit.getScheduler().getActiveWorkers().toString());
		fme.msg("Permissions: " + FactionsPlus.permission.getClass().getName());
		fme.msg("--- END ---");
		
		fme.msg(Utilities.getCountOfWarps(fme.getFaction()) + "");
		
		
	}

}
