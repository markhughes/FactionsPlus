package me.markeh.factionsframework.command.versions;

import java.util.HashMap;
import java.util.List;

import me.markeh.factionsframework.FactionsFramework;
import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsplus.FactionsPlus;

import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.zcore.util.TL;

public class FactionsCommand1_6UUIDWrapper extends FCommand {
	
	// ------------------------------
	//  Fields
	// ------------------------------
	
	private FactionsCommand cmd;
	
	// ------------------------------
	//  Constructor
	// ------------------------------

	public FactionsCommand1_6UUIDWrapper(FactionsCommand cmd, List<String> _aliases, List<String> reqArguments, HashMap<String, String> optArguments) {
		// Store our FactionsCommand
		this.cmd = cmd;
		
		// Register all the aliases
		for(String alias : _aliases) {
			this.aliases.add(alias);
		}
		
		// Register all the required arguments 
		for(String reqArg : reqArguments) {
			this.requiredArgs.add(reqArg);
		}
		
		// Register all the optional arguments
		for(String optArg : optArguments.keySet()) {
			this.optionalArgs.put(optArg, optArguments.get(optArg));
		}
		
		try {
			this.getClass().getMethod("setHelpShort", String.class).invoke(this, cmd.description);
		} catch (Exception e) {
			FactionsFramework.get().logError(e);
		}
		
		cmd.helpLine = this.getUseageTemplate();
		
		this.errorOnToManyArgs = cmd.errorOnTooManyArgs;
	}
	
	// ------------------------------
	//  Methods
	// ------------------------------

	@Override
	public void perform() {
		try { 
			cmd.reset();
					
			// Set our information
			if(me != null) {
				cmd.player = me.getPlayer();
			}
			
			cmd.arguments = this.args;
			
			// Call our pre-run
			cmd.preRun();		
			
			// Run the command (if we're allowed) 
			if(cmd.canRun) cmd.run();
		} catch (Throwable e) {
			FactionsPlus.get().logError(e);
		}
	}
	
	// FactionsUUID has a really dumb translation system, we can't do anything with this 
	public TL getUsageTranslation() { return null; }
}
