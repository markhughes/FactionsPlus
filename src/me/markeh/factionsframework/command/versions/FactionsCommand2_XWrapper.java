package me.markeh.factionsframework.command.versions;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import me.markeh.factionsframework.FactionsFramework;
import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsplus.FactionsPlus;

import com.massivecraft.massivecore.MassiveException;

public class FactionsCommand2_XWrapper extends com.massivecraft.factions.cmd.FactionsCommand {

	// ------------------------------
	//  Fields
	// ------------------------------

	private FactionsCommand cmd;
	
	// ------------------------------
	//  Constructor
	// ------------------------------
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public FactionsCommand2_XWrapper(FactionsCommand cmd, List<String> _aliases, List<String> reqArguments, HashMap<String, String> optArguments) {
		// Older arg system ..
		
		// Store our FactionsCommand
		this.cmd = cmd;
		
		// Register all the aliases
		for(String alias : _aliases) {
			this.aliases.add(alias);
		}
		
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
			
			this.getClass().getMethod("setGivingErrorOnTooManyArgs", Boolean.class).invoke(this, cmd.errorOnTooManyArgs);
					
			
			cmd.helpLine = (String) this.getClass().getMethod("getUseageTemplate", Boolean.class).invoke(this).getClass().getMethod("toRaw").invoke(this);

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
			
			this.overflowSensitive = cmd.errorOnTooManyArgs;
			cmd.helpLine = this.getTemplate(true).toRaw();

		}
				
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
