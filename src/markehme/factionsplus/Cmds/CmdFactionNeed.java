package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusTemplates;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.config.OldConfig;
import markehme.factionsplus.util.FPPerm;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore.util.Txt;

public class CmdFactionNeed extends FPCommand {
	
	public CmdFactionNeed() { 
		this.aliases.add("need");
		
		this.fpidentifier = "need";
		
		this.errorOnToManyArgs = false;
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		
		this.addRequirements(ReqHasPerm.get(FPPerm.NEED.node));
		
		this.setHelp(LConf.get().cmdDescFactionNeed);
		this.setDesc(LConf.get().cmdDescFactionNeed);
	}
	
	@Override
	public void performfp() {		
		if(usender.hasFaction()) {
			msg(Txt.parse(LConf.get().factionNeedAlreadyHaveFaction));	
			return;
		}
		
		// TODO: Move this into a thread
		// TODO: Cooldown? 
		// TODO: Allow certain Factions to ignore these messages ( /f needs ignore | /f needs listen )
		
		int i = 0;
		
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			if(UPlayer.get(p).getRole() == Rel.LEADER || UPlayer.get(p).getRole() == Rel.OFFICER) {
				if(!FactionsPlus.permission.has(p, "factionsplus.ignoreneeds")) {        			
					i++;
					p.sendMessage(Txt.parse(LConf.get().factionNeedNotification, sender.getName(), sender.getName()));
				}
			}
		}
		
		if(i == 0) {
			// Notify the player that no one received the need request
			msg(Txt.parse(LConf.get().factionNeedClarifyNoneSent));
		} else {
			// Notify player that they received the need request
			msg(Txt.parse(LConf.get().factionNeedClarifySent));
		}
	}
}