package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class SubSection_Protection {
	
	@Section(
			realAlias_neverDotted = "LWC" )
	public final SubSection_LWC	_lwc							= new SubSection_LWC();
	
	// FIXME: this interferes with LWC's option
	@Option(autoComment={"unused"},
			realAlias_inNonDottedFormat = "removeSignProtectionOnClaim" )
	public final _boolean		removeSignProtectionOnClaim		= new _boolean( false );
	
	
	@Option(autoComment={"unused"},
		realAlias_inNonDottedFormat = "allowShopsInWilderness" )
	public final _boolean		allowShopsInWilderness			= new _boolean( false );
	
	
	@Option(autoComment={"unused"},
		realAlias_inNonDottedFormat = "allowShopsInTerritory" )
	public final _boolean		allowShopsInTerritory			= new _boolean( false );
	
	
	@Option(autoComment={"unused"},
		realAlias_inNonDottedFormat = "allowSignProtectionInWilderness" )
	public final _boolean		allowSignProtectionInWilderness	= new _boolean( false );
	
	
	@Option(autoComment={"unused"},
		realAlias_inNonDottedFormat = "allowSignProtectionInTerritory" )
	public final _boolean		allowSignProtectionInTerritory	= new _boolean( false );
	
}
