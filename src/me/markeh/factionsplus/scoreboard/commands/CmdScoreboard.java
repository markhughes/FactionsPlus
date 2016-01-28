package me.markeh.factionsplus.scoreboard.commands;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.command.requirements.ReqHasFaction;
import me.markeh.factionsframework.command.requirements.ReqIsPlayer;
import me.markeh.factionsframework.objs.Perm;
import me.markeh.factionsplus.conf.Texts;
import me.markeh.factionsplus.scoreboard.FactionsPlusScoreboard;
import me.markeh.factionsplus.scoreboard.obj.SBMenu;

public class CmdScoreboard extends FactionsCommand {
		
	public CmdScoreboard() {
		this.aliases.add("scoreboard");
		this.aliases.add("sb");
		
		this.requiredPermissions.add(Perm.get("factionsplus.togglescoreboard", Texts.cmdScoreboard_noPermission));
		
		this.description = Texts.cmdScoreboard_description;
		
		this.errorOnTooManyArgs = false;
		
		this.addRequirement(ReqHasFaction.get());
		this.addRequirement(ReqIsPlayer.get());
	}
	
	@Override
	public void run() {
		String request = null;
		
		if (this.arguments.size() > 0) {
			request = this.arguments.get(0).trim();
			
			SBMenu<?> menu = FactionsPlusScoreboard.get().findMenu(request);
			
			if (menu == null) {
				if (request == "hide") {
					msg("<green>Scoreboard hidden!");
					FactionsPlusScoreboard.get().setFor(this.player, null);
					return; 
				}
				
				msg("<red>Unknown menu <gold>" + request);
				return;
			}
			
			FactionsPlusScoreboard.get().setFor(this.player, menu);
			return;
		}
		
		SBMenu<?> newMenu = FactionsPlusScoreboard.get().setNext(this.player);
		
		msg("<green>Menu changed to <gold>" + newMenu.getTitle());
	}
}
