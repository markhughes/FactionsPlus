package me.markeh.factionsplus.commands;

import me.markeh.factionsframework.command.FactionsCommand;
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
		
		this.requiredPermissions.add(Perm.get("factionsplus.rules", Texts.cmdJail_noPermission));
		
		this.description = Texts.cmdJail_description;
		
		this.mustHaveFaction = true;
		
		this.errorOnTooManyArgs = false;
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
		
		FactionData fdata = FactionData.get(faction.getID());
		
		String rule = String.join(" ", this.arguments);
		
		fdata.rules.add(rule);
		
		msg(Texts.cmdAddRule_added);
	}
}
