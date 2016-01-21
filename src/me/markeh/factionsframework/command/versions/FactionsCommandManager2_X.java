package me.markeh.factionsframework.command.versions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.massivecraft.factions.Factions;
import com.massivecraft.massivecore.command.MassiveCommand;

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
		// newer type system in 2.7+ versions
		try {
			getCmdFactions().getClass().getMethod("addChild", MassiveCommand.class).invoke(getCmdFactions(), wrappers.get(cmd));
		} catch (Exception e1) {
			try {
				getCmdFactions().getClass().getMethod("addSubCommand", MassiveCommand.class).invoke(getCmdFactions(), wrappers.get(cmd));
			} catch (Exception e2) {
				// Failed to add, so we remove the wrapper
				wrappers.remove(cmd);
				FactionsFramework.get().logError(e1);
				FactionsFramework.get().logError(e2);
			}

		}
	}

	@Override
	public void removeCommand(FactionsCommand cmd) {
		try {
			getCmdFactions().getClass().getMethod("removeChild", MassiveCommand.class).invoke(getCmdFactions(), wrappers.get(cmd));
			wrappers.remove(cmd);
		} catch (Exception e1) {
			try {
				getCmdFactions().getClass().getMethod("removeSubCommand", MassiveCommand.class).invoke(getCmdFactions(), wrappers.get(cmd));
				wrappers.remove(cmd);
			} catch (Exception e2) {
				FactionsFramework.get().logError(e1);
				FactionsFramework.get().logError(e2);
			}

		}
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
