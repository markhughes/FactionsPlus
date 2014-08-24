package markehme.factionsplus.Cmds;

import markehme.factionsplus.Cmds.req.ReqJailsEnabled;
import markehme.factionsplus.MCore.FactionData;
import markehme.factionsplus.MCore.FactionDataColls;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.util.FPPerm;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.util.Txt;

public class CmdUnsetJail extends FPCommand {
	public CmdUnsetJail() {
		this.aliases.add("unsetjail");
		this.aliases.add("removejail");
		
		this.fpidentifier = "unsetjail";
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(ReqJailsEnabled.get());
		
		this.addRequirements(ReqHasPerm.get(FPPerm.SETJAIL.node));
		
		this.setHelp(LConf.get().cmdDescUnSetJail);
		this.setDesc(LConf.get().cmdDescUnSetJail);
	}
	
	@Override
	public void performfp() {
		
		// Ensure we can remove the jail here
		if(FPUConf.get(usender.getUniverse()).whoCanSetJails.get(usender.getRole())) {
			msg(Txt.parse(LConf.get().jailsNotHighEnoughToModifyTheJail));
			return;
		}
		
		// Fetch the FactionData object 
		FactionData fData = FactionDataColls.get().getForUniverse(usender.getUniverse()).get(usenderFaction.getId());
		
		// Remove the jail
		fData.jailLocation = null;
		
		// Notify the player it has been removed 
		msg(Txt.parse(LConf.get().jailsHasBeenRemoved));
	}
	
}
