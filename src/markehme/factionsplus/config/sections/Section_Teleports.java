package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public final class Section_Teleports {
	
	@Option(oldAliases_alwaysDotted={
//		"Teleports.disallowTeleportingToEnemyLandViaHomeCommand"//newest
		"teleports.disallowTeleportingToEnemyLandViaHomeCommand"//newer even
		,"homesintegration.disallowTeleportingToEnemyLandViaHomeCommand"//newer
		,"disallowTeleportingToEnemyLandViaHomeCommand"//very old one
//		,""//for tests
		}, realAlias_inNonDottedFormat = "disallowTeleportingToEnemyLandViaHomeCommand" )
	public  final _boolean disallowTeleportingToEnemyLandViaHomeCommand=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
//		"Teleports.reportSuccessfulByCommandTeleportsIntoEnemyLand"//newest
		"teleports.reportSuccessfulByCommandTeleportsIntoEnemyLand"//newer even
		,"homesintegration.reportSuccessfulByCommandTeleportsIntoEnemyLand"//newer
		,"reportSuccessfulByCommandTeleportsIntoEnemyLand"//very old one
		}, realAlias_inNonDottedFormat = "reportSuccessfulByCommandTeleportsIntoEnemyLand" )
	public  final _boolean reportSuccessfulByCommandTeleportsIntoEnemyLand=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"teleports.disallowTeleportingToEnemyLandViaEnderPeals"//old
		}, realAlias_inNonDottedFormat = "disallowTeleportingToEnemyLandViaEnderPeals" )
	public  final _boolean disallowTeleportingToEnemyLandViaEnderPeals=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		}, realAlias_inNonDottedFormat = "disallowTeleportingToWarZoneViaEnderPeals" )
	public  final _boolean disallowTeleportingToWarZoneViaEnderPeals=new _boolean(true);
	
	
	@Option(realAlias_inNonDottedFormat = "disallowTeleportingToSafeZoneViaEnderPeals" )
	public  final _boolean disallowTeleportingToSafeZoneViaEnderPeals=new _boolean(true);

}
