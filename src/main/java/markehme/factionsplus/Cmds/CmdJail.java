package markehme.factionsplus.Cmds;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.Cmds.req.ReqJailsEnabled;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.extras.FPPerm;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;

public class CmdJail extends FPCommand {
	
	public CmdJail() {
		this.aliases.add("jail");
		
		this.fpidentifier = "jail";
		
		this.requiredArgs.add("player");
		this.errorOnToManyArgs = false;
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(ReqJailsEnabled.get());
		
		this.addRequirements(ReqHasPerm.get(FPPerm.JAIL.node));
		
		this.setHelp(LConf.get().cmdDescJail);
		this.setDesc(LConf.get().cmdDescJail);
		
	}

	@Override
	public void performfp() {
		String playerToJail = this.arg(0);
		
		if(fpuconf.whoCanJail.get(usender.getRole())) {
			msg(Txt.parse(LConf.get().jailsNotHighEnoughRanking));
			return;
		}
		
		if(fpuconf.mustBeInOwnTerritoryToJail && !usender.isInOwnTerritory()) {
			msg(Txt.parse(LConf.get().jailsMustBeInOwnTerritoryToJail));	
			return;
		}
		
		if(fpuconf.economyCost.get("jail") > 0) {
			if(!Utilities.doCharge(fpuconf.economyCost.get("jail"), usender)) {
				msg(Txt.parse(LConf.get().jailsCantAffordToJail, fpuconf.economyCost.get("jail")));
				return;
			}
		}
		
		final OfflinePlayer bPlayer = Utilities.getPlayer(playerToJail);
		
		if(bPlayer == null) {
			msg(Txt.parse("<red>This player hasn't been on the server before and you therefore can't jail them."));
			return;
		}
		
		Location respawnLoc = null;
		
		if(bPlayer.isOnline()) {
			respawnLoc = bPlayer.getPlayer().getLocation();
		} else {
			if(bPlayer.getBedSpawnLocation() != null) {
				respawnLoc = bPlayer.getBedSpawnLocation();
			} else {
				respawnLoc = null;
			}
		}
		
		final Location finalRespawn = respawnLoc;
		
		if(fpuconf.delayBeforeSentToJail > 0) {
			int delay = fpuconf.delayBeforeSentToJail*20;
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(FactionsPlus.instance, new Runnable() {

				@Override
				public void run() {
					fData.jailedPlayerIDs.put(bPlayer.getUniqueId().toString(), PS.valueOf(finalRespawn));
				}
				
			}, delay);
		} else {
			fData.jailedPlayerIDs.put(bPlayer.getUniqueId().toString(), PS.valueOf(finalRespawn));
		}
		
		
	}
}