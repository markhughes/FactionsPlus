package me.markeh.factionsframework.command.versions;

import me.markeh.factionsframework.command.FactionsCommand;

public abstract class FactionsCommand2_6 extends FactionsCommand {
	public FactionsCommand2_6() { super(); }
	
	public FactionsCommand2_XWrapper wrapper;
	
	public abstract void run();
	
	public final FactionsCommand2_6Wrapper getWrapper() {
		return new FactionsCommand2_6Wrapper(this, aliases, requiredArguments, optionalArguments);
	}
}
