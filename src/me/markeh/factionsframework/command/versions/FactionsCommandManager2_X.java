package me.markeh.factionsframework.command.versions;

import java.util.HashMap;

import com.massivecraft.factions.cmd.CmdFactions;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.command.FactionsCommandManager;
import me.markeh.factionsframework.objs.NotifyEvent;

public class FactionsCommandManager2_X extends FactionsCommandManager {

	// ------------------------------
	//  Fields
	// ------------------------------
	
	private HashMap<FactionsCommand, FactionsCommand2_XWrapper> wrappers = new HashMap<FactionsCommand, FactionsCommand2_XWrapper>();
	
	// ------------------------------
	//  Methods
	// ------------------------------

	@Override
	public void addCommand(FactionsCommand cmd) {
		wrappers.put(cmd, new FactionsCommand2_XWrapper(cmd, cmd.aliases, cmd.requiredArguments, cmd.optionalArguments));
		
		CmdFactions.get().addChild(wrappers.get(cmd));
		
	}

	@Override
	public void removeCommand(FactionsCommand cmd) {
		CmdFactions.get().removeChild(wrappers.get(cmd));
		
		wrappers.remove(cmd);
	}

	@Override
	public void notify(NotifyEvent event) { }
}