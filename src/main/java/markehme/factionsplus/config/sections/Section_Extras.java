package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.Option;
import markehme.factionsplus.config.Section;


public final class Section_Extras {

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
	
	@Section(
			realAlias_neverDotted = "Flight" )
	public final SubSection_Flight _Flight=new SubSection_Flight();  
	
	@Section(
			realAlias_neverDotted = "CreativeGates" )
	public final SubSection_CreativeGates _CreativeGates = new SubSection_CreativeGates();  
	
	@Section(
			realAlias_neverDotted = "Fixes" )
	public final SubSection_Fixes _Fixes = new SubSection_Fixes();  
	
	@Option(autoComment={"Warning: High Intensity/Resource Hog Check, prevents stuff like cobblestone grief using lava/water"},
			realAlias_inNonDottedFormat = "crossBorderLiquidFlowBlock" )
		public final _boolean		crossBorderLiquidFlowBlock	= new _boolean( false );
	
}
