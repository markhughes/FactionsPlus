package markehme.factionsplus.config;

import java.lang.reflect.*;

import org.bukkit.*;

import markehme.factionsplus.*;
import markehme.factionsplus.config.yaml.*;



public class CO_Upgraded extends CO_FieldPointer {
	
	private static final String	commentPrefixForUPGRADEDones	= "UPGRADED to %s at line %d #";
	
	
	
	private WYIdentifier		upgradedWID;
	private String				upgradedDotted;
	private WYIdentifier		theNewUpgrade;
	private String				theNewUpgradeDotted;
	
	
	public CO_Upgraded( WYIdentifier<COMetadata> theOld, String dottedOld, Field field, WYIdentifier newUpgradeWID, String newUpgradeDotted )
	{
		super(field, newUpgradeWID);
		assert null != theOld;
		assert Typeo.isValidAliasFormat( dottedOld );
		assert null != newUpgradeWID;
		assert Typeo.isValidAliasFormat( newUpgradeDotted );
		upgradedWID = theOld;
		upgradedDotted = dottedOld;
		theNewUpgrade = newUpgradeWID;
		theNewUpgradeDotted = newUpgradeDotted;
	}
	
	
	@Override
	protected void override_apply() {
		super.override_apply();
		upgradedWID.getParent().replaceAndTransformInto_WYComment( upgradedWID,
			String.format( commentPrefixForUPGRADEDones, theNewUpgradeDotted, theNewUpgrade.getLineNumber() ) );
		
		Config.info( "Upgraded `" + ChatColor.DARK_AQUA + upgradedDotted + ChatColor.RESET + "` of line `"
			+ ChatColor.DARK_AQUA + upgradedWID.getLineNumber() + ChatColor.RESET + "` to the new config name of `"
			+ COLOR_FOR_NEW_OPTIONS_ADDED + theNewUpgradeDotted + ChatColor.RESET + "` of line `" + COLOR_FOR_NEW_OPTIONS_ADDED
			+ theNewUpgrade.getLineNumber() + "`" );
	}
	
}
