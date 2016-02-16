package me.markeh.factionsplus.conf;

import me.markeh.factionsplus.conf.obj.Configuration;
import me.markeh.factionsplus.conf.obj.Option;

public class Texts extends Configuration<Texts> {
	public Texts() {
		this.setName("texts");	
	}
	
	private static Texts i;
	public static Texts get() {
		if (i == null) i = new Texts();
		
		return i;
	}
	
	// ------------------------------
	//  General Messages 
	// ------------------------------
	
	@Option(section = "General", name = "Ready") 
	public static String ready = "Ready.";
	
	@Option(section = "General", name = "PlayerNotFound") 
	public static String playerNotFound = "<red>Player not found.";
	
	@Option(section = "General", name = "PlayerNotInYourFaction") 
	public static String playerNotInYourFaction = "<aqua>%s <red>is not in your faction!";
	
	@Option(section = "General", name = "PlayerOnlycommand") 
	public static String playerOnlyCommand = "<red>This is a player-only command.";

	@Option(section = "General", name = "PlayerMustHaveFaction") 
	public static String playerMustHaveFaction = "<red>You have to be in a faction to do that.";

	@Option(section = "General", name = "InternalNoPermission") 
	public static String internalNoPermission = "<red>Your don't have permission to do that.";
	
	@Option(section = "General", name = "PuginReloaded") 
	public static String pluginReloaded = "<green>Reloaded!";
	
	// ------------------------------
	//  Commands
	// ------------------------------
	
	// ------------------------------ 
	//  Jail Related
	// ------------------------------ 
	
	@Option(section = "Jails", name = "NotEnabled") 
	public static String jails_notEnabled = "<red>Jails aren't enabled on this server.";

	// -- CmdJail -- 
	@Option(section = "Jails", name = "JailCommand.Description") 
	public static String cmdJail_description = "Jail a member of your faction";
	
	@Option(section = "Jails", name = "JailCommand.NoPermission") 
	public static String cmdJail_noPermission = "You don't have permission to jail players!";
	
	@Option(section = "Jails", name = "JailCommand.BadRank") 
	public static String cmdJail_badRank = "<red>Your rank is not high enough to use jails.";
	
	@Option(section = "Jails", name = "JailCommand.NotSet") 
	public static String cmdJail_notSet = "<red>There is no jail location set.";
	
	@Option(section = "Jails", name = "JailCommand.NotInLand") 
	public static String cmdJail_notInLand = "<red>Your jail location is no longer in your Faction land.";
	
	@Option(section = "Jails", name = "JailCommand.Sent") 
	public static String cmdJail_sent = "<aqua>%s <green>has been sent to jail!";
	
	// -- CmdSetJail
	@Option(section = "Jails", name = "SetJailCommand.NoPermission") 
	public static String cmdSetJail_noPermission = "You don't have permission to set a jail location!";
	
	@Option(section = "Jails", name = "SetJailCommand.BadRank") 
	public static String cmdSetJail_badRank = "<red>Your rank is not high enough to set jails";
	
	@Option(section = "Jails", name = "SetJailCommand.NotInLand") 
	public static String cmdSetJail_notInLand = "<red>Your jail location must be in your land.";
	
	@Option(section = "Jails", name = "SetJailCommand.JailSet") 
	public static String cmdSetJail_jailSet = "<green>Jail location has been set to your current position.";
	
	// -- CmdUnjail
	@Option(section = "Jails", name = "UnjailCommand.NoPermission") 
	public static String cmdUnjail_noPermission = "<red>You don't have permission to unjail players!";
	
	@Option(section = "Jails", name = "UnjailCommand.BadRank") 
	public static String cmdUnjail_badRank = "<red>Your rank is not high enough to unjail.";
	
	@Option(section = "Jails", name = "UnjailCommand.NotJailed") 
	public static String cmdUnjail_notJailed = "<red>%s is not jailed!";
	
	@Option(section = "Jails", name = "UnjailCommand.NotifyJailedPlayer") 
	public static String cmdUnjail_notifyJailedPlayer = "<green>You have been released from jail!";
	
	@Option(section = "Jails", name = "UnjailCommand.NotifyUnjailer") 
	public static String cmdUnJail_notifyUnjailer = "<green>%s has been released from jail.";

	
	// ------------------------------ 
	//  Rules Related
	// ------------------------------ 
	
	@Option(section = "Rules", name = "NotEnabled") 
	public static String rules_notEnabled = "<red>Rules aren't enabled on this server.";
	
	@Option(section = "Rules", name = "NoRules") 
	public static String rules_noRules = "<red>Your faction has not listed their rules!";
	
	// -- CmdRules
	@Option(section = "Rules", name = "RulesCommand.NoPermission") 
	public static String cmdRules_noPermission = "<red>You don't have permission to view the rules.";

	@Option(section = "Rules", name = "RulesCommand.Description") 
	public static String cmdRules_description = "View your faction rules";
	
	@Option(section = "Rules", name = "RulesCommand.Layout") 
	public static String cmdRules_layout = "<i>. <rule>";

	// -- CmdAddRule
	@Option(section = "Rules", name = "AddRuleCommand.NoPermission") 
	public static String cmdAddRule_noPermission = "<red>You don't have permission to add rules.";
	
	@Option(section = "Rules", name = "AddRuleCommand.BadRank") 
	public static String cmdAddRule_badRank = "<red>Your rank is not high enough to add rules!";
	
	@Option(section = "Rules", name = "AddRuleCommand.Descripion") 
	public static String cmdAddRule_description = "Add faction rules";
	
	@Option(section = "Rules", name = "AddRuleCommand.Added") 
	public static String cmdAddRule_added = "<green>Rule added!";
	
	// ------------------------------ 
	//  Announcements Related
	// ------------------------------ 
	
	@Option(section = "Announcements", name = "NotEnabled") 
	public static String announcements_notEnabled = "<red>Announcements aren't enabled on this server.";
	
	@Option(section = "Announcements", name = "CmdAnnounce.NoPermission") 
	public static String cmdAnnounce_noPermission = "<red>You don't have permission to make announcements.";

	// ------------------------------ 
	//  Warps Related
	// ------------------------------ 
	
	@Option(section = "Warps", name = "NotEnabled") 
	public static String warps_notEnabled = "<red>Warps aren't enabled on this server.";
	
	// ------------------------------ 
	//  Scoreboard Related
	// ------------------------------ 
	
	@Option(section = "Scoreboard", name = "CmdScoreboard.NoPermission") 
	public static String cmdScoreboard_noPermission = "<red>You don't have permission to toggle the scoreboard.";

	@Option(section = "Scoreboard", name = "CmdScoreboard.Description") 
	public static String cmdScoreboard_description = "Toggle the scoreboard";

}
