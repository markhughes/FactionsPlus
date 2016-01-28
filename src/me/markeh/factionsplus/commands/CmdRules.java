package me.markeh.factionsplus.commands;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.command.requirements.ReqHasFaction;
import me.markeh.factionsframework.command.requirements.ReqIsPlayer;
import me.markeh.factionsframework.objs.Perm;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.conf.FactionData;
import me.markeh.factionsplus.conf.Texts;

public class CmdRules extends FactionsCommand {
	
	public CmdRules() {
		this.aliases.add("rules");
		
		this.requiredPermissions.add(Perm.get("factionsplus.rules", Texts.cmdRules_noPermission));
		
		this.description = Texts.cmdRules_description;
		
		this.addRequirement(ReqHasFaction.get());
		this.addRequirement(ReqIsPlayer.get());
	}
	
	@Override
	public void run() {		
		if ( ! Config.get().enableRules) {
			msg(Texts.rules_notEnabled);
			return;
		}
		
		FactionData fdata = FactionData.get(faction.getID());
		
		if (fdata.rules.isEmpty()) {
			msg(Texts.rules_noRules);
			return;
		}
		
		int i = 0;
		for (String rule : fdata.rules) {
			i++;
			msg(Texts.cmdRules_layout.replace("<i>", String.valueOf(i)).replace("<rule>", rule));
		}
	}
}
