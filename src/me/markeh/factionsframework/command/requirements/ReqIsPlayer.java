package me.markeh.factionsframework.command.requirements;

import me.markeh.factionsframework.command.FactionsCommand;

public class ReqIsPlayer extends Requirement {
	
	private static ReqIsPlayer i;
	public static ReqIsPlayer get() {
		if (i == null) i = new ReqIsPlayer();
		return i;
	}
	
	@Override
	public boolean isMet(FactionsCommand command) {
		if (command.player != null) return true;
		
		command.fplayer.msg("<red>You must be a player to run this command.");
		return false;
	}
}
