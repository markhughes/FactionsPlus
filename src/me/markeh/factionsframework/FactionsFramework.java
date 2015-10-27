/*
 * 
 *   FactionsFramework is apart of FactionsPlus.
 *   
 *   FactionsPlus extends on-top of Factions to provide more features.
 *   Copyright (C) 2015  Mark Hughes
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package me.markeh.factionsframework;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.markeh.factionsframework.command.FactionsCommandManager;
import me.markeh.factionsframework.events.listeners.FFListenerFactions16UUID;
import me.markeh.factionsframework.events.listeners.FFListenerFactions2;
import me.markeh.factionsframework.events.listeners.FFListenerGlobal;
import me.markeh.factionsframework.factionsmanager.FactionsManager;
import me.markeh.factionsframework.factionsmanager.FactionsVersion;
import me.markeh.factionsframework.objs.NotifyEvent;
import me.markeh.factionsplus.FactionsPlus;

public class FactionsFramework {
	
	// ------------------------------
	//  Singleton
	// ------------------------------
	
	private static FactionsFramework instance;
	public FactionsFramework(JavaPlugin plugin) {
		plugins.add(plugin);
		if(parent == null) parent = plugin;
	}
	
	public static FactionsFramework get(JavaPlugin plugin) { 
		if(instance == null) instance = new FactionsFramework(plugin);
		
		return instance;
	}
	public static FactionsFramework get() { 
		if(instance == null) instance = new FactionsFramework(null);
		
		return instance;
	}
	
	// ------------------------------
	//  Fields
	// ------------------------------
	
	private FactionsManager factionsManager = null;
	
	private Listener listener = null;
	private Boolean isSetup = false;
	
	private JavaPlugin parent = null;
	private List<JavaPlugin> plugins = new ArrayList<JavaPlugin>();
	
	// ------------------------------
	//  Methods
	// ------------------------------
	
	// Get the parent plugin
	public JavaPlugin getParent() {
		return this.parent;
	}
	
	// Ensure our listeners are enabled etc
	public final void ensureSetup() {
		try { 
			if (isSetup) return;
			
			factionsManager = FactionsManager.get();
			
			if (listener == null) {
				if (factionsManager.determineVersion() == FactionsVersion.FactionsUUID) {
					listener = new FFListenerFactions16UUID();
				} else if (factionsManager.determineVersion() == FactionsVersion.Factions2) {
					listener = new FFListenerFactions2();
				} else {
					Bukkit.getLogger().log(Level.SEVERE, "[FactionsFramework] FactionsFramework can not work out your Factions version.");
					Bukkit.getLogger().log(Level.SEVERE, "[FactionsFramework] FactionsFramework did not register our listener.");	
				}
			}
			
			if (listener != null) {
				if(parent == null) throw new Error("No parent! Pass a plugin to FactionsFramework.get() so we have a parent.");
				
				// We hook to the parent plugin (we need a plugin to rgister our listener against)
				// TODO: If parent goes away, we should hook into the next available plugin 
				Bukkit.getServer().getPluginManager().registerEvents(listener, parent);
				Bukkit.getLogger().log(Level.INFO, "[FactionsFramework] FactionsFramework has set its listener parent to " + parent.getName());
			}
			
			// Global Listener that is factions specific events 
			FFListenerGlobal globalListener = new FFListenerGlobal();
			Bukkit.getServer().getPluginManager().registerEvents(globalListener, parent);
	
			// Schedule tasks only start running when all plugins are loaded 
			parent.getServer().getScheduler().scheduleSyncDelayedTask(parent, new Runnable() {
				@Override
				public void run() {
					// Notify our services of things
					FactionsCommandManager.get().notify(NotifyEvent.Loaded);
					FactionsManager.get().notify(NotifyEvent.Loaded);
				}
			});
		} catch (Throwable e) {
			// TODO: Don't depend on FactionsPlus 
			FactionsPlus.get().logError(e);
		}
	}
	
	// Simple list of tested and supported versions, should be checked with .startsWith() 
	public final List<String> getSupported() {
		List<String> supported = new ArrayList<String>();
		
		// Factions 1.6 UUID
		supported.add("1.6.9.5-U0.1.18");
		
		// Original Factions
		supported.add("2.7.0");
		supported.add("2.7.1");
		supported.add("2.7.2");
		supported.add("2.7.3");
		supported.add("2.7.4");
		supported.add("2.7.5");
		supported.add("2.8.0");
		
		return supported;
	}
}
