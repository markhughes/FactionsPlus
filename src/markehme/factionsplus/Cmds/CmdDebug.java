package markehme.factionsplus.Cmds;

import java.util.List;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.util.FPPerm;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitWorker;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;

public class CmdDebug extends FPCommand {
	public CmdDebug() {
		this.aliases.add("debug");
		
		this.fpidentifier = "debug";
		
		this.errorOnToManyArgs = true;
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasPerm.get(FPPerm.DEBUG.node));
		
		this.setHelp(LConf.get().cmdDescDebug);
		this.setDesc(LConf.get().cmdDescDebug);
	}
	
	@Override
	public void performfp() {
		if(!sender.isOp() && !usender.isUsingAdminMode()) {
			return;
		}
		
		//String param = this.arg(0);
		
		msg("-=== Debug Start ===-");
		
		msg("FactionsPlus Version: " + FactionsPlus.pluginVersion);
		msg("Bukkit Version: " + Bukkit.getBukkitVersion());
		msg("Bukkit Version: " + Bukkit.getServer().getVersion());
		
		List<BukkitWorker> workers = Bukkit.getScheduler().getActiveWorkers();
		msg("Active Workers: "+workers.size());
		
		for ( BukkitWorker bukkitWorker : workers ) {
			msg("  workerOwner: "+bukkitWorker.getOwner()+" taskId="+bukkitWorker.getTaskId() +", "+bukkitWorker.getThread().getName());			
		}
		
		msg("Permissions: " + FactionsPlus.permission.getClass().getName());
		
		msg("-=== Debug End ===-");
	}
}
