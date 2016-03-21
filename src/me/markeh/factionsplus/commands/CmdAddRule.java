package me.markeh.factionsplus.commands;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.command.requirements.ReqHasFaction;
import me.markeh.factionsframework.command.requirements.ReqIsPlayer;
import me.markeh.factionsframework.command.requirements.ReqPermission;
import me.markeh.factionsframework.objs.Perm;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.conf.FactionData;
import me.markeh.factionsplus.conf.Texts;

public class CmdAddRule extends FactionsCommand {

	// ----------------------------------------
	// Constructor
	// ----------------------------------------

	public CmdAddRule() {
		this.aliases.add("addrule");
		this.requiredArguments.add("rule");
		
		this.description = Texts.get().cmdAddRule_description;
		
		this.setErrorOnTooManyArgs(false);
		
		this.addRequirement(ReqHasFaction.get());
		this.addRequirement(ReqIsPlayer.get());
		this.addRequirement(ReqPermission.get(Perm.get("factionsplus.rules", Texts.get().cmdAddRule_noPermission)));
	}
	
	// ----------------------------------------
	// Methods
	// ----------------------------------------
	
	@Override
	public void run() {
		if ( ! Config.get().enableRules) {
			msg(Texts.get().rules_notEnabled);
			return;
		}

		if ( ! getFPlayer().isLeader() && ! getFPlayer().isOfficer()) {
			msg(Texts.get().cmdAddRule_badRank);
			return;
		}
		
		FactionData fdata = FactionData.get(getFaction());
		
		String rule = String.join(" ", this.arguments);
		
		fdata.rules.add(rule);
		
		msg(Texts.get().cmdAddRule_added);
	}
}
