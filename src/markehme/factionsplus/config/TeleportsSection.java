package markehme.factionsplus.config;


public class TeleportsSection {
	
	@ConfigOption(oldAliases={
		"Teleports.disallowTeleportingToEnemyLandViaHomeCommand"//newest
		,"teleports.disallowTeleportingToEnemyLandViaHomeCommand"//newer even
		,"homesintegration.disallowTeleportingToEnemyLandViaHomeCommand"//newer
		,"disallowTeleportingToEnemyLandViaHomeCommand"//very old one
		,""//for tests
		})
	public boolean disallowTeleportingToEnemyLandViaHomeCommand=true;
	
	
	@ConfigOption(oldAliases={
		"Teleports.reportSuccessfulByCommandTeleportsIntoEnemyLand"//newest
		,"teleports.reportSuccessfulByCommandTeleportsIntoEnemyLand"//newer even
		,"homesintegration.reportSuccessfulByCommandTeleportsIntoEnemyLand"//newer
		,"reportSuccessfulByCommandTeleportsIntoEnemyLand"//very old one
		})
	public boolean reportSuccessfulByCommandTeleportsIntoEnemyLand=true;
	
	
	@ConfigOption(oldAliases={
		"Teleports.disallowTeleportingToEnemyLandViaEnderPeals"//newest
		,"teleports.disallowTeleportingToEnemyLandViaEnderPeals"//old
		})
	public boolean disallowTeleportingToEnemyLandViaEnderPeals=true;
	
	
	
}
