package markehme.factionsplus.Cmds;

import org.bukkit.Bukkit;

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
		
		if(!FPUConf.get(usender.getUniverse()).whoCanBan.get(usender.getRole())) {
			msg(Txt.parse(LConf.get().banNotHighEnoughRanking));
			return;
		}
		
		FactionData fdata = FactionDataColls.get().getForUniverse(usender.getUniverse()).get(usender.getFactionId());
		
		String id = null;
		
		if(Bukkit.getPlayer(args.get(0)) != null) {
			id = Bukkit.getPlayer(args.get(0)).getUniqueId().toString();
		} else {
			// TODO: get ID from Mojang API
		}
		
		// Remove that ID from the ban list
		fdata.bannedPlayerIDs.remove(id);
		
	}
}
