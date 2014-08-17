package markehme.factionsplus.Cmds;

import org.bukkit.entity.Player;

import markehme.factionsplus.Utilities;
import markehme.factionsplus.Cmds.req.ReqBansEnabled;
import markehme.factionsplus.MCore.FactionData;
import markehme.factionsplus.MCore.FactionDataColls;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.util.FPPerm;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.util.Txt;

public class CmdUnban extends FPCommand {
	public CmdUnban() {
		this.aliases.add("unban");
		
		this.fpidentifier = "unban";
		
		this.requiredArgs.add("player");
		
		this.errorOnToManyArgs = false;
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(ReqBansEnabled.get());
		
		this.addRequirements(ReqHasPerm.get(FPPerm.BAN.node));

		this.setHelp(LConf.get().cmdDescUnBan);
		this.setDesc(LConf.get().cmdDescUnBan);
	}
	
	@Override
	public void performfp(){
		
		if(!FPUConf.get(usender.getUniverse()).whoCanBan.containsKey(usender.getRole())) {
			msg(Txt.parse(LConf.get().banNotHighEnoughRanking));
			return;
		}
		
		if(!FPUConf.get(usender.getUniverse()).whoCanBan.get(usender.getRole())) {
			msg(Txt.parse(LConf.get().banNotHighEnoughRanking));
			return;
		}
		
		FactionData fdata = FactionDataColls.get().getForUniverse(usender.getUniverse()).get(usender.getFactionId());
				
		Player unbanPlayer = Utilities.getPlayer(args.get(0));
		
		if(unbanPlayer == null) {
			msg(Txt.parse("<red>This player hasn't been on the server before and you therefore can't unban them."));
			return;
		}
		
		// Remove that ID from the ban list
		if(!fdata.bannedPlayerIDs.containsKey(unbanPlayer.getUniqueId().toString())) {
			msg(Txt.parse(LConf.get().banPlayerNotBanned));
			return;
		}
		fdata.bannedPlayerIDs.remove(unbanPlayer.getUniqueId().toString());
		
	}
}
