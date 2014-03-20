package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.Option;

public class SubSection_Fixes {
	@Option(autoComment={"Currently Factions allows setting the Wilderness/Safezone/Warzone to enemy, etc. These options stops that."},
			realAlias_inNonDottedFormat = "disallowChangingRelationshipToWilderness" )
	public  final _boolean disallowChangingRelationshipToWilderness = new _boolean(true);
	
	@Option(
			realAlias_inNonDottedFormat = "disallowChangingRelationshipToSafezone" )
	public  final _boolean disallowChangingRelationshipToSafezone = new _boolean(true);
	
	@Option(
			realAlias_inNonDottedFormat = "disallowChangingRelationshipToWarzone" )
	public  final _boolean disallowChangingRelationshipToWarzone = new _boolean(true);
	
	

}
