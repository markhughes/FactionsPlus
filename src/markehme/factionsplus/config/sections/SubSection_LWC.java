package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public final class SubSection_LWC {
	
//	public static final static final String	str_removeLWCLocksOnClaim	= "removeLWCLocksOnClaim";

	@Option(autoComment={
		"It is highly encouraged to have this enabled IF you have Factions' \"onCaptureResetLwcLocks\": true,"
		,"and you should then set onCaptureResetLwcLocks to false because all that one does is reset chests' locks,"
		,"which is already done by this option below & more."
		,"The following are unlocked: chests, furnaces, signs, dispensers, wooden doors, iron doors, trap doors."
		,"The locks are removed only if they are owned by someone that is not in your faction."
		,"The removal happens when you claim land ie. /f claim"
		,"If onCaptureResetLwcLocks is true (in Factions plugin) then this option will be autoset to true in memory"
	},
		oldAliases_alwaysDotted={
		"extras.lwc.removeLWCLocksOnClaim",
		"removeLWCLocksOnClaim" //the first one in this line is the latest ID for this option, and will be used when saving the config.yml
		//all below lines are used to import the from the obsolete names for this same config option, the higher ones override the lower ones
		,"extras.lwc.useLWCIntegrationFix"
//		,"removeLWCLocksOnClaim"
		,"useLWCIntegrationFix"
	}, realAlias_inNonDottedFormat = "removeAllLocksOnClaim" )
	public  final _boolean removeAllLocksOnClaim=new _boolean(false);
	
	@Option(autoComment={"will allow you to make a door truly public via /cpublic"},
		oldAliases_alwaysDotted={
		"extras.lwc.blockCPublicAccessOnNonOwnFactionTerritory"
	},
			realAlias_inNonDottedFormat = "blockCPublicAccessOnNonOwnFactionTerritory" )
	public  final _boolean blockCPublicAccessOnNonOwnFactionTerritory=new _boolean(false);
}
