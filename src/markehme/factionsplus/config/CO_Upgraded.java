package markehme.factionsplus.config;

import java.lang.reflect.*;

import markehme.factionsplus.config.yaml.*;



public class CO_Upgraded extends CO_FieldPointer {
	
	public static final String	commentPrefixForUPGRADEDones	= "UPGRADED to %s at line %d #";
	
	
	
	public WYIdentifier		upgradedWID;
	public String				upgradedDotted;
//	public WYIdentifier		theNewUpgrade;
	public String				theNewUpgradeDotted;
	
	
	public CO_Upgraded( WYIdentifier<COMetadata> theOld, String dottedOld, Field _field, WYIdentifier newUpgradeWID, String newUpgradeDotted )
	{
		super(_field, newUpgradeWID, true);
		assert null != theOld;
		assert Typeo.isValidAliasFormat( dottedOld );
//		assert null != newUpgradeWID;
		assert Typeo.isValidAliasFormat( newUpgradeDotted );
		upgradedWID = theOld;
		upgradedDotted = dottedOld;
//		theNewUpgrade = newUpgradeWID;
		theNewUpgradeDotted = newUpgradeDotted;
	}
	
	
	
}
