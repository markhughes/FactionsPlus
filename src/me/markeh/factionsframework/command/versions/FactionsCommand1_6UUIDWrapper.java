package me.markeh.factionsframework.command.versions;

import java.util.HashMap;
import java.util.List;

import me.markeh.factionsframework.FactionsFramework;
import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsplus.FactionsPlus;

import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.zcore.util.TL;

/**
 * Command Wrapper
 * Supports: Latest Factions UUID
 * 
 * Some version of Factions make major changes, in which a new
 * command wrapper should be created.
 * 
 * The latest wrapper should not initiate any reflection.
 */
public class FactionsCommand1_6UUIDWrapper extends FCommand {
	
	// ---------------------------------------- //
	// FIELDS
	// ---------------------------------------- //
	
	private FactionsCommand command;
	
	// ---------------------------------------- //
	// CONSTRUCTOR
	// ---------------------------------------- //

	public FactionsCommand1_6UUIDWrapper(FactionsCommand command, List<String> aliases, List<String> reqArguments, HashMap<String, String> optArguments) {
		// Store our FactionsCommand
		this.command = command;
		
		// Register all the aliases
		this.aliases.addAll(aliases);
		
		// Register all the required arguments 
		this.requiredArgs.addAll(reqArguments);
		
		// Register all the optional arguments
		for (String optArg : optArguments.keySet()) {
			this.optionalArgs.put(optArg, optArguments.get(optArg));
		}
		
		// Reflection required here 
		try {
			this.getClass().getMethod("setHelpShort", String.class).invoke(this, this.command.getDescription());
		} catch (Exception e) {
			FactionsFramework.get().logError(e);
		}
		
		this.errorOnToManyArgs = this.command.doErrorOnTooManyArgs();
	}
	
	// ---------------------------------------- //
	// METHODS
	// ---------------------------------------- //

	@Override
	public void perform() {
		try { 
			this.command.executeAs(this.sender, this.args);
		} catch (Throwable e) {
			FactionsPlus.get().logError(e);
		}
	}
	
	// FactionsUUID translation system (can't do anything with this)
	public TL getUsageTranslation() {
		return null;
	}
	
}
