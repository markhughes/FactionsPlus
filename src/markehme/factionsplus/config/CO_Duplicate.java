package markehme.factionsplus.config;

import org.bukkit.*;

import markehme.factionsplus.*;
import markehme.factionsplus.config.yaml.*;



public class CO_Duplicate extends COMetadata {
	
	private static final String				commentPrefixForDUPs	= "DUPLICATE #";
	private static final ChatColor			colorLineNumOnDuplicate	= ChatColor.RED;
	private static final ChatColor			colorOnDuplicate		= ChatColor.YELLOW;
	
	
	/**
	 * the duplicate of the active wid
	 */
	private final WYIdentifier<COMetadata>	appliesToWID;
	
	private final String					thePassedDottedFormatForThisWID;
	
	private final WYIdentifier<COMetadata>	theActiveFirstWID;
	
	
	/**
	 * @param wid
	 * @param dottedWID
	 * @param activeWID
	 */
	public CO_Duplicate( WYIdentifier<COMetadata> wid, String dottedWID, WYIdentifier activeWID ) {
		assert null != wid;
		assert Typeo.isValidAliasFormat( dottedWID );
		assert null != activeWID;
		appliesToWID = wid;
		thePassedDottedFormatForThisWID = dottedWID;
		theActiveFirstWID = activeWID;
	}
	
	
	@Override
	protected void override_apply() {
		// WYItem<COMetadata> newItem=
		appliesToWID.getParent().replaceAndTransformInto_WYComment( appliesToWID, commentPrefixForDUPs );
		
		Config.warn( "Duplicate config option encountered at line " + colorLineNumOnDuplicate
			+ appliesToWID.getLineNumber()
			+ ChatColor.RESET
			+ " and this was transformed into comment so that you can review it & know that it was ignored.\n"
			// + "This is how the line looks now(without leading spaces):\n"
			+ colorOnDuplicate + appliesToWID.toString() + "\n" + ChatColor.RESET + "the option at line " + ChatColor.AQUA
			+ theActiveFirstWID.getLineNumber() + ChatColor.RESET + " overriddes this duplicate with value " + ChatColor.AQUA
			+ theActiveFirstWID.getValue() );
		
		// return newItem;
	}
	
}
