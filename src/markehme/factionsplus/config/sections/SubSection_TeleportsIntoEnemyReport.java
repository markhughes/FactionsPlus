package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class SubSection_TeleportsIntoEnemyReport {
	@Option(comment={
		"When true this will report on server Console all commands that were used and were successful"
		,"in teleporting players inside enemy territory. Enemy is relative to the specific player that"
		," teleported and evaluated at the time of the teleport"
		,"This will not be triggered if you have set to true the option to deny teleports into enemy territory (0.4.8+)"
		,"This may be inaccurate if the command had a warm-up delay or caused the teleport event after a delay, mainly due to"
		,"the ability of the player to insert commands inbetween and our inability to associate the command that caused the teleport"
		,"with the teleport event itself"
	}
		,oldAliases_alwaysDotted={
		"Teleports.reportSuccessfulByCommandTeleportsIntoEnemyLand",//newest
		"teleports.reportSuccessfulByCommandTeleportsIntoEnemyLand"//newer even
		,"homesintegration.reportSuccessfulByCommandTeleportsIntoEnemyLand"//newer
		,"reportSuccessfulByCommandTeleportsIntoEnemyLand"//very old one
		}, realAlias_inNonDottedFormat = "ifTeleportCauseIs_Command" )
	public  final _boolean viaCommand=new _boolean(true);
																
	@Option(comment={
		"Reports on server Console when a teleport via ender pearls caused the player to land inside enemy territory"
		,"This should be accurate."
	}
		,oldAliases_alwaysDotted = {}, realAlias_inNonDottedFormat = "ViaEnderPeals" )
	public final _boolean	viaPearls	= new _boolean( true );
	
	
	public final  boolean isAnySet() {
		return viaCommand._ || viaPearls._;
	}


	public final  boolean shouldReportCommands() {
		return viaCommand._;
	}
}
