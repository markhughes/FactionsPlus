package me.markeh.factionsframework.command.requirements;

import org.bukkit.entity.Player;

import me.markeh.factionsframework.command.FactionsCommand;

public class ReqIsPlayer extends Requirement {
	
	private static ReqIsPlayer i;
	public static ReqIsPlayer get() {
		if (i == null) i = new ReqIsPlayer();
		return i;
	}
	
	@Override
	public boolean isMet(FactionsCommand command) {
		if (command.sender != null) return true;
		if (command.sender instanceof Player) return true;
		
		command.sender.sendMessage(command.colourise("<red>You must be a player to run this command."));
		return false;
	}
}
