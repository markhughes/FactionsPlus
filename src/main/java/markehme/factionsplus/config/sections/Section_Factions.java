package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.Option;


public class Section_Factions {
	@Option(autoComment={"Forces the first letter to uppercase on Faction names"},
			realAlias_inNonDottedFormat = "factionNameFirstLetterForceUpperCase" )
		public final _boolean		factionNameFirstLetterForceUpperCase		= new _boolean( false );

}
