package markehme.factionsplus.config.sections;

import org.bukkit.ChatColor;

import markehme.factionsplus.config.Option;

public final class Section_Templates {
	@Option(	autoComment={"This is what is displayed for announcements"},
			realAlias_inNonDottedFormat = "announcement_message"
		)
	public  final _string announcement_message=new _string("<red>!1 <white>announced: !2");
	
	@Option(	autoComment={"This is ONLY send to the player setting the warp."},
			realAlias_inNonDottedFormat = "warp_created"
		)
	public  final _string warp_created=new _string("<green>Warp <white>!1 <green>set for your Faction!");
	
	@Option(	autoComment={"This is announced to the Faction when a warp is set"},
			realAlias_inNonDottedFormat = "notify_warp_created"
		)
	public  final _string notify_warp_created=new _string("!1 created a warp in your faction called !2");
	
	@Option(	autoComment={"Sent when a warp does not exist."},
			realAlias_inNonDottedFormat = "warp_non_existant"
		)
	public  final _string warp_non_existant=new _string("That warp does not exist.");
	
	@Option(	autoComment={"Sent when a player is warped."},
			realAlias_inNonDottedFormat = "warped_to"
		)
	public  final _string warped_to=new _string("<green>Warped to <white>!1");
	
	@Option(	autoComment={"Sent when a warp is removed."},
			realAlias_inNonDottedFormat = "warped_removed"
		)
	public  final _string warped_removed=new _string("<green>The warp <white>!1<green> was removed.");
	
	@Option(	autoComment={"Sent when a warp has an incorrect password."},
			realAlias_inNonDottedFormat = "warp_incorrect_password"
		)
	public  final _string warp_incorrect_password=new _string("<red>Incorrect password, please use /f warp [warp] <password>");
	
	@Option(	autoComment={"When a players ranking is not high enough, this is sent through."},
			realAlias_inNonDottedFormat = "create_warp_denied_badrank"
		)
	public	final _string create_warp_denied_badrank = new _string("<red>Sorry, your ranking is not high enough to create warps!");
	
	@Option( 	autoComment={"Message sent if the player is not in their own territory."},
			realAlias_inNonDottedFormat = "create_warp_denied_badterritory"
		)
	public	final _string create_warp_denied_badterritory = new _string("<red>You must be in your own territory to create a warp!");
	
	@Option(	autoComment={"Message sent when reached max warps for the players Faction."},
			realAlias_inNonDottedFormat = "warps_reached_max"
		)
	public 	final _string warps_reached_max = new _string("<red>You have reached the max amount of warps.");
	
	@Option(	autoComment={"Messaged sent to player when a warp already exists."},
			realAlias_inNonDottedFormat = "warps_already_exists"
		)
	public	final _string warps_already_exists = new _string("<red>A warp already exists with that name.");
	
	@Option(	autoComment={"Sent to a player when they have been jailed."},
			realAlias_inNonDottedFormat = "jailed_message"
		)
	public  final _string jailed_message = new _string("<red>You have been Jailed! If you are unhappy with this faction, you can leave the Faction.");
	
	@Option(	autoComment={"Sent to Faction leaders and officers when /f need is called."},
			realAlias_inNonDottedFormat = "faction_need"
		)
	public  final _string faction_need = new _string("<white>The player !1 is looking for a Faction! You can invite them with <green>/f inv !1");

}
