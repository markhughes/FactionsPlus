package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.Cmds.req.ReqWarpsEnabled;
import markehme.factionsplus.MCore.FactionData;
import markehme.factionsplus.MCore.FactionDataColls;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.extras.FType;
import markehme.factionsplus.util.FPPerm;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.mcore.cmd.req.ReqHasPerm;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;
import com.massivecraft.mcore.util.Txt;

public class CmdListWarps extends FPCommand  {
	public CmdListWarps() {
		this.aliases.add("listwarps");
		
		this.fpidentifier = "listwarps";
		
		this.optionalArgs.put("faction", "string");
		this.errorOnToManyArgs = true;
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(ReqWarpsEnabled.get());
		
		this.addRequirements(ReqHasPerm.get(FPPerm.LISTWARPS.node));
		
		this.setHelp(LConf.get().cmdDescListWarps);
		this.setDesc(LConf.get().cmdDescListWarps);
	}
	
	@Override
	public void performfp() {
		Faction currentFaction = usenderFaction;
		
		// If there is an argument, update the currentFaction
		if(this.arg(0) != null) {

			currentFaction = Faction.get(arg(0));
			
			// If the Faction doesn't exist, ensure we give the correct response 
			if(currentFaction == null && FactionsPlus.permission.has(usender.getPlayer(), FPPerm.LISTWARPSOTHERS.node)) {
				msg(Txt.parse(LConf.get().warpsCantViewListNonExistant));
				return;
			} else if(currentFaction == null && !FactionsPlus.permission.has(usender.getPlayer(), FPPerm.LISTWARPSOTHERS.node)) {
				msg(Txt.parse(LConf.get().warpsCantViewOthersWarps));
				return;
			}
			
			// If the Factions is not the users original, ensure we have permission to do this
			if(currentFaction.getId() != usenderFaction.getId()) { 
				if(!FactionsPlus.permission.has(usender.getPlayer(), FPPerm.LISTWARPSOTHERS.node)) {
					msg(Txt.parse(LConf.get().warpsCantViewOthersWarps));
					return;
				}
			}
		}
		
		// Do not view Wilderness warps, and ensure we're using a valid Faction
		if(FType.valueOf(currentFaction) == FType.WILDERNESS || currentFaction == null) {
			msg(Txt.parse(LConf.get().warpsCantViewListNonExistant));
			return;
		}
		
		// Fetch the FactionData for this Faction
		FactionData fData = FactionDataColls.get().getForUniverse(usender.getUniverse()).get(currentFaction.getId());
		
		// Ensure there are warps
		if(fData.warpLocation.size() == 0) {
			msg(Txt.parse(LConf.get().warpsFactionHasNone));
			return;
		}
		
		// Loop through each of the values and build a list 
		String warpList = "";
		int i = 0;
		
		for(String warpName : fData.warpLocation.keySet()) {
			i++;
			
			// Disclose passwords to the leader
			String addition = "";
			
			if(usender.getRole() == Rel.LEADER) {
				addition = " (" + fData.warpPasswords.get(warpName) + ")";
			}
			
			// Make it into a displayable list 
			if(i != 1) {
				warpList = warpList + ", " + warpName + addition;
			} else {
				warpList = warpName + addition;
			}
		}
		
		// Send the message back 
		msg(Txt.parse(LConf.get().warpsListPrefix, warpList));
	}
}
