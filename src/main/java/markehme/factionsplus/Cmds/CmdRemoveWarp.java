package markehme.factionsplus.Cmds;

import markehme.factionsplus.Utilities;
import markehme.factionsplus.Cmds.req.ReqWarpsEnabled;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.util.FPPerm;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.util.Txt;

public class CmdRemoveWarp extends FPCommand {
	public CmdRemoveWarp() {
		
		this.aliases.add("removewarp");
		this.aliases.add("deletewarp");
		this.aliases.add("unsetwarp");
		
		this.fpidentifier = "removewarp";
		
		this.requiredArgs.add("name");
		
		this.errorOnToManyArgs = true;
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(ReqWarpsEnabled.get());
		
		this.addRequirements(ReqHasPerm.get(FPPerm.CREATEWARP.node));
		
		this.setHelp(LConf.get().cmdDescRemoveWarp);
		this.setDesc(LConf.get().cmdDescRemoveWarp);
		
	}
	
	@Override
	public void performfp() {
		String warpname = this.arg(0);
		
		// Ensure we are a high enough rank to remove (aka set) warps 
		if(!FPUConf.get(usender.getUniverse()).whoCanSetWarps.get(usender.getRole())) {
			msg(Txt.parse(LConf.get().warpsNotHighEnoughRankingToModify));
			return;
		}
				
		// Check that warp exists
		if(!fData.warpExists(warpname)) {
			msg(Txt.parse(LConf.get().warpsCantDeleteNonExistant));
			return;
		}
		
		// Make any charges as necessary 
		if(FPUConf.get(usender.getUniverse()).economyCost.get("deletewarp") > 0.0) {
			if(!Utilities.doCharge(FPUConf.get(usender.getUniverse()).economyCost.get("deletewarp"), usender)) {
				msg(Txt.parse(LConf.get().warpsCanNotAffordDelete, FPUConf.get(usender.getUniverse()).economyCost.get("deletewarp")));
				return;
			}
		}
		
		// Remove the data from the lists directly 
		fData.warpLocation.remove(warpname);
		fData.warpPasswords.remove(warpname);
		
		// Notify them of the success
		msg(Txt.parse(LConf.get().warpsNotifyRemoveSuccess, warpname));

	}
}
