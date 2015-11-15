package me.markeh.factionsplus.integration.deadbolt;

import me.markeh.factionsplus.integration.Integration;

public class IntegrationDeadbolt extends Integration {
	
	private static IntegrationDeadbolt instance = null;
	public static IntegrationDeadbolt get() {
		if (instance == null) instance = new IntegrationDeadbolt();
		
		return instance;
	}
	
	public IntegrationDeadbolt() {
		this.setPluginName("Deadbolt");
	}
	
	public final void setup() {
		this.setEventsClass(new IntegrationDeadboltEvents());
	}
}
