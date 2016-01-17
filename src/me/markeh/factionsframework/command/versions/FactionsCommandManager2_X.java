package me.markeh.factionsframework.command.versions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.massivecraft.factions.Factions;

import me.markeh.factionsframework.FactionsFramework;
import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.command.FactionsCommandManager;
import me.markeh.factionsframework.objs.NotifyEvent;

public class FactionsCommandManager2_X extends FactionsCommandManager {

	// ------------------------------
	//  Fields
	// ------------------------------
	
	private HashMap<FactionsCommand, FactionsCommand2_XWrapper> wrappers = new HashMap<FactionsCommand, FactionsCommand2_XWrapper>();
	
	// ------------------------------
	//  Methods
	// ------------------------------

	@Override
	public void addCommand(FactionsCommand cmd) {
		wrappers.put(cmd, new FactionsCommand2_XWrapper(cmd, cmd.aliases, cmd.requiredArguments, cmd.optionalArguments));
		// newer type system...
		//getCmdFactions().addChild(wrappers.get(cmd));	
		
		// older arg system..
		getCmdFactions().addSubCommand(wrappers.get(cmd));
	}

	@Override
	public void removeCommand(FactionsCommand cmd) {
		// newer type system...
		//getCmdFactions().removeChild(wrappers.get(cmd));
		
		// older arg system..
		getCmdFactions().removeSubCommand(wrappers.get(cmd));
	}

	@Override
	public void notify(NotifyEvent event) {
		// NOTE: Not needed
	}
	
	// We have to do this to simplify the compiling process
	private com.massivecraft.factions.cmd.CmdFactions getCmdFactions() {
		try { 
			Method getMethod = com.massivecraft.factions.Factions.class.getMethod("get");
			Factions factionsInstance = (Factions) getMethod.invoke(null);
						
	        Field field = com.massivecraft.factions.Factions.class.getDeclaredField("outerCmdFactions");
	        field.setAccessible(true);
	        	        
			return (com.massivecraft.factions.cmd.CmdFactions) field.get(factionsInstance);
			
		} catch(Exception e) {
			FactionsFramework.get().logError(e);
			return null;
		}
	}
}
