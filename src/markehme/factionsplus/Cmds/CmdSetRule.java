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
import markehme.factionsplus.config.Config;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore.util.Txt;

public class CmdSetRule extends FPCommand{
	
	public CmdSetRule() {
		this.aliases.add("setrule");
		this.aliases.add("addrule");
		
		this.requiredArgs.add("rule");
		this.errorOnToManyArgs = false;
		
		this.addRequirements( ReqFactionsEnabled.get() );
		this.addRequirements( ReqIsPlayer.get() );
		this.addRequirements( ReqHasFaction.get() );
		
		this.setHelp("set Faction rules");
		this.setDesc("set Faction rules");
	}
	
	@Override
	public void performfp() {
		if(!FactionsPlus.permission.has(sender, "factionsplus.setrules")) {
			sender.sendMessage(ChatColor.RED + "No permission!");
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
		
		String newRule = Txt.implode(args, " ").replaceAll("(&([a-f0-9]))", "& $2");
				
		FactionsPlusRules.setRuleForFaction(usender.getFaction(), usender, newRule);
		
	}
}
