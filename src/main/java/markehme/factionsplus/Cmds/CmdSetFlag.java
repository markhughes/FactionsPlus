package markehme.factionsplus.Cmds;

import markehme.factionsplus.Utilities;
import markehme.factionsplus.Cmds.req.ReqFactionsPlusEnabled;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.MCore.LConf;

import com.massivecraft.factions.FFlag;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.util.Txt;

public class CmdSetFlag extends FPCommand {
	public CmdSetFlag() {
		this.aliases.add("setflag");
		
		this.fpidentifier = "setflag";
		
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqFactionsPlusEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		
		this.requiredArgs.add("flag");
		this.requiredArgs.add("value");
		
		this.setHelp(LConf.get().cmdDescSetFlag);
		this.setDesc(LConf.get().cmdDescSetFlag);
	}

	@Override
	protected void performfp() {
		if(!fpuconf.whoCanSetFlags.containsKey(usender.getRole())) {
			msg(Txt.parse(LConf.get().setFlagCommandNoPermission, args.get(0)));
			return;
		}
		
		if(!fpuconf.whoCanSetFlags.get(usender.getRole())) {
			msg(Txt.parse(LConf.get().setFlagCommandNoPermission, args.get(0)));
			return;
		}
		
		if(FFlag.parse(args.get(0)) == null) {
			msg(Txt.parse(LConf.get().setFlagCommandInvalid, args.get(0)));
			return;
		}
		
		
		if(!me.hasPermission("factionsplus.setflag."+args.get(0))) {
			msg(Txt.parse(LConf.get().setFlagCommandNoPermission, args.get(0)));
			return;
		}
		
		// Check peaceful toggle stuff
		if(FFlag.parse(args.get(0)).equals(FFlag.PEACEFUL)) {
			
			// Delay
			if(fpuconf.peacefulToggleDelayInSeconds > 0) {
				if(fData.lastPeacefulToggle > 0) {
					if(System.currentTimeMillis()-fData.lastPeacefulToggle < fpuconf.peacefulToggleDelayInSeconds*1000) {
						msg(Txt.parse(LConf.get().toggleStateMustWait, fpuconf.peacefulToggleDelayInSeconds));
						return;
					}
					
				}
				
				fData.lastPeacefulToggle = System.currentTimeMillis();
				
			}
			
			// Cost
			if(usenderFaction.getFlag(FFlag.PEACEFUL)) {
				if(FPUConf.get(usender.getUniverse()).economyCost.get("toggleUpPeaceful") > 0) {
					if(!Utilities.doCharge(FPUConf.get(usender.getUniverse()).economyCost.get("toggleUpPeaceful"), usender)) {
						msg(Txt.parse(LConf.get().toggleStateCanAffordUp));
						return;
					}
				}
			} else {
				if(FPUConf.get(usender.getUniverse()).economyCost.get("toggleDownPeaceful") > 0) {
					if(!Utilities.doCharge(FPUConf.get(usender.getUniverse()).economyCost.get("toggleDownPeaceful"), usender)) {
						msg(Txt.parse(LConf.get().toggleStateCanAffordDown));
						return;
					}
				}
			}
		}
		
		usenderFaction.setFlag(FFlag.parse(args.get(0)), Boolean.valueOf(args.get(1)));
		msg(Txt.parse(LConf.get().setFlagCommandComplete, args.get(0).toUpperCase(), args.get(0).toUpperCase()));
	}
}
