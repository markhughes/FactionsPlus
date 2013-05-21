package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusRules;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;

import org.bukkit.ChatColor;

import com.massivecraft.factions.struct.Permission;

public class CmdRemoveRule extends FPCommand {
	public CmdRemoveRule() {
		this.aliases.add("removerule");
		
		this.errorOnToManyArgs = false;
		
		this.permission = Permission.HELP.node;
		
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = false;
		
		this.setHelpShort("remove a Faction rule");
		
	}
	
	@Override
	public void performfp() {
		if(!FactionsPlus.permission.has(sender, "factionsplus.removerule")) {
			fme.msg(ChatColor.RED + "No permission!");
			return;
		}
		
		if(!Utilities.isLeader(fme) && !Utilities.isOfficer(fme)) {
			fme.msg("Your ranking is not high enough to modify rules.");
		}
		
		if(Utilities.isOfficer(fme) && !Config._rules.officersCanSetRules._) {
			fme.msg("Officers can not modify rules on this server.");
		}
		
		if(Utilities.isLeader(fme) && !Config._rules.leadersCanSetRules._) {
			fme.msg("Leaders can not modify rules on this server.");
		}
		
		try {
			FactionsPlusRules.removeRule(fme.getFaction(), Integer.parseInt(args.get(0).trim()), fme);
		} catch(NumberFormatException e) {
			fme.msg(ChatColor.RED + "Apparently '"+args.get(0)+"' wasn't a number.");
		}

	}
}
