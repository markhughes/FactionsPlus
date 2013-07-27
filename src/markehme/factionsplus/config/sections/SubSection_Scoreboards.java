package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.Option;

public class SubSection_Scoreboards {
	@Option(
			realAlias_inNonDottedFormat = "showScoreboardOfMap" )
	public  final _boolean showScoreboardOfMap = new _boolean(false);

	@Option(
			realAlias_inNonDottedFormat = "showScoreboardOfFactions" )
	public  final _boolean showScoreboardOfFactions = new _boolean(true);

	@Option(
			realAlias_inNonDottedFormat = "sendScoreboardOnJoin" )
	public  final _boolean sendScoreboardOnJoin = new _boolean(true);
	
	@Option(
			realAlias_inNonDottedFormat = "secondsBetweenScoreboardUpdates" )
	public  final _int secondsBetweenScoreboardUpdates = new _int(5);
	
	
}
