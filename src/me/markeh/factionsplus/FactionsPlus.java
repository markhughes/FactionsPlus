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
import me.markeh.factionsplus.integration.hawkeye.IntegrationHawkEye;
import me.markeh.factionsplus.integration.idisguise.IntegrationIDisguise;
import me.markeh.factionsplus.integration.libsdisguises.IntegrationLibsDisguises;
import me.markeh.factionsplus.integration.lockette.IntegrationLockette;
import me.markeh.factionsplus.integration.prism.IntegrationPrism;
import me.markeh.factionsplus.integration.showcasestandalone.IntegrationShowCaseStandalone;
import me.markeh.factionsplus.listeners.*;
import me.markeh.factionsplus.scoreboard.FactionsPlusScoreboard;
import me.markeh.factionsplus.util.FactionsUUIDTools;
import me.markeh.factionsplus.util.Metrics;
import me.markeh.factionsplus.util.PatreonBadge;
import me.markeh.factionsplus.wildernesschunks.WildernessChunks;

public class FactionsPlus extends FactionsPlusPlugin<FactionsPlus> {
	
	// ----------------------------------------
	//  Singleton
	// ----------------------------------------
	
	private static FactionsPlus instance;
	public static FactionsPlus get() { return instance; }
	public FactionsPlus() { instance = this; }
	
	// ----------------------------------------
	//  Methods
	// ----------------------------------------

	public final void enable() {
		// Get, load, save our configuration and texts. Then start watching.
		Config.get().load().save().watchStart();
		
		Texts.get().setHeader(
				"It is suggested that you do not make major changes to the texts file.",
				"Proceed with caution!"
			).load().save().watchStart();
		
		// Add our Factions dependency and MassiveCore dependency (if required) 
		this.addDependency("Factions");
		this.addDependency(FactionsManager.get().determineVersion().requiresMassiveCore(), "MassiveCore", "MCore");
		
		// Ensure FactionsFramework is setup
		FactionsFramework.get(this).ensureSetup();
		
		CommandManager.get().add();
		
		this.manageListeners();
		
		// Remove FactionsUUID /warp command (it's shit) 
		if (FactionsManager.get().determineVersion().isFactions1_6UUID()) {
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
			IntegrationShowCaseStandalone.get(),
			IntegrationHawkEye.get(),
			IntegrationPrism.get()
		);
				
		// Notify console we're ready
		log(Texts.ready);
		
		// Enable metrics 
		if (Config.get().metrics) Metrics.get(this).attemptEnable();
		
		// Notify events
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				// Notify our services of things
				IntegrationManager.get().notify(NotifyEvent.Loaded);
				
				// Enable FactionsPlusScoreboard - this is also so we can easily depend on compatibility plugins
				FactionsPlusScoreboard.get().enable();
				
				// If wilderness regen is enabled, launch it
				if (Config.get().wildernessregenEnabled) WildernessChunks.get().startCheck();
				
				// Patreon Badge
				PatreonBadge.get().setup();
				PatreonBadge.get().display();
			}
		});
	}
	
	public final void disable() {
		// Remove any commands we've added
		CommandManager.get().remove();
		
		// Save all the FactionData collections  
		for(FactionData data : FactionData.getAll()) data.save();
		
		FactionsPlusScoreboard.get().disable();
		
		if (FactionsManager.get().determineVersion() == FactionsVersion.FactionsUUID) FactionsUUIDTools.get().addFactionsUUIDWarpCommands();
		
		// Notify events
		IntegrationManager.get().notify(NotifyEvent.Stopping);
		
		// Disable integrations 
		IntegrationManager.get().disableIntegrations();
		
		// Stop Wilderness Chunks if its enabled 
		WildernessChunks.get().stopCheck();
		
		// Disable metrics 
		try {
			Metrics.get().disable();
		} catch (Throwable e) {
			this.logError(e);
		}
		
		// Stop our watch
		Config.get().watchStop();
	}
	
	// Manage our listeners, only add as we need it 
	public final void manageListeners() {
		// Always enable our core listener
		if ( ! this.isListenerEnabled(CoreListener.get())) {
			this.addListener(CoreListener.get());
		}
		
		// Enable jail listener if jails are enabled 
		if (Config.get().enableJails && ! this.isListenerEnabled(JailListener.get())) {
			this.addListener(JailListener.get());
		} else if (this.isListenerEnabled(JailListener.get())) {
			this.removeListener(JailListener.get());
		}
		
		// Enable scoreboard listener if scoreboards are enabled 
		if (Config.get().scoreboard_enabled && ! this.isListenerEnabled(ScoreboardListener.get())) {
			this.addListener(ScoreboardListener.get());
		} else if (this.isListenerEnabled(ScoreboardListener.get())) {
			this.removeListener(ScoreboardListener.get());
		}
		
	}
}
