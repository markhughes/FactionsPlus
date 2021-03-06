package me.markeh.factionsplus.commands;

import java.util.ArrayList;
import java.util.List;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.command.FactionsCommandManager;

public class CommandManager {
	
	private static CommandManager instance = null;
	public static CommandManager get() {
		if (instance == null) instance = new CommandManager();
		
		return instance;
	}
	
	// ----------------------------------------
	// Fields
	// ----------------------------------------
	
	// Commands	
	private FactionsCommand cmdSetJail = new CmdSetJail();
	private FactionsCommand cmdJail = new CmdJail();
	private FactionsCommand cmdUnjail = new CmdUnjail();
		
	private FactionsCommand cmdWarps = new CmdWarps();
	private FactionsCommand cmdWarp = new CmdWarp();
	private FactionsCommand cmdSetWarp = new CmdSetWarp();
	private FactionsCommand cmdRemoveWarp = new CmdRemoveWarp();
	
	private FactionsCommand cmdRules = new CmdRules();
	private FactionsCommand cmdAddRule = new CmdAddRule();
	private FactionsCommand cmdDelRule = new CmdDelRule();
	
	private FactionsCommand cmdChest = new CmdChest();
	
	// ----------------------------------------
	// Methods
	// ----------------------------------------

	// Add in our commands
	public void add() {
		this.enableCmd(cmdSetJail);
		this.enableCmd(cmdJail);
		this.enableCmd(cmdUnjail);
		
		this.enableCmd(cmdWarp);
		this.enableCmd(cmdWarps);
		this.enableCmd(cmdSetWarp);
		this.enableCmd(cmdRemoveWarp);
		
		this.enableCmd(cmdAddRule);
		this.enableCmd(cmdDelRule);
		this.enableCmd(cmdRules);
		
		this.enableCmd(cmdChest);
	}
	
	// Remove our commands 
	public void remove() {
		for (FactionsCommand cmd : this.enabledCommands) {
			FactionsCommandManager.get().removeCommand(cmd);
		}
		
		this.enabledCommands.clear();
	}
	
	private List<FactionsCommand> enabledCommands = new ArrayList<FactionsCommand>();
	
	public void enableCmd(FactionsCommand cmd) {
		this.enabledCommands.add(cmd);
		FactionsCommandManager.get().addCommand(cmd);
	}
}
