package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.Option;


public class SubSection_TeleportsIntoEnemyDeny {
	@Option(
		autoComment={
			"If, by using /home, the player would be teleported inside enemy territory then"
			,"having this option set to `true` will prevent the teleport from ever happening"
		}
		,oldAliases_alwaysDotted={
		"Teleports.disallowTeleportingToEnemyLandViaHomeCommand"//newest
		,"teleports.disallowTeleportingToEnemyLandViaHomeCommand"//newer even
		,"homesintegration.disallowTeleportingToEnemyLandViaHomeCommand"//newer
		,"disallowTeleportingToEnemyLandViaHomeCommand"//very old one
//		,""//for tests
		}, realAlias_inNonDottedFormat = "viaHomeCommand" )
	public  final _boolean viaHome=new _boolean(true);
	
	@Option(autoComment={
		"If true, it will deny the teleporting into enemy territory"
		,"when the /back command would teleport you there"
	}
		,oldAliases_alwaysDotted={
		}, realAlias_inNonDottedFormat = "viaBackCommand" )
	public  final _boolean viaBack=new _boolean(true);
	
	
	@Option(autoComment={
		"If set to `true` this will prevent players from landing inside enemy territory by using ender pearls"
		,"the pearl is spent if failed and a message will show"
	}
		,oldAliases_alwaysDotted={
		"Teleports.disallowTeleportingToEnemyLandViaEnderPeals"
		,"teleports.disallowTeleportingToEnemyLandViaEnderPeals"//old
		}, realAlias_inNonDottedFormat = "viaEnderPeals" )
	public  final _boolean viaPearls=new _boolean(true);


	public final  boolean isAnySet() {
		return viaHome._ || viaPearls._ || viaBack._;
	}


	public  final boolean shouldPreventHomeTelepors() {
		return viaHome._;
	}

	public final  boolean shouldPreventBackTelepors() {
		return viaBack._;
	}

	public boolean shouldPreventEnderPearlsTeleports() {
		return viaPearls._;
	}
}
