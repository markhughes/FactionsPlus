package markehme.factionsplus.config;

import markehme.factionsplus.config.yaml.WYIdentifier;

import org.bukkit.ChatColor;


@SuppressWarnings({ "all" }) 
public class CO_Duplicate extends COMetadata {
	
	public static final String				commentPrefixForDUPs	= "DUPLICATE #";
	public static final ChatColor			colorLineNumOnDuplicate	= ChatColor.RED;
	public static final ChatColor			colorOnDuplicate		= ChatColor.YELLOW;
	
	
	/**
	 * the duplicate of the active wid
	 */
	public final WYIdentifier<COMetadata>	appliesToWID;
	
	public final String					thePassedDottedFormatForThisWID;
	
	public final WYIdentifier<COMetadata>	theActiveFirstWID;
	
	
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
	
	
	
}
