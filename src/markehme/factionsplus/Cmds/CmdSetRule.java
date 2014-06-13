package markehme.factionsplus.Cmds;

import markehme.factionsplus.Cmds.req.ReqRulesEnabled;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.util.FPPerm;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore.util.Txt;

public class CmdSetRule extends FPCommand{
	
	public CmdSetRule() {
		this.aliases.add("setrule");
		this.aliases.add("addrule");
		
		this.fpidentifier = "setrule";
		
		this.requiredArgs.add("rule");
		this.errorOnToManyArgs = false;
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(ReqRulesEnabled.get());
		
		this.addRequirements(ReqHasPerm.get(FPPerm.MODIFYRULES.node));

		this.setHelp(LConf.get().cmdDescSetRule);
		this.setDesc(LConf.get().cmdDescSetRule);
	}
	
	@Override
	public void performfp() {
		
		if(!FPUConf.get(usender.getUniverse()).whoCanSetJails.get(usender.getRole())) {
			msg(Txt.parse(LConf.get().rulesNotHighEnoughRankingToModify));
			return;
		}
		
		if(usenderFaction.isNone()) {
			msg(Txt.parse(LConf.get().rulesNotInFaction));
			return;
		}
		
		String newRule = Txt.implode(args, " ").replaceAll("(&([a-f0-9]))", "& $2");
		
		if(fData.rules.size() >= FPUConf.get(usender.getUniverse()).maxRulesPerFaction) {
			msg(Txt.parse(LConf.get().rulesHitMax), FPUConf.get(usender.getUniverse()).maxRulesPerFaction);
			return;
		}
		
		fData.rules.add(newRule);
		
	}
}
