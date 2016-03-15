package me.markeh.factionsframework.command;

import me.markeh.factionsframework.command.versions.FactionsCommandManager1_6UUID;
import me.markeh.factionsframework.command.versions.FactionsCommandManager2_6;
import me.markeh.factionsframework.command.versions.FactionsCommandManager2_8_6;
import me.markeh.factionsframework.command.versions.FactionsCommandManager2_X;
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
				instance = new FactionsCommandManager1_6UUID();
			} else {
				if (FactionsManager.get().determineVersion() == FactionsVersion.Factions2_8_6) {
					// Let's test for 2.6 
					try {
						Class.forName("com.massivecraft.factions.cmd.FCommand");
					} catch (ClassNotFoundException e) {
						// 2.6 class did not exist, so use newest 2_8
						instance = new FactionsCommandManager2_8_6();
					}
					
					// Nope, switch back to 2.6
					if (instance == null) instance = new FactionsCommandManager2_6();
				} else {
					instance = new FactionsCommandManager2_X();
				}
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
