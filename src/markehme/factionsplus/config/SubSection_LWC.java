package markehme.factionsplus.config;


public class SubSection_LWC {
	
	@ConfigOption(oldAliases={
		"extras.lwc.useLWCIntegrationFix"
//		,"removeLWCLocksOnClaim"
		,"useLWCIntegrationFix"
	})
	public boolean removeLWCLocksOnClaim=false;
	
	@ConfigOption
	public boolean blockCPublicAccessOnNonOwnFactionTerritory=false;
}
