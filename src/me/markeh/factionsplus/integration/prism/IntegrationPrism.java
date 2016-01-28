package me.markeh.factionsplus.integration.prism;

import me.markeh.factionsplus.integration.Integration;

public class IntegrationPrism extends Integration {
	
	private static IntegrationPrism instance = null;
	public static IntegrationPrism get() {
		if (instance == null) instance = new IntegrationPrism();
		
		return instance;
	}
	
	public IntegrationPrism() {
		this.setPluginName("Prism");
	}
	
	public final void setup() {
		this.setEventsClass(new IntegrationPrismEvents());
	}

}
