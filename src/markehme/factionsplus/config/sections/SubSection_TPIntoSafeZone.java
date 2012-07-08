package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class SubSection_TPIntoSafeZone {
	@Option(oldAliases_alwaysDotted={
		"Teleports.disallowTeleportingToSafeZoneViaEnderPeals"
	},
		realAlias_inNonDottedFormat = "denyIfViaEnderPeals" )
	public  final _boolean denyIfViaPearls=new _boolean(false);

	public boolean isAnySet() {
		return denyIfViaPearls._;
	}

	public boolean shouldPreventEnderPearlsTeleports() {
		return denyIfViaPearls._;
	}
}
