package me.markeh.factionsplus.integration.showcasestandalone;

import me.markeh.factionsplus.integration.Integration;

public class IntegrationShowCaseStandalone extends Integration {
	
	private static IntegrationShowCaseStandalone instance = null;
	public static IntegrationShowCaseStandalone get() {
		if (instance == null) instance = new IntegrationShowCaseStandalone();
		
		return instance;
	}
	
	public IntegrationShowCaseStandalone() {
		this.setPluginName("ShowCaseStandalone");
	}
	
	public final void setup() {
		this.setEventsClass(new IntegrationShowCaseStandaloneEvents());
	}
}
