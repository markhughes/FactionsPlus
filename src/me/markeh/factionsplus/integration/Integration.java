package me.markeh.factionsplus.integration;

public abstract class Integration {
	private IntegrationEvents events = null;
	private String pluginName = null;
	
	public final void setEventsClass(IntegrationEvents eventsClass) { this.events = eventsClass; }
	public final IntegrationEvents getEvents() { return this.events; }
	
	public final void setPluginName(String name) { this.pluginName = name; }
	public final String getPluginName() { return this.pluginName; }
	
	public abstract void setup();
}
