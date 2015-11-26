package me.markeh.factionsplus.commands;

import java.util.Date;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.objs.Perm;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.conf.FactionData;
import me.markeh.factionsplus.conf.Texts;

public class CmdAnnounce extends FactionsCommand {
	
	public CmdAnnounce() {
		this.aliases.add("announce");
		this.requiredArguments.add("message");
		
		this.requiredPermissions.add(Perm.get("factionsplus.announce", Texts.cmdAnnounce_noPermission));
		
		this.description = Texts.cmdAddRule_description;
		
		this.mustHaveFaction = true;
		
		this.errorOnTooManyArgs = false;

	}
	
	@Override
	public void run() {
		if ( ! this.isPlayer() ) {
			msg(Texts.playerOnlyCommand);
			return; 
		}
		
		if ( ! Config.get().enableAnnouncements) {
			msg(Texts.announcements_notEnabled);
			return;
		}
		
		String announcement = "";
		
		for (String word : this.arguments) announcement += " " + word;
		
		if (new Date().getTime() + Config.get().announcementsCooldown * 1000 < FactionData.get(this.faction).lastAnnouncement) {
			msg("<red>You must wait " + Config.get().announcementsCooldown + " seconds between announements.");
		}
		
		FactionData.get(this.faction).lastAnnouncement = new Date().getTime();
		
		this.faction.msg("<white><bold> " + this.player.getName() + " <reset><white> announced: " + announcement);
	}
}
