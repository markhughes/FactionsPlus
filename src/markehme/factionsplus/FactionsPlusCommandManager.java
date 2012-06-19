package markehme.factionsplus;


import markehme.factionsplus.Cmds.*;

import com.massivecraft.factions.P;

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
		P.p.cmdBase.addSubCommand(new CmdAddWarp()); 
		P.p.cmdBase.addSubCommand(new CmdRemoveWarp());
		P.p.cmdBase.addSubCommand(new CmdWarp());
		P.p.cmdBase.addSubCommand(new CmdListWarps());
		
		// Jail Commands
		P.p.cmdBase.addSubCommand(new CmdSetJail());
		P.p.cmdBase.addSubCommand(new CmdUnsetJail());
		P.p.cmdBase.addSubCommand(new CmdJail());
		P.p.cmdBase.addSubCommand(new CmdUnJail());
		
		// General Commands
		P.p.cmdBase.addSubCommand(new CmdAnnounce());
		P.p.cmdBase.addSubCommand(new CmdBan());
		P.p.cmdBase.addSubCommand(new CmdToggleState());
		P.p.cmdBase.addSubCommand(new CmdFC());
		P.p.cmdBase.addSubCommand(new CmdGC());
		
		// Region based Commands
		//P.p.cmdBase.addSubCommand(new CmdPlot());
		
		// New Admin commands 
		P.p.cmdBase.addSubCommand(new CmdFactionHome());
		
		P.p.cmdBase.cmdMoney.addSubCommand(new CmdMoneyTop());
		
		P.p.cmdBase.addSubCommand(new CmdDebug());
		
	}

	public void perform() {
		
	}


	
}
