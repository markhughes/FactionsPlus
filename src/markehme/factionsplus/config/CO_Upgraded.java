package markehme.factionsplus.config;

import org.bukkit.*;

import markehme.factionsplus.*;
import markehme.factionsplus.config.yaml.*;



public class CO_Upgraded extends COMetadata {
	
	private static final String	commentPrefixForUPGRADEDones	= "UPGRADED to %s at line %d #";
	
	
	
	private WYIdentifier		upgradedWID;
	private String				upgradedDotted;
	private WYIdentifier		theNewUpgrade;
	private String				theNewUpgradeDotted;
	
	
	public CO_Upgraded( WYIdentifier<COMetadata> theOld, String dottedOld, WYIdentifier newUpgradeWID, String newUpgradeDotted )
	{
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
		upgradedWID.getParent().replaceAndTransformInto_WYComment( upgradedWID,
			String.format( commentPrefixForUPGRADEDones, theNewUpgradeDotted, theNewUpgrade.getLineNumber() ) );
		
		FactionsPlus.info( "Upgraded `" + ChatColor.DARK_AQUA + upgradedDotted + ChatColor.RESET + "` of line `"
			+ ChatColor.DARK_AQUA + upgradedWID.getLineNumber() + ChatColor.RESET + "` to the new config name of `"
			+ ChatColor.GREEN + theNewUpgradeDotted + ChatColor.RESET + "` of line `" + ChatColor.GREEN
			+ theNewUpgrade.getLineNumber() + "`" );
	}
	
}
