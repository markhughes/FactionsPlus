package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class SubSection_TeleportsIntoEnemyReport {
	@Option(oldAliases_alwaysDotted={
		"Teleports.reportSuccessfulByCommandTeleportsIntoEnemyLand",//newest
		"teleports.reportSuccessfulByCommandTeleportsIntoEnemyLand"//newer even
		,"homesintegration.reportSuccessfulByCommandTeleportsIntoEnemyLand"//newer
		,"reportSuccessfulByCommandTeleportsIntoEnemyLand"//very old one
		}, realAlias_inNonDottedFormat = "ifTeleportCauseIs_Command" )
	public  final _boolean viaCommand=new _boolean(true);
																
	@Option(
		oldAliases_alwaysDotted = {}, realAlias_inNonDottedFormat = "ViaEnderPeals" )
	public final _boolean	viaPearls	= new _boolean( true );
	
	
	public boolean isAnySet() {
		return viaCommand._ || viaPearls._;
	}


	public boolean shouldReportCommands() {
		return viaCommand._;
	}
}
