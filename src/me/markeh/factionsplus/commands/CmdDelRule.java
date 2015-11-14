package me.markeh.factionsplus.commands;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.objs.Perm;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.conf.FactionData;
import me.markeh.factionsplus.conf.Texts;

public class CmdDelRule extends FactionsCommand {
	public CmdDelRule() {
		this.aliases.add("delrule");
		this.requiredArguments.add("rule number");
		
		this.requiredPermissions.add(Perm.get("factionsplus.rules", Texts.cmdAddRule_noPermission));
		
		this.description = Texts.cmdAddRule_description;
		
		this.mustHaveFaction = true;
		
	}
	
	// ----------------------------------------
	// Methods
	// ----------------------------------------
	
	@Override
	public void run() {
		if ( ! this.isPlayer() ) {
			msg(Texts.playerOnlyCommand);
			return; 
		}
		
		if ( ! Config.get().enableRules) {
			msg(Texts.rules_notEnabled);
			return;
		}

		if ( ! fplayer.isLeader() && !fplayer.isOfficer()) {
			msg(Texts.cmdAddRule_badRank);
			return;
		}
		
		FactionData fdata = FactionData.get(fplayer.getFaction().getID());
		
		if (fdata.rules.size() >= Integer.valueOf(this.getArg(0))-1) {
			fdata.rules.remove(Integer.valueOf(this.getArg(0))-1);
			msg("<green>Rule removed.");
		} else {
			msg("<red>That rule does not exist!");
		}
		
	}
}
