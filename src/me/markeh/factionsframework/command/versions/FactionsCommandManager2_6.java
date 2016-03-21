package me.markeh.factionsframework.command.versions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import me.markeh.factionsframework.FactionsFramework;
import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.command.FactionsCommandManager;
import me.markeh.factionsframework.objs.NotifyEvent;
import me.markeh.factionsplus.FactionsPlus;

import com.massivecraft.factions.Factions;

public class FactionsCommandManager2_6 extends FactionsCommandManager {

	// ------------------------------
	//  Fields
	// ------------------------------
	
	private HashMap<FactionsCommand, FactionsCommand2_6Wrapper> wrappers = new HashMap<FactionsCommand, FactionsCommand2_6Wrapper>();
	
	// ------------------------------
	//  Methods
	// ------------------------------

	@Override
	public void addCommand(FactionsCommand cmd) {
		wrappers.put(cmd, new FactionsCommand2_6Wrapper(cmd, cmd.aliases, cmd.requiredArguments, cmd.optionalArguments));
		
		// <= 2.6 needs to be able to use FCommand 
		try {
			getCmdFactions().getClass().getMethod("addSubCommand", Class.forName("com.massivecraft.massivecore.cmd.MassiveCommand")).invoke(getCmdFactions(), wrappers.get(cmd));
		} catch (Exception e) {
			FactionsPlus.get().logError(e);
		}
	}

	@Override
	public void removeCommand(FactionsCommand cmd) {
		// TODO: use reflection to unprotected the subCommands variable and then remove it ourselves 
		/*
		try {
			getCmdFactions().getClass().getMethod("removeSubCommand", MassiveCommand.class).invoke(getCmdFactions(), wrappers.get(cmd));
		} catch (Exception e) {
			FactionsPlus.get().logError(e);
		}
		*/
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
