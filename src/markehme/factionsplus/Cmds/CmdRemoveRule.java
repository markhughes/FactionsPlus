package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusRules;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;

import org.bukkit.ChatColor;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;

public class CmdRemoveRule extends FPCommand {
	public CmdRemoveRule() {
		this.aliases.add("removerule");
		
		this.errorOnToManyArgs = false;
		
		this.addRequirements( ReqFactionsEnabled.get() );
		this.addRequirements( ReqIsPlayer.get() );
		this.addRequirements( ReqHasFaction.get() );
		
		this.setHelp( "remove a Faction rule" );
		this.setDesc( "remove a Faction rule" );
		
	}
	
	@Override
	public void performfp() {
		if(!FactionsPlus.permission.has(sender, "factionsplus.removerule")) {
			msg(ChatColor.RED + "No permission!");
			return;
		}
		
		if(!Utilities.isLeader(usender) && !Utilities.isOfficer(usender)) {
			msg("Your ranking is not high enough to modify rules.");
		}
		
		if(Utilities.isOfficer(usender) && !Config._rules.officersCanSetRules._) {
			msg("Officers can not modify rules on this server.");
		}
		
		if(Utilities.isLeader(usender) && !Config._rules.leadersCanSetRules._) {
			msg("Leaders can not modify rules on this server.");
		}
		
		try {
			FactionsPlusRules.removeRule(usender.getFaction(), Integer.parseInt(args.get(0).trim()), usender);
		} catch(NumberFormatException e) {
			// Not a number apparently! 
			msg(ChatColor.RED + "Apparently '"+args.get(0)+"' wasn't a number.");
		}

	}
}
