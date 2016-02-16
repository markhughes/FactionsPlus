package me.markeh.factionsplus.commands;

import java.util.Date;

import me.markeh.factionsframework.command.FactionsCommand;
import me.markeh.factionsframework.command.requirements.ReqHasFaction;
import me.markeh.factionsframework.command.requirements.ReqIsPlayer;
import me.markeh.factionsframework.command.requirements.ReqPermission;
import me.markeh.factionsframework.objs.Perm;
import me.markeh.factionsplus.conf.Config;
import me.markeh.factionsplus.conf.FactionData;
import me.markeh.factionsplus.conf.Texts;

public class CmdAnnounce extends FactionsCommand {
	
	public CmdAnnounce() {
		this.aliases.add("announce");
		this.requiredArguments.add("message");
				
		this.description = Texts.cmdAddRule_description;
		
		this.addRequirement(ReqIsPlayer.get());
		this.addRequirement(ReqHasFaction.get());
		this.addRequirement(ReqPermission.get(Perm.get("factionsplus.announce", Texts.cmdAnnounce_noPermission)));

		this.errorOnTooManyArgs = false;
	}
	
	@Override
	public void run() {
		
		if ( ! Config.get().enableAnnouncements) {
			msg(Texts.announcements_notEnabled);
			return;
		}
		
		String announcement = "";
		
		for (String word : this.arguments) announcement += " " + word;
		
		FactionData fdata = FactionData.get(this.getFaction());
		
		if (new Date().getTime() + Config.get().announcementsCooldown * 1000 < fdata.lastAnnouncement) {
			msg("<red>You must wait " + Config.get().announcementsCooldown + " seconds between announements.");
		}
		
		fdata.lastAnnouncement = new Date().getTime();
		
		getFaction().msg("<white><bold> " + getPlayer().getName() + " <reset><white> announced: " + announcement);
	}
}
