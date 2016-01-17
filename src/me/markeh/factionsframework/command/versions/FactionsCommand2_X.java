package me.markeh.factionsframework.command.versions;

import me.markeh.factionsframework.command.FactionsCommand;

public abstract class FactionsCommand2_X extends FactionsCommand {
	
	public FactionsCommand2_X() { super(); }
	
	public FactionsCommand2_XWrapper wrapper;
	
	public abstract void run();
	
	public final FactionsCommand2_XWrapper getWrapper() {
		return new FactionsCommand2_XWrapper(this, aliases, requiredArguments, optionalArguments);
	}
}
