package markehme.factionsplus.Cmds;

import markehme.factionsplus.Cmds.req.ReqFactionsPlusEnabled;
import markehme.factionsplus.Cmds.req.ReqScoreboardEnabled;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.extras.FPPerm;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.util.Txt;

public class CmdScoreboard extends FPCommand {
	public CmdScoreboard() {
		this.aliases.add("scoreboard");
		this.aliases.add("sb");
		
		// Unique identifier for this command 
		this.fpidentifier = "scoreboard";
		
		this.optionalArgs.put("visibility", "hide/show");
		
		this.addRequirements(ReqFactionsEnabled.get());
		
		// Ensure FactionsPlus is enabled
		this.addRequirements(ReqFactionsPlusEnabled.get());
		
		// Ensure Warps are enabled
		this.addRequirements(ReqScoreboardEnabled.get());
		
		this.addRequirements(ReqIsPlayer.get());
		
		this.addRequirements(ReqHasPerm.get(FPPerm.SCOREBOARD.node));
		
		this.setHelp(LConf.get().cmdDescScoreboard);
		this.setDesc(LConf.get().cmdDescScoreboard);

	}
	
	@Override
	public void performfp() {
		if(args.size() > 0) {
			if(args.get(0).equalsIgnoreCase("hide")) {
				if(fData.membersHidingScoreboard.contains(me.getUniqueId().toString())) {
					msg(Txt.parse(LConf.get().scoreboardAlreadyHidden));
					return;
				}
				
				fData.membersHidingScoreboard.add(me.getUniqueId().toString());
				msg(Txt.parse(LConf.get().scoreboardToggled));
				
			} else if(args.get(0).equalsIgnoreCase("show")) {
				if(!fData.membersHidingScoreboard.contains(me.getUniqueId().toString())) {
					msg(Txt.parse(LConf.get().scoreboardAlreadyShowing));
					return;
				}
				
				fData.membersHidingScoreboard.remove(me.getUniqueId().toString());
				msg(Txt.parse(LConf.get().scoreboardToggled));
				
			} else {
				msg(Txt.parse(LConf.get().scoreboardBadArgument));
				
			}
			
			return;
		} 
		
		if(fData.membersHidingScoreboard.contains(me.getUniqueId().toString())) {
			fData.membersHidingScoreboard.remove(me.getUniqueId().toString());
		} else {
			fData.membersHidingScoreboard.add(me.getUniqueId().toString());
		}
	}
}
