package me.markeh.factionsframework.command.versions;

import java.util.HashMap;
import java.util.List;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsplus.FactionsPlus;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.cmd.arg.ARString;

public class FactionsCommand2XWrapper extends com.massivecraft.factions.cmd.FactionsCommand {

	// ------------------------------
	//  Fields
	// ------------------------------

	private FactionsCommand cmd;
	
	// ------------------------------
	//  Constructor
	// ------------------------------
	
	public FactionsCommand2XWrapper(FactionsCommand cmd, List<String> _aliases, List<String> reqArguments, HashMap<String, String> optArguments)
	{
		// Store our FactionsCommand
		this.cmd = cmd;
		
		// Register all the aliases
		for(String alias : _aliases) {
			this.aliases.add(alias);
		}
		
		// Register all the required arguments 
		for(String reqArg : reqArguments) {
			this.addArg(ARString.get(), reqArg);
		}
		
		// Register all the optional arguments
		for(String optArg : optArguments.keySet()) {
			this.addArg(ARString.get(), optArg, optArguments.get(optArg));
		}
		
		this.desc = cmd.description;
		
		cmd.helpLine = this.getUseageTemplate(false).toRaw();
		
		this.setGivingErrorOnTooManyArgs(cmd.errorOnTooManyArgs);
	}

	// ------------------------------
	//  Methods
	// ------------------------------
	
	@Override
	public void perform() throws MassiveException
	{
		try { 
			cmd.reset();
			
			// Set our information
			if(me != null && me.getPlayer() != null) {
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
}
