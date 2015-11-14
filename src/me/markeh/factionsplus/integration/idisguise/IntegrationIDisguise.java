package me.markeh.factionsplus.integration.idisguise;

import me.markeh.factionsplus.integration.Integration;

public class IntegrationIDisguise extends Integration {
	
	private static IntegrationIDisguise instance = null;
	public static IntegrationIDisguise get() {
		if (instance == null) instance = new IntegrationIDisguise();
		
		return instance;
	}
	
	public IntegrationIDisguise() {
		this.setPluginName("iDisguise");
	}
	
	public final void setup() {
		this.setEventsClass(new IntegrationIDisguiseEvents());
	}
}
