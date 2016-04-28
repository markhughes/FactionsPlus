package me.markeh.factionsframework.command.versions;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsplus.FactionsPlus;

import com.massivecraft.factions.Factions;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

/**
 * Command Wrapper
 * Supports: Factions >= 2.8.7
 * 
 * Some version of Factions make major changes, in which a new
 * command wrapper should be created.
 * 
 * The latest wrapper should not initiate any reflection.
 */
public class FactionsCommand2_XWrapper extends com.massivecraft.factions.cmd.FactionsCommand {

	// ----------------------------------------
	//  FIELDS
	// ----------------------------------------

	private FactionsCommand command;
	
	// ----------------------------------------
	//  CONSTRUCTOR
	// ----------------------------------------
	
	public FactionsCommand2_XWrapper(FactionsCommand command, List<String> aliases, List<String> requiredArguments, HashMap<String, String> optionalArguments) {
		this.setCommand(command);
		
		// Add required arguments
		for (String reqArg : requiredArguments) {
			this.addParameter(TypeString.get(), reqArg);
		}
		
		// Add optional arguments
		for (Entry<String, String> optArg : optionalArguments.entrySet()) {
			this.addParameter(TypeString.get(), optArg.getKey(), optArg.getValue());
		}
		
		this.aliases.addAll(aliases);
		
		this.setDesc(command.getDescription());
		this.setHelp(command.getHelpLine());
		
		this.setOverflowSensitive(command.doErrorOnTooManyArgs());
		
		this.setActivePlugin(Factions.get());
		this.setActive(true);
	}

	// ----------------------------------------
	//  OVERRIDE
	// ----------------------------------------
	
	@Override
	public void perform() throws MassiveException {
		try {
			this.command.executeAs(this.sender, this.args);
		} catch (Throwable e) {
			FactionsPlus.get().logError(e);
		}
	}
	
	// ----------------------------------------
	//  UTIL METHODS
	// ----------------------------------------
	
	public final void setCommand(FactionsCommand cmd) {
		this.command = cmd;
	}
	
	public final FactionsCommand getCommand() {
		return this.command;
	}
}
