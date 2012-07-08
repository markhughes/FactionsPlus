package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class SubSection_TPIntoWarZone {
	@Option(oldAliases_alwaysDotted={
		"Teleports.disallowTeleportingToWarZoneViaEnderPeals"
		}, realAlias_inNonDottedFormat = "denyIfViaEnderPeals" )
	public  final _boolean denyIfViaEnderPeals=new _boolean(false);

	public boolean isAnySet() {
		return denyIfViaEnderPeals._;
	}

	public boolean shouldPreventEnderPearlsTeleports() {
		return denyIfViaEnderPeals._;
	}
}
