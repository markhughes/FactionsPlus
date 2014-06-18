package markehme.factionsplus.Cmds;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import markehme.factionsplus.FactionsPlusJail;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.Cmds.req.ReqJailsEnabled;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.util.FPPerm;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.util.Txt;

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
		
		if(!FPUConf.get(usender.getUniverse()).whoCanJail.get(usender.getRole())) {
			msg(Txt.parse(LConf.get().jailsNotHighEnoughRanking));
			return;
		}
		
		if(FPUConf.get(usender.getUniverse()).mustBeInOwnTerritoryToJail && !usender.isInOwnTerritory()) {
			msg(Txt.parse(LConf.get().jailsMustBeInOwnTerritoryToJail));	
			return;
		}
		
		if(FPUConf.get(usender.getUniverse()).economyCost.get("jail") > 0) {
			if(!Utilities.doCharge(FPUConf.get(usender.getUniverse()).economyCost.get("jail"), usender)) {
				msg(Txt.parse(LConf.get().jailsCantAffordToJail, FPUConf.get(usender.getUniverse()).economyCost.get("jail")));
				return;
			}
		}
		
		OfflinePlayer bPlayer = Bukkit.getPlayer(playerToJail);
		
		if(bPlayer == null) {
			bPlayer = Bukkit.getOfflinePlayer(playerToJail);
			
			if(bPlayer == null) {
				// ??
				return;
			}
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
		
		fData.jailedPlayerIDs.put(bPlayer.getUniqueId().toString(), PS.valueOf(respawnLoc));
		
	}
}