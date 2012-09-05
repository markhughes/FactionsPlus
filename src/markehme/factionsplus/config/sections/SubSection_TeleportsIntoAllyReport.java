package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.Option;


public class SubSection_TeleportsIntoAllyReport {
	@Option(
		oldAliases_alwaysDotted = {}, realAlias_inNonDottedFormat = "ifTeleportCauseIs_Command" )
	public final _boolean	viaCommand		= new _boolean( false );	
																
	@Option(
		oldAliases_alwaysDotted = {}, realAlias_inNonDottedFormat = "ViaEnderPeals" )
	public final _boolean	viaPearls	= new _boolean( false );	

	public final  boolean isAnySet() {
		return viaCommand._ || viaPearls._;
	}

	public  final boolean shouldReportCommands() {
		return viaCommand._;
	}
}
