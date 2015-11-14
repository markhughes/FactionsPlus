package me.markeh.factionsplus.conf;

import me.markeh.factionsplus.conf.obj.Configuration;
import me.markeh.factionsplus.conf.obj.Option;

// TODO: Move colours to MassiveCore standard 

public class Texts extends Configuration {
	public Texts() {
		this.setName("texts");	
	}
	
	// ------------------------------
	//  General Messages 
	// ------------------------------
	
	@Option({"General", "Ready"})
	public static String ready = "Ready.";
	
	@Option({"General", "PlayerNotFound"})
	public static String playerNotFound = "<red>Player not found.";
	
	@Option({"General", "PlayerNotInYourFaction"})
	public static String playerNotInYourFaction = "<aqua>%s <red>is not in your faction!";
	
	@Option({"General", "PlayerOnlycommand"})
	public static String playerOnlyCommand = "<red>This is a player-only command.";

	@Option({"General", "PlayerMustHaveFaction"})
	public static String playerMustHaveFaction = "<red>You have to be in a faction to do that.";

	@Option({"General", "InternalNoPermission"})
	public static String internalNoPermission = "<red>Your don't have permission to do that.";
	
	@Option({"General", "PuginReloaded"})
	public static String pluginReloaded = "<green>Reloaded!";
	
	// ------------------------------
	//  Commands
	// ------------------------------
	
	// ------------------------------ 
	//  Jail Related
	// ------------------------------ 
	
	@Option({"Jails", "NotEnabled"})
	public static String jails_notEnabled = "<red>Jails aren't enabled on this server.";

	// -- CmdJail -- 
	@Option({"Jails", "JailCommand.Description"})
	public static String cmdJail_description = "Jail a member of your faction";
	
	@Option({"Jails", "JailCommand.NoPermission"})
	public static String cmdJail_noPermission = "You don't have permission to jail players!";
	
	@Option({"Jails", "JailCommand.BadRank"})
	public static String cmdJail_badRank = "<red>Your rank is not high enough to use jails.";
	
	@Option({"Jails", "JailCommand.NotSet"})
	public static String cmdJail_notSet = "<red>There is no jail location set.";
	
	@Option({"Jails", "JailCommand.NotInLand"})
	public static String cmdJail_notInLand = "<red>Your jail location is no longer in your Faction land.";
	
	@Option({"Jails", "JailCommand.Sent"})
	public static String cmdJail_sent = "<aqua>%s <green>has been sent to jail!";
	
	// -- CmdSetJail
	@Option({"Jails", "SetJailCommand.NoPermission"})
	public static String cmdSetJail_noPermission = "You don't have permission to set a jail location!";
	
	@Option({"Jails", "SetJailCommand.BadRank"})
	public static String cmdSetJail_badRank = "<red>Your rank is not high enough to set jails";
	
	@Option({"Jails", "SetJailCommand.NotInLand"})
	public static String cmdSetJail_notInLand = "<red>Your jail location must be in your land.";
	
	@Option({"Jails", "SetJailCommand.JailSet"})
	public static String cmdSetJail_jailSet = "<green>Jail location has been set to your current position.";
	
	// -- CmdUnjail
	@Option({"Jails", "UnjailCommand.NoPermission"})
	public static String cmdUnjail_noPermission = "<red>You don't have permission to unjail players!";
	
	@Option({"Jails", "UnjailCommand.BadRank"})
	public static String cmdUnjail_badRank = "<red>Your rank is not high enough to unjail.";
	
	@Option({"Jails", "UnjailCommand.NotJailed"})
	public static String cmdUnjail_notJailed = "<red>%s is not jailed!";
	
	@Option({"Jails", "UnjailCommand.NotifyJailedPlayer"})
	public static String cmdUnjail_notifyJailedPlayer = "<green>You have been released from jail!";
	
	@Option({"Jails", "UnjailCommand.NotifyUnjailer"})
	public static String cmdUnJail_notifyUnjailer = "<green>%s has been released from jail.";

	
	// ------------------------------ 
	//  Rules Related
	// ------------------------------ 
	
	@Option({"Rules", "NotEnabled"})
	public static String rules_notEnabled = "<red>Rules aren't enabled on this server.";
	
	@Option({"Rules", "NoRules"})
	public static String rules_noRules = "<red>Your faction has not listed their rules!";
	
	// -- CmdRules
	@Option({"Rules", "RulesCommand.NoPermission"})
	public static String cmdRules_noPermission = "<red>You don't have permission to view the rules.";

	@Option({"Rules", "RulesCommand.Description"})
	public static String cmdRules_description = "View your faction rules";
	
	@Option({"Rules", "RulesCommand.Layout"})
	public static String cmdRules_layout = "<i>. <rule>";

	// -- CmdAddRule
	@Option({"Rules", "AddRuleCommand.NoPermission"})
	public static String cmdAddRule_noPermission = "<red>You don't have permission to add rules.";
	
	@Option({"Rules", "AddRuleCommand.BadRank"})
	public static String cmdAddRule_badRank = "<red>Your rank is not high enough to add rules!";
	
	@Option({"Rules", "AddRuleCommand.Descripion"})
	public static String cmdAddRule_description = "Add faction rules";
	
	@Option({"Rules", "AddRuleCommand.Added"})
	public static String cmdAddRule_added = "<green>Rule added!";
	
	// ------------------------------ 
	//  Warps Related
	// ------------------------------ 
	
	@Option({"Warps", "NotEnabled"})
	public static String warps_notEnabled = "<red>Warps aren't enabled on this server.";





}
