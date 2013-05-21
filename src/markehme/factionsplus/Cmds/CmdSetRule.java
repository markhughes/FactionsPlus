package markehme.factionsplus.Cmds;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.bukkit.ChatColor;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusRules;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.FactionsBridge.Bridge;
import markehme.factionsplus.config.Config;

import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TextUtil;

public class CmdSetRule extends FPCommand{
	
	public CmdSetRule() {
		this.aliases.add("setrule");
		this.aliases.add("addrule");
		
		this.errorOnToManyArgs = false;
		
		this.requiredArgs.add("rule");
		//this.optionalArgs.put("on/off", "flip");

		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = false;
		
		this.setHelpShort("manage Faction rules");
	}
	
	@Override
	public void performfp() {
		if(!FactionsPlus.permission.has(sender, "factionsplus.setrules")) {
			sender.sendMessage(ChatColor.RED + "No permission!");
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
		
		String newRule = TextUtil.implode(args, " ").replaceAll("(&([a-f0-9]))", "& $2");
		
		fme.msg("Adding rule: " + newRule);
		
		FactionsPlusRules.setRuleForFaction(fme.getFaction(), fme, newRule);
		
	}
}
