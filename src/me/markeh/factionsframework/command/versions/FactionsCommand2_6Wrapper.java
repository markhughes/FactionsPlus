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
 * Supports: Factions <= 2.6
 * 
 * Some version of Factions make major changes, in which a new
 * command wrapper should be created.
 *
 * The latest wrapper should not initiate any reflection.
 */
public class FactionsCommand2_6Wrapper extends FCommand {
	
	// Stay compatible with Factions UUID, ensure you have Factions 2.6 Patches
	// 
	public TL getUsageTranslation() { return null; }
	
	// ---------------------------------------- //
	// FIELDS
	// ---------------------------------------- //

	private FactionsCommand command;
	
	// ---------------------------------------- //
	// CONSTRUCTOR 
	// ---------------------------------------- //
	
	public FactionsCommand2_6Wrapper(FactionsCommand command, List<String> aliases, List<String> reqArguments, HashMap<String, String> optArguments) {
		// Older arg system ..
		
		// Store our FactionsCommand
		this.command = command;
		
		// Register all the aliases
		this.aliases.addAll(aliases);		
		
		// Register all the required arguments 
		this.requiredArgs.addAll(reqArguments);
		
		// Register all the optional arguments
		for(String optArg : optArguments.keySet()) {
			try { 
				this.getClass().getMethod("addOptionalArg", String.class, String.class).invoke(this, optArg, optArguments.get(optArg));
			} catch (Exception e) {
				FactionsFramework.get().logError(e);
			}
		}
		
		try {
			this.getClass().getMethod("setDesc", String.class).invoke(this, this.command.getDescription());
		} catch (Exception e) {
			FactionsPlus.get().logError(e);
		}
		
		this.errorOnToManyArgs = command.doErrorOnTooManyArgs();
	}

	// ---------------------------------------- //
	//  METHODS
	// ---------------------------------------- //
	
	public final void perform() {
		try { 
			this.command.executeAs(this.sender, this.args);
		} catch (Throwable e) {
			FactionsPlus.get().logError(e);
		}
	}

}
