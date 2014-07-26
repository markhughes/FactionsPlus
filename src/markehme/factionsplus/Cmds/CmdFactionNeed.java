package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.util.FPPerm;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.util.Txt;

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
		
		if(this.args.size() > 0) {
			if(this.arg(0).equalsIgnoreCase("ignore") && FactionsPlus.permission.has(sender, "factionsplus.ignoreneeds")) {
				if(!FactionsPlus.whosIgnoringNeeds.contains(me.getUniqueId())) {
					FactionsPlus.whosIgnoringNeeds.add(me.getUniqueId());
					msg(Txt.parse(LConf.get().factionNeedNowIgnoring));
				} else {
					msg(Txt.parse(LConf.get().factionNeedAlreadyIgnoring));
				}
			} else if(this.arg(0).equalsIgnoreCase("listen") && FactionsPlus.permission.has(sender, "factionsplus.ignoreneeds")) {
				if(FactionsPlus.whosIgnoringNeeds.contains(me.getUniqueId())) {
					FactionsPlus.whosIgnoringNeeds.remove(me.getUniqueId());
					msg(Txt.parse(LConf.get().factionNeedNowListening));
				} else {
					msg(Txt.parse(LConf.get().factionNeedAlreadyListening));
				}
			}
			
			if(!FactionsPlus.permission.has(sender, "factionsplus.ignoreneeds")) {
				
			}
		}
		// TODO: Move this into a thread
		// TODO: Cooldown? 
		
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