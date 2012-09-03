package markehme.factionsplus;

import markehme.factionsplus.Cmds.*;
import markehme.factionsplus.FactionsBridge.*;
import markehme.factionsplus.config.*;
import markehme.factionsplus.extras.*;

import com.massivecraft.factions.*;
import com.massivecraft.factions.cmd.*;



/*
	/f warp [name]	Warps to the Faction warp!	factionsplus.warp
	/f createwarp [name]	Creates a Faction warp, these can only be created in faction land	factionsplus.createwarp
	/f deletewarp [name]	Removes a Faction warp, these can only be created in faction land	factionsplus.deletewarp
	/f removewarp [name]	Alias of deletewarp	factionsplus.deletewarp
	/f setjail	Sets the jail location, this can only be set in faction land	factionsplus.setjail
	/f jail [name]	Sends a player to jail!	factionsplus.jail
	/f unjail [name]	Removes a player from jail!	factionsplus.jail

 */

public class FactionsPlusCommandManager {
	FactionsPlusCommandManager FactionsPlusCommandManager;
	
	public static void setup() {
		// Warp Commands 
		if(Config._warps.enabled._) {
			addSC(new CmdAddWarp()); 
			addSC(new CmdRemoveWarp());
			addSC(new CmdWarp());
			addSC(new CmdListWarps());
		}
		
		// Jail Commands
		if(Config._jails.enabled._) {
			addSC(new CmdSetJail());
			addSC(new CmdUnsetJail());
			addSC(new CmdJail());
			addSC(new CmdUnJail());
		}
		// General Commands
		if(Config._announce.enabled._) {
			addSC(new CmdAnnounce());
		}
		if(Config._banning.enabled._) {
			addSC(new CmdBan());
			addSC(new CmdUnban());
		}
		if(Config._peaceful.officersCanToggleState._ || Config._peaceful.membersCanToggleState._ || Config._peaceful.leadersCanToggleState._) {
			addSC(new CmdToggleState());
		}
		addSC(new CmdFC());
		addSC(new CmdGC());
		
		// Region based Commands
		//addSC(new CmdPlot());
		
		
		if (Config._economy.isHooked()){
			Bridge.factions.addSubCommand(P.p.cmdBase.cmdMoney, new CmdMoneyTop());
		}
		
		// New Admin commands 
		addSC(new CmdFactionHome());
		
		addSC(new CmdDebug());
		
		//LWC Officer/Faction Owner clear chunk of non-member claims command
		if(LWCBase.isLWC()) {
			addSC(new CmdClearLocks());
		}
		
		addSC(new CmdReloadFP());
		
//		Bridge.factions.addSubCommand(P.p.cmdBase.cmdPower, new CmdPowPow());
		addSC(new CmdPowSettings());
		
		//last:
		Bridge.factions.finalizeHelp(); 
	}

	private static final void addSC(FCommand subCommand) {
		Bridge.factions.addSubCommand(P.p.cmdBase, subCommand);
	}
	
}
