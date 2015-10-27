package me.markeh.factionsframework.command.versions;

import me.markeh.factionsframework.command.FactionsCommand;

public abstract class FactionsCommand2X extends FactionsCommand {
	
	public FactionsCommand2X() { super(); }
	
	public FactionsCommand2XWrapper wrapper;
	
	public abstract void run();
	
	public final FactionsCommand2XWrapper getWrapper() {
		return new FactionsCommand2XWrapper(this, aliases, requiredArguments, optionalArguments);
	}
}
