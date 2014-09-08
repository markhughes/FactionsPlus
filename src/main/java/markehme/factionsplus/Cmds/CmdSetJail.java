package markehme.factionsplus.Cmds;

import markehme.factionsplus.Utilities;
import markehme.factionsplus.Cmds.req.ReqJailsEnabled;
import markehme.factionsplus.MCore.FactionData;
import markehme.factionsplus.MCore.FactionDataColls;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.extras.FPPerm;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;

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
		if(!fpuconf.whoCanSetJails.get(usender.getRole())) {
			msg(Txt.parse(LConf.get().jailsNotHighEnoughRanking));
			return;
		}
		
		// Ensure the player is in their own territory 
		if(!usender.isInOwnTerritory()) {
			msg(Txt.parse(LConf.get().jailsOnlySetInOwnTerritory));
			return;
		}
		
		// Ensure the player pays if required 
		if(fpuconf.economyCost.get("setJail") > 0) {
			if(!Utilities.doCharge(fpuconf.economyCost.get("setJail"), usender)) {
				msg(Txt.parse(LConf.get().jailsCantAffordToJail, fpuconf.economyCost.get("setJail")));
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
