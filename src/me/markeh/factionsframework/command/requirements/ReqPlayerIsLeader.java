package me.markeh.factionsframework.command.requirements;

import me.markeh.factionsframework.command.FactionsCommand;

public class ReqPlayerIsLeader extends Requirement {
	
	private static ReqPlayerIsLeader i;
	public static ReqPlayerIsLeader get() {
		if (i == null) i = new ReqPlayerIsLeader();
		return i;
	}
	
	@Override
	public boolean isMet(FactionsCommand command) {
		if (command.fplayer.isLeader()) return true;
		
		command.fplayer.msg("<reset><red>This command can <bold>only<reset><red> be run by a leader!");
		return false;
	}
}
