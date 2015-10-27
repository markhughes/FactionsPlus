package me.markeh.factionsframework.factionsmanager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import me.markeh.factionsframework.faction.Factions;
import me.markeh.factionsframework.faction.versions.Factions16UUID;
import me.markeh.factionsframework.faction.versions.Factions2X;
import me.markeh.factionsframework.objs.NotifyEvent;

public class FactionsManager {
	
	// -----------------------
	// Singleton 
	// -----------------------
	
	private static FactionsManager instance = null;
	public static FactionsManager get() {
		if(instance == null) {
			instance = new FactionsManager();
		}
			
		return instance;
	}
	
	public FactionsManager() { 
		if(instance == null) {
			instance = this;
		} else if(instance != this) {
			try {
				finalize();
			} catch (Throwable e) {
				e.printStackTrace();
			}
			
			return;
		}
		
		version = determineVersion();
	}
	
	// -----------------------
	// Fields
	// -----------------------
	
	private FactionsVersion version = null;
	private Factions factions = null;
	
	// -----------------------
	// Methods
	// -----------------------
	
	// Determine the version of Factions
	public FactionsVersion determineVersion() {
		
		if(this.version != null) return version;
		
		Plugin factionsPlugin = Bukkit.getPluginManager().getPlugin("Factions");
		if(factionsPlugin == null) {
			return null;
		}
		
		String factionsVersion = factionsPlugin.getDescription().getVersion();
		
		// Factions UUID always starts with '1.6.9.5-U'
		if(factionsVersion.startsWith("1.6.9.5-U")) {
			return FactionsVersion.FactionsUUID;
		} else if(factionsVersion.startsWith("2.")) {
			return FactionsVersion.Factions2;
		}
		
		throw new Error("Please use FactionsUUID (1.6.9.5-U) or Factions 2.8+");
		
	}
	
	// Fetch the correct Factions object
	public Factions fetch() {
		if(factions == null) {
			if(this.version.equals(FactionsVersion.Factions2)) {
				factions = new Factions2X();
			} else {
				factions = new Factions16UUID();
			}
		}
		
		return factions;
	}

	public void notify(NotifyEvent loaded) {
		// Not required
	}
}
