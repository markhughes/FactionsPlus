package me.markeh.factionsplus.integration.disguisecraft;

import me.markeh.factionsplus.integration.Integration;

public class IntegrationDisguiseCraft extends Integration {
	
	private static IntegrationDisguiseCraft instance = null;
	public static IntegrationDisguiseCraft get() {
		if (instance == null) instance = new IntegrationDisguiseCraft();
		
		return instance;
	}
	
	public IntegrationDisguiseCraft() {
		this.setPluginName("DisguiseCraft");
	}
	
	public final void setup() {
		this.setEventsClass(new IntegrationDisguiseCraftEvents());
	}
}
