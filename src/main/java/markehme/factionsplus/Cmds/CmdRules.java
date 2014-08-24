package markehme.factionsplus.Cmds;

import markehme.factionsplus.Cmds.req.ReqRulesEnabled;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.util.FPPerm;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.util.Txt;

public class CmdRules extends FPCommand {

	public CmdRules() {
		
		this.aliases.add("rules");
		this.aliases.add("viewrules");
		this.aliases.add("listrules");
		
		this.fpidentifier = "rules";
		
		this.errorOnToManyArgs = false;
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(ReqRulesEnabled.get());
		
		this.addRequirements(ReqHasPerm.get(FPPerm.RULES.node));
		
		this.setHelp(LConf.get().cmdDescListRules);
		this.setDesc(LConf.get().cmdDescListRules);
		
	}
	
	@Override
	public void performfp() {
		int i = 0;
		
		msg(Txt.parse(LConf.get().rulesListingStart));
		
		for(String rule : fData.rules) {
			i++;
			msg(i+". " + rule);
		}
		
	}
}
