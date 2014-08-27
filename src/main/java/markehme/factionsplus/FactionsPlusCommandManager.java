package markehme.factionsplus;

import java.util.ArrayList;
import java.util.List;

import markehme.factionsplus.Cmds.*;
import markehme.factionsplus.Cmds.FPCommand;
import markehme.factionsplus.extras.LWCBase;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.massivecore.cmd.MassiveCommand;

public class FactionsPlusCommandManager {
	FactionsPlusCommandManager FactionsPlusCommandManager;
	public static ArrayList<String> addedCommands = new ArrayList<String>();
	
	public static void setup() {
		
		// Warp Commands 
		addSC(new CmdAddWarp()); 
		addSC(new CmdRemoveWarp());
		addSC(new CmdWarp());
		addSC(new CmdListWarps());
		
		// Jail Commands
		addSC(new CmdSetJail());
		addSC(new CmdUnsetJail());
		addSC(new CmdJail());
		addSC(new CmdUnJail());

		// General Commands
		addSC(new CmdAnnounce());

		addSC(new CmdToggleState());
		
		// Faction Bans
		addSC(new CmdBan());
		addSC(new CmdUnban());

		// Faction Rules
		addSC(new CmdSetRule());
		addSC(new CmdRemoveRule());
		addSC(new CmdRules());
		
		// Region based Commands
		//addSC(new CmdPlot());

		Factions.get().getOuterCmdFactions().cmdFactionsMoney.addSubCommand(new CmdMoneyTop());
		
		// New Admin commands 
		addSC(new CmdFactionHome());
		
		// General Additional Commands
		addSC(new CmdFactionNeed());
		addSC(new CmdChest());
		
		addSC(new CmdDebug());
		
		//LWC Officer/Faction Owner clear chunk of non-member claims command
		if(LWCBase.isLWCPluginPresent()) {
			addSC(new CmdClearLocks());
		}
				
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
		List<MassiveCommand> commands = Factions.get().getOuterCmdFactions().getSubCommands();
				
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

	public static void integrateCmdFactionsFaction() throws Exception {
		// Fetch all the sub commands - do NOT copy
		List<MassiveCommand> commands = Factions.get().getOuterCmdFactions().getSubCommands();
		
		boolean found = false;
		
		// We're hunting for the "faction" command
		for (int i=0; i < commands.size(); i++) {
			
			if(commands.get(i).getAliases().contains("faction") || commands.get(i).getAliases().contains("f")) {
				FactionsPlus.debug("Found the /f faction or /f f command - removing. ");
				Factions.get().getOuterCmdFactions().getSubCommands().remove(i); // Remove it! 
				
				found= true;
			}
		}
		if(found) {
			FactionsPlus.debug("Adding our /f faction and /f f command");
			addSC(new CmdFactionsFaction());
		} else {
			FactionsPlus.info("We could not locate the /f faction or /f f command:");
			FactionsPlus.info("factionCommand* features disabled.");
		}
	}

}
