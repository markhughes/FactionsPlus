package me.markeh.factionsframework.command;

import me.markeh.factionsframework.command.versions.FactionsCommandManager16UUID;
import me.markeh.factionsframework.command.versions.FactionsCommandManager2X;
import me.markeh.factionsframework.factionsmanager.FactionsManager;
import me.markeh.factionsframework.factionsmanager.FactionsVersion;
import me.markeh.factionsframework.objs.NotifyEvent;

public abstract class FactionsCommandManager {
	
	// ------------------------------
	// Singleton
	// ------------------------------

	private static FactionsCommandManager instance;
	public static FactionsCommandManager get() {
		if(instance == null) {
			if(FactionsManager.get().determineVersion() == FactionsVersion.FactionsUUID) {
				instance = new FactionsCommandManager16UUID();
			} else {
				instance = new FactionsCommandManager2X();
			}
		}
		
		return instance;
	}
	
	// ------------------------------
	// Methods
	// ------------------------------
	
	// Add a command
	public abstract void addCommand(FactionsCommand cmd);
	
	// Remove a command 
	public abstract void removeCommand(FactionsCommand cmd);
	
	// Simple event notification
	public abstract void notify(NotifyEvent event);
}
