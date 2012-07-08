package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class SubSection_TeleportsIntoEnemyDeny {
	@Option(oldAliases_alwaysDotted={
		"Teleports.disallowTeleportingToEnemyLandViaHomeCommand"//newest
		,"teleports.disallowTeleportingToEnemyLandViaHomeCommand"//newer even
		,"homesintegration.disallowTeleportingToEnemyLandViaHomeCommand"//newer
		,"disallowTeleportingToEnemyLandViaHomeCommand"//very old one
//		,""//for tests
		}, realAlias_inNonDottedFormat = "viaHomeCommand" )
	public  final _boolean viaHome=new _boolean(true);
	
	@Option(oldAliases_alwaysDotted={
		}, realAlias_inNonDottedFormat = "viaBackCommand" )
	public  final _boolean viaBack=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"Teleports.disallowTeleportingToEnemyLandViaEnderPeals"
		,"teleports.disallowTeleportingToEnemyLandViaEnderPeals"//old
		}, realAlias_inNonDottedFormat = "viaEnderPeals" )
	public  final _boolean viaPearls=new _boolean(true);


	public boolean isAnySet() {
		return viaHome._ || viaPearls._ || viaBack._;
	}


	public boolean shouldPreventHomeTelepors() {
		return viaHome._;
	}

	public boolean shouldPreventBackTelepors() {
		return viaBack._;
	}

	public boolean shouldPreventEnderPearlsTeleports() {
		return viaPearls._;
	}
}
