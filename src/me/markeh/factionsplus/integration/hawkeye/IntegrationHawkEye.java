package me.markeh.factionsplus.integration.hawkeye;

import me.markeh.factionsplus.integration.Integration;

public class IntegrationHawkEye extends Integration {
	
	private static IntegrationHawkEye instance = null;
	public static IntegrationHawkEye get() {
		if (instance == null) instance = new IntegrationHawkEye();
		
		return instance;
	}
	
	public IntegrationHawkEye() {
		this.setPluginName("HawkEye");
	}
	
	public final void setup() {
		this.setEventsClass(new IntegrationHawkEyeEvents());
	}

}
