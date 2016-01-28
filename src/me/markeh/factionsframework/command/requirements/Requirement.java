package me.markeh.factionsframework.command.requirements;

import me.markeh.factionsframework.command.FactionsCommand;

public abstract class Requirement {
	public abstract boolean isMet(FactionsCommand command);
}
