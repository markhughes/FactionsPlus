package me.markeh.factionsframework.command.requirements;

import me.markeh.factionsframework.command.FactionsCommand;

public class ReqHasFaction extends Requirement {
	
	private static ReqHasFaction i;
	public static ReqHasFaction get() {
		if (i == null) i = new ReqHasFaction();
		return i;
	}
	
	@Override
	public boolean isMet(FactionsCommand command) {
		if (command.fplayer.hasFaction()) return true;
		
		command.fplayer.msg("<reset><red>You must be in a <bold>faction<reset><red> to run this command!");
		return false;
	}
}
