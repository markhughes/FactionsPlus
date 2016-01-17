package me.markeh.factionsframework.command.versions;

import me.markeh.factionsframework.command.FactionsCommand;

public abstract class FactionsCommand1_6UUID extends FactionsCommand {
	
	public FactionsCommand1_6UUID() { super(); }
	
	public FactionsCommand1_6UUIDWrapper wrapper;
	
	public abstract void run();
	
	public final FactionsCommand1_6UUIDWrapper getWrapper() {
		return new FactionsCommand1_6UUIDWrapper(this, aliases, requiredArguments, optionalArguments);
	}
}
