package markehme.factionsplus.config;

import markehme.factionsplus.config.yaml.WYIdentifier;

import org.bukkit.ChatColor;



public class CO_Invalid extends COMetadata {
	
	public static final String				commentPrefixForINVALIDs	= "Invalid / Removed #";
	public static final ChatColor			colorOnINVALID				= ChatColor.YELLOW;
	
	
	public final WYIdentifier<COMetadata>	appliesToWID;
	public final String					thePassedDottedFormatForThisWID;
	
	
	public CO_Invalid( WYIdentifier<COMetadata> wid, String dotted ) {
		assert null != wid;
		assert Typeo.isValidAliasFormat( dotted );
		appliesToWID = wid;
		thePassedDottedFormatForThisWID = dotted;
	}
	
	
	
}
