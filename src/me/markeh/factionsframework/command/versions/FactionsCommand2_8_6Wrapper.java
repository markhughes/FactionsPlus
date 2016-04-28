package me.markeh.factionsframework.command.versions;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import me.markeh.factionsframework.FactionsFramework;
import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsplus.FactionsPlus;

import com.massivecraft.massivecore.MassiveException;

/**
 * Command Wrapper
 * Supports: Factions > 2.6 & <= 2.8.6
 * 
 * Some version of Factions make major changes, in which a new
 * command wrapper should be created.
 * 
 * The latest wrapper should not initiate any reflection.
 */
public class FactionsCommand2_8_6Wrapper extends com.massivecraft.factions.cmd.FactionsCommand {

	// ---------------------------------------- //
	// FIELDS
	// ---------------------------------------- //

	private FactionsCommand command;
	
	// ---------------------------------------- //
	// CONSTRUCTOR
	// ---------------------------------------- //
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public FactionsCommand2_8_6Wrapper(FactionsCommand command, List<String> aliases, List<String> reqArguments, HashMap<String, String> optArguments) {
		// Older arg system ..
		
		// Store our FactionsCommand
		this.command = command;
		
		// Register all the aliases
		this.aliases.addAll(aliases);
		
		// --- Older AR Style ( <= 2.8.2 ) --- //
		
		// (eg: com.massivecraft.massivecore.cmd.arg.ARString)
		
		try {
			Class ARString = Class.forName("com.massivecraft.massivecore.cmd.arg.ARString");
			Method addArg = this.getClass().getMethod("addArg", ARString, String.class);
			
			// Register the required arguments 
			for(String reqArg : reqArguments) {
				addArg.invoke(this, ARString.getMethod("get").invoke(this), reqArg);
			}
			
			Method addArgOpt = this.getClass().getMethod("addArg", ARString, String.class, String.class);
			
			// Register all the optional arguments
			for(String optArg : optArguments.keySet()) {
				addArgOpt.invoke(this, ARString.getMethod("get").invoke(this), optArg, optArguments.get(optArg));

			}
			
			this.getClass().getMethod("setGivingErrorOnTooManyArgs", Boolean.class).invoke(this, this.command.doErrorOnTooManyArgs());
					
			
			this.command.helpLine = (String) this.getClass().getMethod("getUseageTemplate", Boolean.class).invoke(this).getClass().getMethod("toRaw").invoke(this);

		} catch(Exception e) {
			
			// --- Newer TYPE Style ( > 2.8.2 ) --- //
			
			// Register the required arguments 
			try { 
				
				for(String reqArg : reqArguments) {
					this.addParameter(com.massivecraft.massivecore.command.type.primitive.TypeString.get(), reqArg);
				}
				
				// Register the optional arguments
				for(String optArg : optArguments.keySet()) {
					this.addParameter(com.massivecraft.massivecore.command.type.primitive.TypeString.get(), optArg, optArguments.get(optArg));
				}
			} catch (Exception e2) {
				FactionsFramework.get().logError(e);
			}
			
			this.overflowSensitive = this.command.doErrorOnTooManyArgs();
			this.command.helpLine = this.getTemplate(true).toRaw();

		}
				
		this.setDesc(this.command.description);
	}

	// ---------------------------------------- //
	// METHODS
	// ---------------------------------------- //
	
	@Override
	public void perform() throws MassiveException {
		try { 
			this.command.executeAs(this.sender, this.args);
		} catch (Throwable e) {
			FactionsPlus.get().logError(e);
		}
	}
}
