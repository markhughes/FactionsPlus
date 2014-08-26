package markehme.factionsplus.Cmds;

import markehme.factionsplus.Cmds.req.ReqRulesEnabled;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.util.FPPerm;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.util.Txt;

public class CmdRemoveRule extends FPCommand {
	public CmdRemoveRule() {
		this.aliases.add("removerule");
		
		this.fpidentifier = "removerule";
		
		this.errorOnToManyArgs = false;
		
		this.requiredArgs.add("rule");
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(ReqRulesEnabled.get());
		
		this.addRequirements(ReqHasPerm.get(FPPerm.MODIFYRULES.node));
		
		this.setHelp(LConf.get().cmdDescRemoveRule);
		this.setDesc(LConf.get().cmdDescRemoveRule);
		
	}
	
	@Override
	public void performfp() {
		
		if(!FPUConf.get(usender.getUniverse()).whoCanSetRules.get(usender.getRole())) {
			msg(Txt.parse(LConf.get().rulesNotHighEnoughRankingToModify));
			return;
		}
				
		try {
			// Remove the rule directly from the list 
			// Go one less (as we actually start at 0)
			int startNo = fData.rules.size();
			fData.rules.remove(Integer.parseInt(args.get(0).trim())-1); 
			
			if(fData.rules.size() == startNo) {
				// No changes detected in size aka no rules were removed
				msg(Txt.parse(LConf.get().rulesNoRuleRemoved));
			} else {
				// Change detected, so the rule was removed 
				msg(Txt.parse(LConf.get().rulesNotifyRemoved));
			}
		} catch(NumberFormatException e) {
			// NumberFormatException = not a valid number
			msg(Txt.parse(LConf.get().rulesErrorNotNumber, args.get(0)));
		}

	}
}
