package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlusJail;
import markehme.factionsplus.Cmds.req.ReqJailsEnabled;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.util.FPPerm;


import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore.util.Txt;



public class CmdUnJail extends FPCommand {
	
	public CmdUnJail() {
		
		this.aliases.add( "unjail" );
		
		this.fpidentifier = "unjail";
		
		this.requiredArgs.add( "player" );
		this.errorOnToManyArgs = false;
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(ReqJailsEnabled.get());
		
		this.addRequirements(ReqHasPerm.get(FPPerm.JAIL.node));
		
		this.setHelp(LConf.get().cmdDescUnJail);
		this.setDesc(LConf.get().cmdDescUnJail);
		
	}
	
	
	@Override
	public void performfp() {
		
		if(!FPUConf.get(usender.getUniverse()).whoCanJail.get(usender.getRole())) {
			msg(Txt.parse(LConf.get().jailsNotHighEnoughRanking));
			return;
		}
		
		FactionsPlusJail.removeFromJail(this.arg(0), usender, true);
	}
}
