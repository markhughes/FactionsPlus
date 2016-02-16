package me.markeh.factionsframework.command.versions;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import me.markeh.factionsframework.FactionsFramework;
import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsplus.FactionsPlus;

import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.zcore.util.TL;

public class FactionsCommand2_6Wrapper extends FCommand {
	
	// Stay compatible with Factions UUID, ensure you have Factions 2.6 Patches
	// 
	public TL getUsageTranslation() { return null; }
	
	// ------------------------------
	//  Fields
	// ------------------------------

	private FactionsCommand cmd;
	
	// ------------------------------
	//  Constructor
	// ------------------------------
	
	public FactionsCommand2_6Wrapper(FactionsCommand cmd, List<String> _aliases, List<String> reqArguments, HashMap<String, String> optArguments) {
		// Older arg system ..
		
		// Store our FactionsCommand
		this.cmd = cmd;
		
		// Register all the aliases
		for(String alias : _aliases) {
			this.aliases.add(alias);
		}
		
		
		// Register all the required arguments 
		for(String reqArg : reqArguments) {
			this.requiredArgs.add(reqArg);
			//this.addArg(ARString.get(), reqArg);
		}
		
		// Register all the optional arguments
		for(String optArg : optArguments.keySet()) {
			try { 
				this.getClass().getMethod("addOptionalArg", String.class, String.class).invoke(this, optArg, optArguments.get(optArg));
			} catch (Exception e) {
				FactionsFramework.get().logError(e);
			}
		}
		
		try {
			this.getClass().getMethod("setDesc", String.class).invoke(this, cmd.description);
			// this.getClass().getMethod("setHelp", String[].class).invoke(this, cmd.description);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			FactionsPlus.get().logError(e);
		}
		
		this.errorOnToManyArgs = cmd.errorOnTooManyArgs;
	}

	// ------------------------------
	//  Methods
	// ------------------------------
	
	public final void perform() {
		try { 
			cmd.reset();
			
			// Set our information
			if(me != null && me.getPlayer() != null) {
				cmd.sender = this.sender;
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

}
