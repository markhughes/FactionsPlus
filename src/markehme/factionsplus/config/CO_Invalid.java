package markehme.factionsplus.config;

import org.bukkit.*;

import markehme.factionsplus.*;
import markehme.factionsplus.config.yaml.*;



public class CO_Invalid extends COMetadata {
	
	public static final String				commentPrefixForINVALIDs	= "INVALID #";
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
