package markehme.factionsplus;

import java.util.ArrayList;
import java.util.List;

import markehme.factionsplus.Cmds.CmdAddWarp;
import markehme.factionsplus.Cmds.CmdAnnounce;
import markehme.factionsplus.Cmds.CmdBan;
import markehme.factionsplus.Cmds.CmdClearLocks;
import markehme.factionsplus.Cmds.CmdDebug;
import markehme.factionsplus.Cmds.CmdFactionHome;
import markehme.factionsplus.Cmds.CmdFactionNeed;
import markehme.factionsplus.Cmds.CmdJail;
import markehme.factionsplus.Cmds.CmdListWarps;
import markehme.factionsplus.Cmds.CmdMoneyTop;
import markehme.factionsplus.Cmds.CmdPowSettings;
import markehme.factionsplus.Cmds.CmdReloadFP;
import markehme.factionsplus.Cmds.CmdRemoveRule;
import markehme.factionsplus.Cmds.CmdRemoveWarp;
import markehme.factionsplus.Cmds.CmdRules;
import markehme.factionsplus.Cmds.CmdSetJail;
import markehme.factionsplus.Cmds.CmdSetRule;
import markehme.factionsplus.Cmds.CmdToggleState;
import markehme.factionsplus.Cmds.CmdUnJail;
import markehme.factionsplus.Cmds.CmdUnban;
import markehme.factionsplus.Cmds.CmdUnsetJail;
import markehme.factionsplus.Cmds.CmdWarp;
import markehme.factionsplus.Cmds.FPCommand;
import markehme.factionsplus.config.Config;
import markehme.factionsplus.extras.LWCBase;
import markehme.factionsplus.references.FP;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.mcore.cmd.MCommand;



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
	public static ArrayList<String> addedCommands = new ArrayList<String>();
	
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
		
		if(Config._peaceful.officersCanToggleState._ || Config._peaceful.membersCanToggleState._ || Config._peaceful.leadersCanToggleState._) {
			addSC(new CmdToggleState());
		}
		
		// Faction Bans
		if(Config._banning.enabled._) {
			addSC(new CmdBan());
			addSC(new CmdUnban());
		}

		// Faction Rules
		if(Config._rules.enabled._) {
			addSC(new CmdSetRule());
			addSC(new CmdRemoveRule());
			addSC(new CmdRules());
		}
		
		// Region based Commands
		//addSC(new CmdPlot());

		if (Config._economy.isHooked()){
			Factions.get().getOuterCmdFactions().cmdFactionsMoney.addSubCommand(new CmdMoneyTop());
		}
		
		// New Admin commands 
		addSC(new CmdFactionHome());
		
		// General Additional Commands
		addSC(new CmdFactionNeed());
		
		addSC(new CmdDebug());
		
		//LWC Officer/Faction Owner clear chunk of non-member claims command
		if(LWCBase.isLWCPluginPresent()) {
			addSC(new CmdClearLocks());
		}
		
		addSC(new CmdReloadFP());
		
//		Bridge.factions.addSubCommand(P.p.cmdBase.cmdPower, new CmdPowPow());
		addSC(new CmdPowSettings());
		
	}
	
	private static final void addSC(FPCommand subCommand) {
		Factions.get().getOuterCmdFactions().addSubCommand((FCommand) subCommand);
		String Cc;
		
		for (int i=0; i < subCommand.getAliases().size(); i++)  {
			Cc = subCommand.getIdentifier().toString();
			addedCommands.add(Cc);
		}
		
	}
	
	public static void disableSubCommands() throws Exception {
		// Fetch all the sub commands
		List<MCommand> commands = Factions.get().getOuterCmdFactions().getSubCommands();
				
		// Go through each command 
		for (int i=0; i < commands.size(); i++) {
			// Loop through all our known added alias
			for (String a: addedCommands) {
				// Compare each alias to the current command
				try {
					if(commands.get(i).getAliases().contains(a)) {
						// They match, so remove it. 
						commands.remove(i);
					}
				} catch(Exception e) {
					// .. normal 
				}
			}
			
		}
	}
	
}
