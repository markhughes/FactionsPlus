package markehme.factionsplus.Cmds;

import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.extras.FPPerm;
import markehme.factionsplus.extras.LWCFunctions;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.util.Txt;

public class CmdClearLocks extends FPCommand {
	public CmdClearLocks() {
		this.aliases.add("clearlocks");
		
		this.fpidentifier = "clearlocks";
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		
		this.addRequirements(ReqHasPerm.get(FPPerm.CLEARLWCLOCKS.node));
		
		this.setHelp(LConf.get().cmdDescClearLocks);
		this.setDesc(LConf.get().cmdDescClearLocks);
	}
	
	@Override
	public void performfp() {
		int LWClockRemoveCount = LWCFunctions.clearLocksCommand(usender.getPlayer(), usender.getPlayer().getLocation());
		if(LWClockRemoveCount < 0) {	
			LWClockRemoveCount = 0;
		}
		
		if(LWClockRemoveCount == 0) {
			msg(Txt.parse(LConf.get().LWCNoLocksFound));
		} else {		
			msg(Txt.parse(LConf.get().LWCLocksRemoved, LWClockRemoveCount));
		}
	}

}
