package markehme.factionsplus.Cmds;

import java.util.*;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.*;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.*;

import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;

public class CmdDebug extends FCommand {
	public CmdDebug() {
		this.aliases.add("debug");
		this.errorOnToManyArgs = true;
		
		//this.requiredArgs.add("message");
		this.optionalArgs.put("configdiff", "");

		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = false;
		senderMustBeMember = false;
		
		this.setHelpShort("used to debug FactionsPlus");
		
	}
	@Override
	public void perform() {
		if ( (null != fme) && (fme.isOnline()) && (!sender.isOp())) {
				return;
		}
		
		String param = this.argAsString(0).toLowerCase().trim();
		if (param.equals( "configdiff" )) {
			Typeo.showDiff( sender );
			return;
		}
		
		sender.sendMessage("--- START ---");
		sender.sendMessage("Bukkit Version: " + Bukkit.getBukkitVersion());
		sender.sendMessage("Bukkit Version: " + Bukkit.getServer().getVersion());
		List<BukkitWorker> workers = Bukkit.getScheduler().getActiveWorkers();
		sender.sendMessage("Active Workers: "+workers.size());
		
		for ( BukkitWorker bukkitWorker : workers ) {
			sender.sendMessage("  workerOwner: "+bukkitWorker.getOwner()+" taskId="+bukkitWorker.getTaskId()
				+", "+bukkitWorker.getThread().getName());			
		}
		sender.sendMessage("Permissions: " + FactionsPlus.permission.getClass().getName());
		sender.sendMessage("--- END ---");
		
		if (null != fme) {
			sender.sendMessage(Utilities.getCountOfWarps(fme.getFaction()) + "");
		}
		
		
	}

}
