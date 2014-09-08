package markehme.factionsplus.Cmds;


import markehme.factionsplus.Utilities;
import markehme.factionsplus.Cmds.req.ReqFactionsPlusEnabled;
import markehme.factionsplus.Cmds.req.ReqWarpsEnabled;
import markehme.factionsplus.MCore.LConf;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.extras.FPPerm;
import markehme.factionsplus.extras.FType;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;

public class CmdAddWarp extends FPCommand {
	public CmdAddWarp() {
		this.aliases.add("createwarp");
		this.aliases.add("addwarp");
		this.aliases.add("setwarp");
		
		// Unique identifier for this command 
		this.fpidentifier = "addwarp";
		
		this.requiredArgs.add("name" );
		this.optionalArgs.put("password", "");
		
		this.addRequirements(ReqFactionsEnabled.get());
		
		// Ensure FactionsPlus is enabled
		this.addRequirements(ReqFactionsPlusEnabled.get());
		
		// Ensure Warps are enabled
		this.addRequirements(ReqWarpsEnabled.get());
		
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		
		this.addRequirements(ReqHasPerm.get(FPPerm.CREATEWARP.node));
		
		this.setHelp(LConf.get().cmdDescAddWarp);
		this.setDesc(LConf.get().cmdDescAddWarp);

	}

	@Override
	public void performfp() {
				
		String warpName 	= this.arg(0);
		String warpPass		= null;
				
		// Check if setting a password
		if(this.arg(1) != null) {
			warpPass = this.arg(1);
			
			// Ensure the password is long enough
			if(warpPass.length() < 1) {
				msg(LConf.get().warpsPasswordTooSmall);
				return;
			}
		}
				
		// Ensure that this role can set warps 
		if(!FPUConf.get(usender).whoCanSetWarps.get(usender.getRole())) {
			sender.sendMessage(Txt.parse(LConf.get().warpsNotHighEnoughRankingToModify));
			return;
		}
		
		// Ensure they can create warps at their location  
		Faction landIN = BoardColls.get().getForUniverse(usender.getUniverse()).getFactionAt(PS.valueOf(me.getLocation()));
		Boolean canCreateHere = true;
		
		if(!usender.isInOwnTerritory()) {
			if(landIN.getRelationTo(usender) == Rel.ENEMY && !FPUConf.get(usender).allowWarpsIn.get("enemy") ) {
				canCreateHere = false;
			}
			
			if(landIN.getRelationTo(usender) == Rel.ALLY && !FPUConf.get(usender).allowWarpsIn.get("ally") ) {
				canCreateHere = false;
			}
			
			if(landIN.getRelationTo(usender) == Rel.NEUTRAL && !FPUConf.get(usender).allowWarpsIn.get("neutral") ) {
				canCreateHere = false;
			}
			
			if(landIN.getRelationTo(usender) == Rel.TRUCE && !FPUConf.get(usender).allowWarpsIn.get("true") ) {
				canCreateHere = false;
			}
			
			if((
				landIN.getRelationTo(usender) == Rel.MEMBER ||
				landIN.getRelationTo(usender) == Rel.LEADER ||
				landIN.getRelationTo(usender) == Rel.OFFICER ||
				landIN.getRelationTo(usender) == Rel.RECRUIT
			) && !FPUConf.get(usender).allowWarpsIn.get("owned")) {
				canCreateHere = false;
			}
		}
		
		if(FType.valueOf(landIN) == FType.WILDERNESS && !FPUConf.get(usender).allowWarpsIn.get("wilderness")) {
			canCreateHere = false;
		}
		
		if(FType.valueOf(landIN) == FType.WARZONE && !FPUConf.get(usender).allowWarpsIn.get("warzone")) {
			canCreateHere = false;
		}
		
		if(FType.valueOf(landIN) == FType.SAFEZONE && !FPUConf.get(usender).allowWarpsIn.get("safezone")) {
			canCreateHere = false;
		}
		
		// Notify + cancel if they can't 
		if(!canCreateHere) {
			msg(Txt.parse(LConf.get().warpsNotInCorrectTerritory));
			return;
		}
		
		// Ensure the warp doesn't exists
		if(fData.warpExists(warpName)) {
			msg(Txt.parse(LConf.get().warpsAlreadyExists));
			return;
		}
		
		// If there is a warp limit, don't let them go over it
		if(FPUConf.get(usender).maxWarps != 0) {
			if(Utilities.getCountOfWarps(usenderFaction) >= FPUConf.get(usender).maxWarps) {
				msg(Txt.parse(LConf.get().warpsReachedMax));
				return;
			}
		}
		
		// charge if needed
		if(FPUConf.get(usender).economyCost.get("createwarp") > 0.0d) {
			if(!Utilities.doCharge(FPUConf.get(usender).economyCost.get("createwarp"), usender)) {
				msg(Txt.parse(LConf.get().warpsCanNotAfford), FPUConf.get(usender).economyCost.get("createwarp"));
				return;
			}
		}
		
		// Store the warp data 
		fData.warpLocation.put(warpName.toLowerCase(), PS.valueOf(me.getLocation()));
		fData.warpPasswords.put(warpName.toLowerCase(), warpPass);
		
		// As of 0.7.x we supply alternative messages if there is a password
		if(warpPass == null) {
			msg(Txt.parse(LConf.get().warpsCreateSuccess), warpName);
		} else {
			msg(Txt.parse(LConf.get().warpsCreateSuccessWithPassword), warpName, warpPass);
		}
		
		// Notify the Faction 
		usenderFaction.msg(Txt.parse(LConf.get().warpsCreateNotify, usender.getName(), warpName));
		
	}
}