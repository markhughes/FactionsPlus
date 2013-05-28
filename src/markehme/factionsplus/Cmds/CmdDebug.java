package markehme.factionsplus.Cmds;

import java.util.List;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Typeo;
import markehme.factionsplus.config.sections.Section_Jails;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitWorker;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;


public class CmdDebug extends FPCommand {
	public CmdDebug() {
		this.aliases.add("debug");
		
		this.optionalArgs.put("configdiff", "");
		this.errorOnToManyArgs = true;
		
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		
		this.setHelp("used to debug FactionsPlus");
		this.setDesc("From FactionsPlus, a debug command only used by ops.");
		
	}
	@Override
	public void performfp() {
		// TEMP, remove this completely after this inconsistency is fixed; if it's commented out it means it's not fixed
		
		/*
		if ( null != usender ) {
			Player player = Bukkit.getPlayerExact( usender.getId() );
			assert Utilities.getOnlinePlayerExact( usender ).equals(player);
			if ( null != player ) {
				String perm = Section_Jails.permissionNodeNameForCanJailUnjail;//"factionsplus.xyz"
				sender.sendMessage( player.getWorld().toString() );
				sender.sendMessage( "has perm: " + FactionsPlus.permission.playerHas( player, perm ) );
				sender.sendMessage( "has perm: " + FactionsPlus.permission.playerHas( (World)null, player.getName(), perm ) );
				sender.sendMessage( "has perm: "
					+ FactionsPlus.permission.playerHas( player.getWorld(), player.getName(), perm ) );
			}
		}
		*/
		
		if ( (null != usender) && (usender.isOnline()) && (!sender.isOp())) {
				return;
		}
		
		String param = this.arg(0);
		
		if ( ( param != null ) && ( param.trim().equalsIgnoreCase( "configdiff" ) ) ) {
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
		if (null != usender) {
			Faction f=usender.getFaction();
			if (null != f) {
				sender.sendMessage(Utilities.getCountOfWarps(f) + " warps for faction "+f.getName());
			}
		}
		sender.sendMessage("--- END ---");
		
	
		
		
	}

}
