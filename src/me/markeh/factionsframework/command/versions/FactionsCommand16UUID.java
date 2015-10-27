package me.markeh.factionsframework.command.versions;

import me.markeh.factionsframework.command.FactionsCommand;

public abstract class FactionsCommand16UUID extends FactionsCommand {
	
	public FactionsCommand16UUID() { super(); }
	
	public FactionsCommand16UUIDWrapper wrapper;
	
	public abstract void run();
	
	public final FactionsCommand16UUIDWrapper getWrapper() {
		return new FactionsCommand16UUIDWrapper(this, aliases, requiredArguments, optionalArguments);
	}
}
