package markehme.factionsplus.Cmds;

import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.util.FPPerm;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.arg.ARFaction;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;

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
		Faction currentF = this.arg(0, ARFaction.get(sender));
				
		if(currentF == null) {
			msg(Txt.parse(LConf.get().factionHomeFactionNotFound));
		} else {
			if(currentF.hasHome()) {
				me.teleport(PS.asBukkitLocation(currentF.getHome()));
				msg(Txt.parse(LConf.get().factionHomeTeleportedTo, currentF.getName()));
			} else {
				msg(Txt.parse(LConf.get().factionHomeFactionNoHome));
			}
		}
	}
}