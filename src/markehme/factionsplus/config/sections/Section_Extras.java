package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.Option;
import markehme.factionsplus.config.Section;


public final class Section_Extras {
	//XXX: I may use the terms ID, key, alias, config option   interchangeably to mean the same thing.
	
	@Option(oldAliases_alwaysDotted={
		"disableUpdateCheck"
	}, realAlias_inNonDottedFormat = "disableUpdateCheck" )
	public  final _boolean disableUpdateCheck=new _boolean(false);
	
	@Section(
			realAlias_neverDotted="Scoreboards" )
	public  final SubSection_Scoreboards _scoreboards=new SubSection_Scoreboards();
		
	@Section(realAlias_neverDotted="Protection")
	public final SubSection_Protection _protection=new SubSection_Protection();
	
	@Section(
			realAlias_neverDotted = "disguise" )
	public final SubSection_Disguise _disguise=new SubSection_Disguise();  
	
	@Section(
			realAlias_neverDotted = "MultiVerse" )
	public final SubSection_MultiVerse _MultiVerse=new SubSection_MultiVerse();  

	@Section(
			realAlias_neverDotted = "Cannons" )
	public final SubSection_Cannons _Cannons=new SubSection_Cannons();  

	@Option(autoComment={"Warning: High Intensity/Resource Hog Check, prevents stuff like cobblestone grief using lava/water"},
			realAlias_inNonDottedFormat = "crossBorderLiquidFlowBlock" )
		public final _boolean		crossBorderLiquidFlowBlock	= new _boolean( false );
	
}
