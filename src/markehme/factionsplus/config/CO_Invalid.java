package markehme.factionsplus.config;

import org.bukkit.*;

import markehme.factionsplus.*;
import markehme.factionsplus.config.yaml.*;



public class CO_Invalid extends COMetadata {
	
	private static final String				commentPrefixForINVALIDs	= "INVALID #";
	private static final ChatColor			colorOnINVALID				= ChatColor.YELLOW;
	
	
	private final WYIdentifier<COMetadata>	appliesToWID;
	private final String thePassedDottedFormatForThisWID;
	
	
	public CO_Invalid( WYIdentifier<COMetadata> wid, String dotted ) {
		assert null != wid;
		assert Typeo.isValidAliasFormat( dotted );
		appliesToWID = wid;
		thePassedDottedFormatForThisWID=dotted;
	}
	
	
	
	@Override
	protected WYItem<COMetadata> override_apply() {
		WYComment<COMetadata> newItem = appliesToWID.getParent().replaceAndTransformInto_WYComment( appliesToWID, commentPrefixForINVALIDs );
		FactionsPlus.warn( "Invalid config option\n" + colorOnINVALID + thePassedDottedFormatForThisWID + ChatColor.RESET
			+ " was auto commented at line "
			// // + fileConfig
			// + " at line "
			+ colorOnINVALID + appliesToWID.getLineNumber() + '\n'// +ChatColor.RESET
			// +
			// " and this was transformed into comment so that you can review it & know that it was ignored.\n"
			// + "This is how the line looks now(without leading spaces):\n"
			+ colorOnINVALID + appliesToWID.toString() );
		return newItem;
	}
}
