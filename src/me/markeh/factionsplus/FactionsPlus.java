/*
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

package me.markeh.factionsplus;

import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import me.markeh.factionsframework.FactionsFramework;
import me.markeh.factionsframework.factionsmanager.FactionsManager;
import me.markeh.factionsframework.factionsmanager.FactionsVersion;
import me.markeh.factionsframework.objs.NotifyEvent;
import me.markeh.factionsplus.commands.CommandManager;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.conf.FactionData;
import me.markeh.factionsplus.conf.Texts;
import me.markeh.factionsplus.integration.IntegrationManager;
import me.markeh.factionsplus.integration.cannons.IntegrationCannons;
import me.markeh.factionsplus.integration.chestshop.IntegrationChestShop;
import me.markeh.factionsplus.integration.deadbolt.IntegrationDeadbolt;
import me.markeh.factionsplus.integration.disguisecraft.IntegrationDisguiseCraft;
import me.markeh.factionsplus.integration.idisguise.IntegrationIDisguise;
import me.markeh.factionsplus.integration.libsdisguises.IntegrationLibsDisguises;
import me.markeh.factionsplus.integration.lockette.IntegrationLockette;
import me.markeh.factionsplus.integration.showcasestandalone.IntegrationShowCaseStandalone;
import me.markeh.factionsplus.listeners.*;
import me.markeh.factionsplus.tools.*;

public class FactionsPlus extends FactionsPlusPlugin<FactionsPlus> {
	
	// ----------------------------------------
	//  Singleton
	// ----------------------------------------
	
	private static FactionsPlus instance;
	public static FactionsPlus get() { return instance; }
	public FactionsPlus() { instance = this; }
	
	// ----------------------------------------
	//  Fields
	// ----------------------------------------
		
	// Listeners
	private CoreListener coreListener;
	private JailListener jailListener;
	
	// ----------------------------------------
	//  Methods
	// ----------------------------------------

	// Plugin Enable
	@Override
	public final void enable() {
		// Get, load, and save our configuration
		Config.get().load().save();
		
		if ( ! getServer().getPluginManager().isPluginEnabled("Factions")) {
			log(" " + ChatColor.DARK_RED + ChatColor.BOLD +  "******************** Factions is not enabled ******************** ");
			log("FactionsPlus still requires a Factions plugin to be present!");
			log("You can download it from either dev.bukkit.org or Spigot resources:");
			log(" - " + ChatColor.DARK_BLUE + ChatColor.UNDERLINE +  "https://www.spigotmc.org/resources/factions.1900/");
			log(" - " + ChatColor.DARK_BLUE + ChatColor.UNDERLINE +  "http://dev.bukkit.org/bukkit-plugins/factions/");
			log(" " + ChatColor.DARK_RED + ChatColor.BOLD +  "***************************************************************** ");
			return;
		}
		
		// Ensure the FactionsFramework is setup 
		FactionsFramework.get(this).ensureSetup();
		
		// Using our command manager we add our commands 
		CommandManager.get().add();
		
		this.manageListeners();
		
		// Remove FactionsUUID /warp command 
		if (FactionsManager.get().determineVersion() == FactionsVersion.FactionsUUID) {
			FactionsUUIDTools.get().removeFactionsUUIDWarpCommands();
		}
		
		// Add our integrations 
		IntegrationManager.get().addIntegrations(
			IntegrationChestShop.get(),
			IntegrationDisguiseCraft.get(),
			IntegrationIDisguise.get(),
			IntegrationLibsDisguises.get(),
			IntegrationCannons.get(),
			IntegrationLockette.get(),
			IntegrationDeadbolt.get(),
			IntegrationShowCaseStandalone.get()
		);
				
		// Notify console we're ready
		log(Texts.ready);
		
		// Enable metrics 
		if (Config.get().metrics) {
			Metrics.get(this).attemptEnable();
		}
		
		// Notify events
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				// Notify our services of things
				IntegrationManager.get().notify(NotifyEvent.Loaded);
			}
		});
	}
	
	// Plugin Disable
	@Override
	public final void disable() {
		// Remove any commands we've added
		CommandManager.get().remove();
		
		// Save all the FactionData collections  
		for(FactionData fData : FactionData.getAll()) {
			fData.save();
		}
		
		IntegrationManager.get().disableIntegrations();
		
		// Notify events
		IntegrationManager.get().notify(NotifyEvent.Stopping);
		
		// Disable metrics 
		try {
			Metrics.get().disable();
		} catch (Throwable e) {
			this.logError(e);
		}
	}
	
	// Manage our listeners 
	private void manageListeners() {
		if (this.shouldCreateListener(coreListener, true)) {
			coreListener = new CoreListener();
			addListener(coreListener);
		}
		
		if (this.shouldCreateListener(jailListener, Config.get().enableJails)) {
			jailListener = new JailListener();
			addListener(jailListener);
		}
	}
	
	// Returns true if we have to create the listener and add it 
	private boolean shouldCreateListener(Listener listener, Boolean enabled) {
		if (listener == null && enabled) return true;  // listener is null and its enabled
		if (listener != null && enabled) return false;  // listener exists and is enabled
		
		// Shouldn't be enabled, so remove it (if its not already), and set it to null 
		if(listener != null && HandlerList.getRegisteredListeners(this).contains(listener)) {
			removeListener(listener);
			listener = null;
		}
		
		return false; 
	}
}
