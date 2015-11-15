package me.markeh.factionsplus.integration.lockette;

import me.markeh.factionsplus.integration.Integration;

public class IntegrationLockette extends Integration {
	
	private static IntegrationLockette instance = null;
	public static IntegrationLockette get() {
		if (instance == null) instance = new IntegrationLockette();
		
		return instance;
	}
	
	public IntegrationLockette() {
		this.setPluginName("Lockette");
	}
	
	public final void setup() {
		this.setEventsClass(new IntegrationLocketteEvents());
	}
}
