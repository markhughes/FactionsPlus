package markehme.factionsplus.Cmds;

import markehme.factionsplus.Utilities;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.extras.FPPerm;
import markehme.factionsplus.extras.FType;

import com.massivecraft.factions.FFlag;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.util.Txt;

public class CmdToggleState extends FPCommand {
	
	public CmdToggleState() {
		super();
		
		this.aliases.add("toggle");
		this.aliases.add("togglestate");
		this.aliases.add("tog");
		
		this.fpidentifier = "togglestate";
		
		this.optionalArgs.put("faction", "yours");
		this.errorOnToManyArgs = false;
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasPerm.get(FPPerm.TOGGLESTATE.node));
		
		this.setHelp(LConf.get().cmdDescToggleState);
		this.setDesc(LConf.get().cmdDescToggleState);
		
	}
	
	@Override
	public void performfp() {
		
		Faction togglingFaction = usenderFaction;
		
		if(this.arg(0) != null && usenderFaction.getName().equalsIgnoreCase(this.arg(0))) {
			if(!FPPerm.TOGGLESTATEOTHERS.has(sender)) {
				msg(Txt.parse(LConf.get().toggleStateNotOthers));
				return;
			}
			
			togglingFaction = Faction.get(this.arg(0));
		}
		
		if(togglingFaction == null || FType.valueOf(togglingFaction) == FType.WILDERNESS) {
			msg(Txt.parse(LConf.get().toggleStateNonExistant));
			return;
		}
		
		if(!FPUConf.get(usender.getUniverse()).whoCanTogglePeacefulState.get(usender.getRole())) {
			msg(Txt.parse(LConf.get().toggleStateNotHighEnoughRankingToModify));
			return;
		}
		
		if(fpuconf.peacefulToggleDelayInSeconds > 0) {
			if(fData.lastPeacefulToggle > 0) {
				if(System.currentTimeMillis()-fData.lastPeacefulToggle < fpuconf.peacefulToggleDelayInSeconds*1000) {
					msg(Txt.parse(LConf.get().toggleStateMustWait, fpuconf.peacefulToggleDelayInSeconds));
					return;
				}
				
			}
			
			fData.lastPeacefulToggle = System.currentTimeMillis();
			
		}
		
		if(togglingFaction.getFlag(FFlag.PEACEFUL)) {
			if(FPUConf.get(usender.getUniverse()).economyCost.get("toggleUpPeaceful") > 0) {
				if(!Utilities.doCharge(FPUConf.get(usender.getUniverse()).economyCost.get("toggleUpPeaceful"), usender)) {
					msg(Txt.parse(LConf.get().toggleStateCanAffordUp));
					return;
				}
			}
			
			togglingFaction.setFlag(FFlag.PEACEFUL, false);
			msg(Txt.parse(LConf.get().toggleStatePeacefulRemove), togglingFaction.getName());
		} else {
			if(FPUConf.get(usender.getUniverse()).economyCost.get("toggleDownPeaceful") > 0) {
				if(!Utilities.doCharge(FPUConf.get(usender.getUniverse()).economyCost.get("toggleDownPeaceful"), usender)) {
					msg(Txt.parse(LConf.get().toggleStateCanAffordDown));
					return;
				}
			}
			
			togglingFaction.setFlag(FFlag.PEACEFUL, true);
			msg(Txt.parse(LConf.get().toggleStatePeacefulAdd), togglingFaction.getName());
		}
	} 
}