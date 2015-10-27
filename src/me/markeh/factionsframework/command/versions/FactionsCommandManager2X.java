package me.markeh.factionsframework.command.versions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;


import com.massivecraft.factions.Factions;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.command.FactionsCommandManager;
import me.markeh.factionsframework.objs.NotifyEvent;

public class FactionsCommandManager2X extends FactionsCommandManager {

	// ------------------------------
	//  Fields
	// ------------------------------
	
	private HashMap<FactionsCommand, FactionsCommand2XWrapper> wrappers = new HashMap<FactionsCommand, FactionsCommand2XWrapper>();
	
	// ------------------------------
	//  Methods
	// ------------------------------

	@Override
	public void addCommand(FactionsCommand cmd) {
		wrappers.put(cmd, new FactionsCommand2XWrapper(cmd, cmd.aliases, cmd.requiredArguments, cmd.optionalArguments));
		
		getCmdFactions().addSubCommand(wrappers.get(cmd));	
	}

	@Override
	public void removeCommand(FactionsCommand cmd) {
		getCmdFactions().removeSubCommand(wrappers.get(cmd));
	}

	@Override
	public void notify(NotifyEvent event) {
		// NOTE: Not needed
	}
	
	// We have to do this to simply the compiling process
	private com.massivecraft.factions.cmd.CmdFactions getCmdFactions() {
		try { 
			Method getMethod = com.massivecraft.factions.Factions.class.getMethod("get");
			Factions factionsInstance = (Factions) getMethod.invoke(null);
						
	        Field field = com.massivecraft.factions.Factions.class.getDeclaredField("outerCmdFactions");
	        field.setAccessible(true);
	        	        
			return (com.massivecraft.factions.cmd.CmdFactions) field.get(factionsInstance);
			
		} catch(Exception e) {
			e.printStackTrace();
			// TODO: Freak the fuck out 
			return null;
		}
	}
}
