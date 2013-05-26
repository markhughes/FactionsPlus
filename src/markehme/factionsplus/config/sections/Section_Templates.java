package markehme.factionsplus.config.sections;

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
	
	
	
	@Option(	autoComment={"Sent to a player when they have been jailed."},
			realAlias_inNonDottedFormat = "jailed_message"
		)
	public  final _string jailed_message=new _string("<red>You have been Jailed! If you are unhappy with this faction, you can leave the Faction.");
	
	


}
