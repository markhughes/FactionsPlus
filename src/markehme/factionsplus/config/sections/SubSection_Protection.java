package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.Option;
import markehme.factionsplus.config.Section;


public class SubSection_Protection {
	
	@Section(
			realAlias_neverDotted = "LWC" )
	public final SubSection_LWC	_lwc							= new SubSection_LWC();
	

	@Section(
		realAlias_neverDotted = "PVP" )
	public final SubSection_PVP	_pvp							= new SubSection_PVP();
	
	@Option(autoComment={"Remove Lockette's sign protection"},
			realAlias_inNonDottedFormat = "removeSignProtectionOnClaim" )
	public final _boolean		removeSignProtectionOnClaim		= new _boolean( false );

	@Option(autoComment={"Allow factions to kill ally faction mobs"},
			realAlias_inNonDottedFormat = "allowFactionKillAlliesMobs" )
	public final _boolean		allowFactionKillAlliesMobs		= new _boolean( true );

	@Option(autoComment={"Allow factions to kill netural factions mobs"},
			realAlias_inNonDottedFormat = "allowFactionKillNeutralMobs" )
	public final _boolean		allowFactionKillNeutralMobs		= new _boolean( false );
	
	@Option(autoComment={"Allow factions to kill enemy factions mobs"},
			realAlias_inNonDottedFormat = "allowFactionKillEnemyMobs" )
	public final _boolean		allowFactionKillEnemyMobs		= new _boolean( false );

	@Option(autoComment={"Protects some safe mobs in SafeZone"},
			realAlias_inNonDottedFormat = "protectSafeAnimalsInSafeZone" )
	public final _boolean		protectSafeAnimalsInSafeZone		= new _boolean( false );
	
	@Option(autoComment={"Enabling this will disallow shearing, milking, and fishing",
						 "outside your Faction land. But allows it in wilderness/factionless land."},
			realAlias_inNonDottedFormat = "stricterFarmingProtection" )
	public final _boolean		stricterFarmingProtection		= new _boolean( true );

	@Option(autoComment={"Change to false to disallow creating shops in the wilderness"},
		realAlias_inNonDottedFormat = "allowShopsInWilderness" )
	public final _boolean		allowShopsInWilderness			= new _boolean( true );
	
	
	@Option(autoComment={"Change to false to disallow creating shops in their own territory"},
		realAlias_inNonDottedFormat = "allowShopsInTerritory" )
	public final _boolean		allowShopsInTerritory			= new _boolean( true );
	
	
	@Option(autoComment={"unused"},
		realAlias_inNonDottedFormat = "allowSignProtectionInWilderness" )
	public final _boolean		allowSignProtectionInWilderness	= new _boolean( true );
	
	
	@Option(autoComment={"unused"},
		realAlias_inNonDottedFormat = "allowSignProtectionInTerritory" )
	public final _boolean		allowSignProtectionInTerritory	= new _boolean( true );
	
	@Option(autoComment={"Enabling this will only allow peaceful Faction members to create LWC protections"},
			realAlias_inNonDottedFormat = "onlyPeacefulCreateLWCProtections"
	)
	public final _boolean		onlyPeacefulCreateLWCProtections = new _boolean( false );
	
	@Option(autoComment={"Enabling this will deny any damage to a player in a SafeZone."},
			realAlias_inNonDottedFormat = "safeZonesExtraSafe"
	)
	public final _boolean		safeZonesExtraSafe = new _boolean( false );
	
	@Option(autoComment={"If enabled, when claiming this will check world guard regions for can build",
						 "they will otherwise be denied."},
			realAlias_inNonDottedFormat = "worldguardCanBuildCheck"
	)
	public final _boolean		worldguardCanBuildCheck = new _boolean( true );

}
