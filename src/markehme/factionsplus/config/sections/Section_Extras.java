package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public final class Section_Extras {
	//XXX: I may use the terms ID, key, alias, config option   interchangeably to mean the same thing.
	
	@Option(oldAliases_alwaysDotted={
		"disableUpdateCheck"
	}, realAlias_inNonDottedFormat = "disableUpdateCheck" )
	public  final _boolean disableUpdateCheck=new _boolean(false);
	
	@Section(realAlias_neverDotted="Protection")
	public final SubSection_Protection protection=new SubSection_Protection();
	
	@Section(
			realAlias_neverDotted = "disguise" )
	public final SubSection_Disguise _disguise=new SubSection_Disguise();  
	
}
