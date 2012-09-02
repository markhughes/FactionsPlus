package markehme.factionsplus.Cmds;

import java.util.*;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.*;
import markehme.factionsplus.config.sections.*;
import markehme.factionsplus.listeners.*;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.*;

import com.massivecraft.factions.*;
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
		// TEMP, remove this completely after this inconsistency is fixed; if it's commented out it means it's not fixed
//		if ( null != fme ) {
//			Player player = Bukkit.getPlayerExact( fme.getId() );
//			assert Utilities.getOnlinePlayerExact( fme ).equals(player);
//			if ( null != player ) {
//				String perm = Section_Jails.permissionNodeNameForCanJailUnjail;//"factionsplus.xyz"
//				sender.sendMessage( player.getWorld().toString() );
//				sender.sendMessage( "has perm: " + FactionsPlus.permission.playerHas( player, perm ) );
//				sender.sendMessage( "has perm: " + FactionsPlus.permission.playerHas( (World)null, player.getName(), perm ) );
//				sender.sendMessage( "has perm: "
//					+ FactionsPlus.permission.playerHas( player.getWorld(), player.getName(), perm ) );
//			}
//		}
		//temp: test these as non-op
		sender.sendMessage( "Conf.wildernessPowerLoss:"+Utilities.confIs_wildernessPowerLoss() );
		sender.sendMessage( "Conf.warzonePowerLoss:"+Utilities.confIs_warzonePowerLoss() );
		sender.sendMessage("will normally lose power, at feet:"+PowerboostListener.canLosePowerWherePlayerIsAt(fme.getPlayer()));
		sender.sendMessage("will normally lose power, in wilderness:"+PowerboostListener.canLosePowerInThisFaction(Factions.i.getNone(), fme.getPlayer().getWorld()));
		sender.sendMessage("will normally lose power, in warzone:"+PowerboostListener.canLosePowerInThisFaction(Factions.i.get(Utilities.ID_WARZONE), fme.getPlayer().getWorld()));
		sender.sendMessage("will normally lose power, in safezone:"+PowerboostListener.canLosePowerInThisFaction(Factions.i.get(Utilities.ID_SAFEZONE), fme.getPlayer().getWorld()));

		sender.sendMessage("should normally lose power, at feet:"+PowerboostListener.shouldLosePowerWherePlayerIsAt(fme.getPlayer()));
		sender.sendMessage("should normally lose power, in wilderness:"+PowerboostListener.shouldLosePowerInThisFaction(Factions.i.getNone(), fme.getPlayer().getWorld()));
		sender.sendMessage("should normally lose power, in warzone:"+PowerboostListener.shouldLosePowerInThisFaction(Factions.i.get(Utilities.ID_WARZONE), fme.getPlayer().getWorld()));
		sender.sendMessage("should normally lose power, in safezone:"+PowerboostListener.shouldLosePowerInThisFaction(Factions.i.get(Utilities.ID_SAFEZONE), fme.getPlayer().getWorld()));

		if (fme.hasFaction()) {
			sender.sendMessage("at feet isPeaceful:"+Utilities.isPeaceful( fme.getFaction() ));
			Utilities.setPeaceful( fme.getFaction(), !Utilities.isPeaceful( fme.getFaction() ) );
			sender.sendMessage("toggling at-feet peaceful; after that it is now:"+Utilities.isPeaceful( fme.getFaction() ));
		}
		sender.sendMessage("is wilderness peaceful:"+Utilities.isPeaceful(Factions.i.getNone()));
		sender.sendMessage("is warzone peaceful:"+Utilities.isPeaceful(Factions.i.get(Utilities.ID_WARZONE)));
		sender.sendMessage("is safezone peaceful:"+Utilities.isPeaceful(Factions.i.get(Utilities.ID_SAFEZONE)));
		
		//end temp

		if ( (null != fme) && (fme.isOnline()) && (!sender.isOp())) {
				return;
		}
		
		
		
		String param = this.argAsString(0);
		if ((param != null) && (param.trim().equalsIgnoreCase( "configdiff" ))) {
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
		if (null != fme) {
			Faction f=fme.getFaction();
			if (null != f) {
				sender.sendMessage(Utilities.getCountOfWarps(f) + " warps for faction "+f.getTag());
			}
		}
		sender.sendMessage("--- END ---");
		
	
		
		
	}

}
