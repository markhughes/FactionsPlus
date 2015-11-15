package me.markeh.factionsplus.integration.libsdisguises;

import me.markeh.factionsplus.integration.Integration;

public class IntegrationLibsDisguises extends Integration {
	
	private static IntegrationLibsDisguises instance = null;
	public static IntegrationLibsDisguises get() {
		if (instance == null) instance = new IntegrationLibsDisguises();
		
		return instance;
	}
	
	public IntegrationLibsDisguises() {
		this.setPluginName("LibsDisguises");
	}
	
	public final void setup() {
		this.setEventsClass(new IntegrationLibsDisguisesEvents());
	}
}
