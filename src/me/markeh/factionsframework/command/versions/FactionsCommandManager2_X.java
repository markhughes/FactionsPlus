package me.markeh.factionsframework.command.versions;

import java.util.HashMap;

import com.massivecraft.factions.cmd.CmdFactions;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.command.FactionsCommandManager;

public class FactionsCommandManager2_X extends FactionsCommandManager {
	
	// ----------------------------------------
	//  FIELDS
	// ----------------------------------------
	
	private HashMap<FactionsCommand, FactionsCommand2_XWrapper> wrappers = new HashMap<FactionsCommand, FactionsCommand2_XWrapper>();
	
	// ----------------------------------------
	//  METHODS
	// ----------------------------------------
	
	@Override
	public void addCommand(FactionsCommand command) {
		FactionsCommand2_XWrapper wrapper = new FactionsCommand2_XWrapper(command, command.aliases, command.requiredArguments, command.optionalArguments);
		
		wrappers.put(command, wrapper);
		
		CmdFactions.get().addChild(wrappers.get(command));
		
	}

	@Override
	public void removeCommand(FactionsCommand command) {
		CmdFactions.get().removeChild(wrappers.get(command));
		
		wrappers.remove(command);
	}
}
