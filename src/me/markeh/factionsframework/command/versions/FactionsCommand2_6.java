package me.markeh.factionsframework.command.versions;

import me.markeh.factionsframework.command.FactionsCommand;

public abstract class FactionsCommand2_6 extends FactionsCommand {
	public FactionsCommand2_6() { super(); }
	
	public FactionsCommand2_8_6Wrapper wrapper;
	
	public abstract void run();
	
	public final FactionsCommand2_6Wrapper getWrapper() {
		return new FactionsCommand2_6Wrapper(this, aliases, requiredArguments, optionalArguments);
	}
}
