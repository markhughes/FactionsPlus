package me.markeh.factionsplus.integration;

import me.markeh.factionsframework.objs.NotifyEvent;

public abstract class IntegrationEvents {
	public abstract void enable();
	public abstract void disable();
	
	// Optional override
	public void notify(NotifyEvent event) { }
}
