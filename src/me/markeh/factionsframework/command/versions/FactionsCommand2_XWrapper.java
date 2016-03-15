package me.markeh.factionsframework.command.versions;

import java.util.HashMap;
import java.util.List;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsplus.FactionsPlus;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class FactionsCommand2_XWrapper extends com.massivecraft.factions.cmd.FactionsCommand {

	// ------------------------------
	//  Fields
	// ------------------------------

	private FactionsCommand cmd;
	
	// ------------------------------
	//  Constructor
	// ------------------------------
	
	public FactionsCommand2_XWrapper(FactionsCommand cmd, List<String> _aliases, List<String> reqArguments, HashMap<String, String> optArguments) {
		// Add required arguments
		for (String reqArg : reqArguments) this.addParameter(TypeString.get(), reqArg);
		
		// Add optional arguments
		for (String optArg : optArguments.keySet()) this.addParameter(TypeString.get(), optArg, optArguments.get(optArg));
			
		this.overflowSensitive = cmd.errorOnTooManyArgs;
		
		this.setHelp(cmd.helpLine);
		this.setDesc(cmd.description);
	}

	// ------------------------------
	//  Methods
	// ------------------------------
	
	@Override
	public void perform() throws MassiveException {
		try { 
			cmd.reset();
			
			// Set our information
			cmd.sender = this.sender;
			
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