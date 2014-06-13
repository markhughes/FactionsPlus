package markehme.factionsplus.Cmds;


import markehme.factionsplus.Utilities;
import markehme.factionsplus.Cmds.req.ReqJailsEnabled;
import markehme.factionsplus.MCore.FactionData;
import markehme.factionsplus.MCore.FactionDataColls;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.util.FPPerm;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.util.Txt;

public class CmdSetJail extends FPCommand {
	public CmdSetJail() {
		this.aliases.add("setjail");
		
		this.fpidentifier = "setjail";
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(ReqJailsEnabled.get());
		
		this.addRequirements(ReqHasPerm.get(FPPerm.SETJAIL.node));
		
		this.setHelp(LConf.get().cmdDescSetJail);
		this.setDesc(LConf.get().cmdDescSetJail);
	}
	
	@Override
	public void performfp() {
		
		// Ensure this player can set jails
		if(!FPUConf.get(usender.getUniverse()).whoCanSetJails.get(usender.getRole())) {
			msg(Txt.parse(LConf.get().jailsNotHighEnoughRanking));
			return;
		}
		
		// Ensure the player is in their own territory 
		if(!usender.isInOwnTerritory()) {
			msg(Txt.parse(LConf.get().jailsOnlySetInOwnTerritory));
			return;
		}
		
		// Ensure the player pays if required 
		if(FPUConf.get(usender.getUniverse()).economyCost.get("setjail") > 0) {
			if(!Utilities.doCharge(FPUConf.get(usender.getUniverse()).economyCost.get("setjail"), usender)) {
				msg(Txt.parse(LConf.get().jailsCantAffordToJail, FPUConf.get(usender.getUniverse()).economyCost.get("setjail")));
				return;
			}
		}
		
		// Fetch the FactionData
		FactionData fData = FactionDataColls.get().getForUniverse(usender.getUniverse()).get(usenderFaction.getId());
		
		// Update the location
		fData.jailLocation = PS.valueOf(usender.getPlayer().getLocation());
		
		msg(Txt.parse(LConf.get().jailsSetLocation));

	}
}
