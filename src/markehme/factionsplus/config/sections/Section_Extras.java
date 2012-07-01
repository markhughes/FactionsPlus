package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class Section_Extras {
	//XXX: I may use the terms ID, key, alias, config option   interchangeably to mean the same thing.
	
	@Option(oldAliases_alwaysDotted={
		"disableUpdateCheck"
	}, realAlias_inNonDottedFormat = "disableUpdateCheck" )
	public _boolean disableUpdateCheck=new _boolean(false);
	
	@Section(
			realAlias_neverDotted = "lwc" )
	public final SubSection_LWC lwc=new SubSection_LWC();
	
	@Section(
			realAlias_neverDotted = "disguise" )
	public final SubSection_Disguise disguise=new SubSection_Disguise();  
	
}
