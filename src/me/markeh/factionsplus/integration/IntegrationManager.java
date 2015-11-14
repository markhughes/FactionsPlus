package me.markeh.factionsplus.integration;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import me.markeh.factionsframework.objs.NotifyEvent;
import me.markeh.factionsplus.FactionsPlus;

public class IntegrationManager {
	private static IntegrationManager instance = null;
	public static IntegrationManager get() {
		if (instance == null) instance = new IntegrationManager();
		
		return instance;
	}
	
	private List<Integration> integrations = new ArrayList<Integration>();
	
	public void addIntegration(Integration integration) {
		this.integrations.add(integration);
	}
	
	public void addIntegrations(Integration... integrations) {
		for (Integration integration : integrations) {
			this.integrations.add(integration);
		}
	}
	
	public void disableIntegration(Integration integration) {
		if ( ! this.integrations.contains(integration) || integration == null) return;
		
		integration.getEvents().disable();
		
		this.integrations.remove(integration);
	}
	
	public void notify(NotifyEvent event) {
		if (event == NotifyEvent.Loaded) {
			this.enableIntegrations();
		}
		
		for (Integration integration : this.integrations) {
			if (integration.getEvents() == null) continue;
			
			integration.getEvents().notify(event);
		}
	}
	
	public void enableIntegrations() {
		for(Integration integration : this.integrations) {
			// check if the integration is enabled
			if (Bukkit.getPluginManager().isPluginEnabled(integration.getPluginName())) {
				// setup
				integration.setup();
				
				// enable events 
				integration.getEvents().enable();
				
				FactionsPlus.get().log("<gold>Enabled integration for <green><?>", integration.getPluginName());
			}
		}
	}
	
	public void disableIntegrations() {
		for(Integration integration : this.integrations) {
			// check if our events have been enabled 
			if (integration.getEvents() != null) {
				integration.getEvents().disable();
				integration.setEventsClass(null);
			}
		}
	}
}
