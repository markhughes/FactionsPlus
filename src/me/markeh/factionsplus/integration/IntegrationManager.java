package me.markeh.factionsplus.integration;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import me.markeh.factionsframework.objs.NotifyEvent;

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
	
	public void disableIntegration(Integration integration) {
		if ( ! this.integrations.contains(integration)) return;
		
		integration.getEvents().disable();
		
		this.integrations.remove(integration);
	}
	
	public void notify(NotifyEvent event) {
		for (Integration integration : this.integrations) integration.getEvents().notify(event);
	}
	
	public void enableIntegrations() {
		for(Integration integration : this.integrations) {
			// check if the integration is enabled
			if (Bukkit.getPluginManager().isPluginEnabled(integration.getPluginName())) {
				// setup
				integration.setup();
				
				// enable events 
				integration.getEvents().enable();
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
