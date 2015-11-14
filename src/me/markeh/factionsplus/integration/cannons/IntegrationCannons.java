package me.markeh.factionsplus.integration.cannons;

import me.markeh.factionsplus.integration.Integration;

public class IntegrationCannons extends Integration {
	
	private static IntegrationCannons instance = null;
	public static IntegrationCannons get() {
		if (instance == null) instance = new IntegrationCannons();
		
		return instance;
	}
	
	public IntegrationCannons() {
		this.setPluginName("Cannons");
	}
	
	public final void setup() {
		this.setEventsClass(new IntegrationCannonsEvents());
	}
}
