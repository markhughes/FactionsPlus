package markehme.factionsplus.Cmds;



import markehme.factionsplus.Utilities;
import markehme.factionsplus.Cmds.req.ReqAnnouncementsEnabled;
import markehme.factionsplus.MCore.FactionData;
import markehme.factionsplus.MCore.FactionDataColls;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.util.FPPerm;


import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore.util.Txt;

public class CmdAnnounce extends FPCommand {
	public CmdAnnounce() {
		
		this.aliases.add("announce");
		
		this.fpidentifier = "announce";
		
		this.requiredArgs.add("message");
		
		this.errorOnToManyArgs = false;
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(ReqAnnouncementsEnabled.get());
		 
		this.addRequirements(ReqHasPerm.get(FPPerm.ANNOUNCE.node));
		
		this.setHelp(LConf.get().cmdDescAnnounce);
		this.setDesc(LConf.get().cmdDescAnnounce);
		
	}
	
	@Override
	public void performfp() {
		// Ensure there is a high enough ranking
		if(!FPUConf.get(usender.getUniverse()).whoCanAnnounce.get(usender.getRole())) {
			msg(Txt.parse(LConf.get().announcementNotHighEnoughRankingToSet));
			return;
		}
		
		if(FPUConf.get(usender.getUniverse()).economyCost.get("announce") > 0.0) {
			if(!Utilities.doCharge(FPUConf.get(usender.getUniverse()).economyCost.get("announce"), usender)) {
				msg(Txt.parse(LConf.get().announcementCanNotAfford, FPUConf.get(usender.getUniverse()).economyCost.get("announce")));
				return;
			}
		}
		
		// Get the message to be announced
		String message = Txt.implode(args, " ").replaceAll("(&([a-f0-9]))", "& $2");
		
		// Store the announcement data in their FactionData before we apply formatting 
		FactionData fData = FactionDataColls.get().getForUniverse(usender.getUniverse()).get(usenderFaction.getId());
		fData.announcement = message;
		
		// If colours are enabled for announcements, we will parse it
		if(FPUConf.get(usender.getUniverse()).allowColoursInAnnouncements) {
			message = Txt.parse(message);
		}
		
		// Announce it to the Faction
		usenderFaction.msg(Txt.parse(LConf.get().announcementNotify, sender.getName(), message));
		
	}

}
