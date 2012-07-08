package markehme.factionsplus.config;

import org.bukkit.*;

import markehme.factionsplus.*;
import markehme.factionsplus.config.yaml.*;



public class CO_Overridden extends COMetadata {
	
	private static final String	commentPrefixForOVERRIDDENones	= "OVERRIDDEN by %s at line %d #";
	
	
	
	private WYIdentifier		lostOne;
	private String				dottedLostOne;
	private WYIdentifier		overriddenByThis;
	private String				dottedOverriddenByThis;
	
	
	public CO_Overridden( WYIdentifier<COMetadata> wid, String dottedWID, WYIdentifier _overriddenByThis,
		String _dottedOverriddenByThis )
	{
		assert null != wid;
		assert Typeo.isValidAliasFormat( dottedWID );
		assert null != _overriddenByThis;
		assert Typeo.isValidAliasFormat( _dottedOverriddenByThis );
		lostOne = wid;
		dottedLostOne = dottedWID;
		overriddenByThis = _overriddenByThis;
		dottedOverriddenByThis = _dottedOverriddenByThis;
	}
	
	
	@Override
	protected void override_apply() {
		// WYComment<COMetadata> newItem =
		lostOne.getParent().replaceAndTransformInto_WYComment( lostOne,
			String.format( commentPrefixForOVERRIDDENones, dottedOverriddenByThis, overriddenByThis.getLineNumber() ) );
		
		Config.warn( "Config option " + ChatColor.AQUA + dottedOverriddenByThis + ChatColor.RESET + " at line "
			+ ChatColor.AQUA + overriddenByThis.getLineNumber() + ChatColor.RESET + " overrides the old alias for it `"
			+ ChatColor.DARK_AQUA + dottedLostOne + ChatColor.RESET + "` which is at line " + ChatColor.DARK_AQUA
			+ lostOne.getLineNumber() + ChatColor.RESET + " which was also transformed into comment to show it's ignored." );
		
		// return newItem;
	}
	
}
