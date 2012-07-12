package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class SubSection_TPIntoSafeZone {
	@Option(oldAliases_alwaysDotted={
		"Teleports.disallowTeleportingToSafeZoneViaEnderPeals"
	},
		realAlias_inNonDottedFormat = "denyIfViaEnderPeals" )
	public  final _boolean denyIfViaPearls=new _boolean(false);

	public final  boolean isAnySet() {
		return denyIfViaPearls._;
	}

	public  final boolean shouldPreventEnderPearlsTeleports() {
		return denyIfViaPearls._;
	}
}
