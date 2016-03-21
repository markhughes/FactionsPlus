package me.markeh.factionsplus.conf;

import me.markeh.factionsplus.conf.obj.Configuration;
import me.markeh.factionsplus.conf.obj.Option;

public class Texts extends Configuration<Texts> {
	public Texts() {
		this.setName("texts");	
		
		this.load().save();
	}
	
	private static Texts i;
	public static Texts get() {
		if (i == null) i = new Texts();
		
		return i;
	}
	
	// ------------------------------
	//  General Messages 
	// ------------------------------
	
	@Option(section = "General", name = "Ready", comment = "") 
	public String ready = "Ready.";
	
	@Option(section = "General", name = "PlayerNotFound", comment = "") 
	public String playerNotFound = "<red>Player not found.";
	
	@Option(section = "General", name = "PlayerNotInYourFaction") 
	public String playerNotInYourFaction = "<aqua>%s <red>is not in your faction!";
	
	@Option(section = "General", name = "PlayerOnlycommand") 
	public String playerOnlyCommand = "<red>This is a player-only command.";

	@Option(section = "General", name = "PlayerMustHaveFaction") 
	public String playerMustHaveFaction = "<red>You have to be in a faction to do that.";

	@Option(section = "General", name = "InternalNoPermission") 
	public String internalNoPermission = "<red>Your don't have permission to do that.";
	
	@Option(section = "General", name = "PuginReloaded") 
	public String pluginReloaded = "<green>Reloaded!";
	
	// ------------------------------
	//  Commands
	// ------------------------------
	
	// ------------------------------ 
	//  Jail Related
	// ------------------------------ 
	
	@Option(section = "Jails", name = "NotEnabled") 
	public String jails_notEnabled = "<red>Jails aren't enabled on this server.";

	// -- CmdJail -- 
	@Option(section = "Jails", name = "JailCommand.Description") 
	public String cmdJail_description = "Jail a member of your faction";
	
	@Option(section = "Jails", name = "JailCommand.NoPermission") 
	public String cmdJail_noPermission = "You don't have permission to jail players!";
	
	@Option(section = "Jails", name = "JailCommand.BadRank") 
	public String cmdJail_badRank = "<red>Your rank is not high enough to use jails.";
	
	@Option(section = "Jails", name = "JailCommand.NotSet") 
	public String cmdJail_notSet = "<red>There is no jail location set.";
	
	@Option(section = "Jails", name = "JailCommand.NotInLand") 
	public String cmdJail_notInLand = "<red>Your jail location is no longer in your Faction land.";
	
	@Option(section = "Jails", name = "JailCommand.Sent") 
	public String cmdJail_sent = "<aqua>%s <green>has been sent to jail!";
	
	// -- CmdSetJail
	@Option(section = "Jails", name = "SetJailCommand.NoPermission") 
	public String cmdSetJail_noPermission = "You don't have permission to set a jail location!";
	
	@Option(section = "Jails", name = "SetJailCommand.BadRank") 
	public String cmdSetJail_badRank = "<red>Your rank is not high enough to set jails";
	
	@Option(section = "Jails", name = "SetJailCommand.NotInLand") 
	public String cmdSetJail_notInLand = "<red>Your jail location must be in your land.";
	
	@Option(section = "Jails", name = "SetJailCommand.JailSet") 
	public String cmdSetJail_jailSet = "<green>Jail location has been set to your current position.";
	
	// -- CmdUnjail
	@Option(section = "Jails", name = "UnjailCommand.NoPermission") 
	public String cmdUnjail_noPermission = "<red>You don't have permission to unjail players!";
	
	@Option(section = "Jails", name = "UnjailCommand.BadRank") 
	public String cmdUnjail_badRank = "<red>Your rank is not high enough to unjail.";
	
	@Option(section = "Jails", name = "UnjailCommand.NotJailed") 
	public String cmdUnjail_notJailed = "<red>%s is not jailed!";
	
	@Option(section = "Jails", name = "UnjailCommand.NotifyJailedPlayer") 
	public String cmdUnjail_notifyJailedPlayer = "<green>You have been released from jail!";
	
	@Option(section = "Jails", name = "UnjailCommand.NotifyUnjailer") 
	public String cmdUnJail_notifyUnjailer = "<green>%s has been released from jail.";

	
	// ------------------------------ 
	//  Rules Related
	// ------------------------------ 
	
	@Option(section = "Rules", name = "NotEnabled") 
	public String rules_notEnabled = "<red>Rules aren't enabled on this server.";
	
	@Option(section = "Rules", name = "NoRules") 
	public String rules_noRules = "<red>Your faction has not listed their rules!";
	
	// -- CmdRules
	@Option(section = "Rules", name = "RulesCommand.NoPermission") 
	public String cmdRules_noPermission = "<red>You don't have permission to view the rules.";

	@Option(section = "Rules", name = "RulesCommand.Description") 
	public String cmdRules_description = "View your faction rules";
	
	@Option(section = "Rules", name = "RulesCommand.Layout") 
	public String cmdRules_layout = "<i>. <rule>";

	// -- CmdAddRule
	@Option(section = "Rules", name = "AddRuleCommand.NoPermission") 
	public String cmdAddRule_noPermission = "<red>You don't have permission to add rules.";
	
	@Option(section = "Rules", name = "AddRuleCommand.BadRank") 
	public String cmdAddRule_badRank = "<red>Your rank is not high enough to add rules!";
	
	@Option(section = "Rules", name = "AddRuleCommand.Descripion") 
	public String cmdAddRule_description = "Add faction rules";
	
	@Option(section = "Rules", name = "AddRuleCommand.Added") 
	public String cmdAddRule_added = "<green>Rule added!";
	
	// ------------------------------ 
	//  Announcements Related
	// ------------------------------ 
	
	@Option(section = "Announcements", name = "NotEnabled") 
	public String announcements_notEnabled = "<red>Announcements aren't enabled on this server.";
	
	@Option(section = "Announcements", name = "CmdAnnounce.NoPermission") 
	public String cmdAnnounce_noPermission = "<red>You don't have permission to make announcements.";

	// ------------------------------ 
	//  Warps Related
	// ------------------------------ 
	
	@Option(section = "Warps", name = "NotEnabled") 
	public String warps_notEnabled = "<red>Warps aren't enabled on this server.";
	
	// ------------------------------ 
	//  Scoreboard Related
	// ------------------------------ 
	
	@Option(section = "Scoreboard", name = "CmdScoreboard.NoPermission") 
	public String cmdScoreboard_noPermission = "<red>You don't have permission to toggle the scoreboard.";

	@Option(section = "Scoreboard", name = "CmdScoreboard.Description") 
	public String cmdScoreboard_description = "Toggle the scoreboard";

}
