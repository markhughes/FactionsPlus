package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.Option;


public class SubSection_TPIntoWarZone {
	@Option(oldAliases_alwaysDotted={
		"Teleports.disallowTeleportingToWarZoneViaEnderPeals"
		}, realAlias_inNonDottedFormat = "denyIfViaEnderPeals" )
	public  final _boolean denyIfViaEnderPeals=new _boolean(false);

	public final  boolean isAnySet() {
		return denyIfViaEnderPeals._;
	}

	public final  boolean shouldPreventEnderPearlsTeleports() {
		return denyIfViaEnderPeals._;
	}
}
