package markehme.factionsplus.Cmds;

import markehme.factionsplus.Utilities;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.util.FPPerm;

import org.bukkit.entity.Player;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.arg.ARFaction;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.util.Txt;

public class CmdFactionHome extends FPCommand {
	Factions factions;
	
	public CmdFactionHome() {
		this.aliases.add("factionhome");
		
		this.fpidentifier = "factionhome";
		
		this.requiredArgs.add("faction");
		this.errorOnToManyArgs = false;
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		
		this.addRequirements(ReqHasPerm.get(FPPerm.FACTIONHOME.node));
		
		this.setHelp(LConf.get().cmdDescFactionHome);
		this.setDesc(LConf.get().cmdDescFactionHome);
	}
	
	@Override
	public void performfp() {
		Faction fTeleport = this.arg(0, ARFaction.get(sender), usenderFaction);
		
		Player player = Utilities.getOnlinePlayerExact(usender);
		
		if(fTeleport == null) {
			msg(Txt.parse(LConf.get().factionHomeFactionNotFound));
		} else {
			if(fTeleport.hasHome()) {
				player.teleport(PS.asBukkitLocation(fTeleport.getHome()));
				msg(Txt.parse(LConf.get().factionHomeTeleportedTo, fTeleport.getName()));
			} else {
				msg(Txt.parse(LConf.get().factionHomeFactionNoHome));
			}
		}
	}
}