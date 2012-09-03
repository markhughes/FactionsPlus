package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.Option;


public class SubSection_TeleportsIntoEnemyReport {
	@Option(autoComment={
		"When true this will report(on server Console) the player that landed(via teleport caused by a command)"
		," inside enemy territory. The command that caused the teleport cannot be known, but the last command typed"
		," by the teleported player is shown as a helper. For example: another player can use /tphere to teleport that player"
		," and that player's last command will be reported(ie. /who) which is not the command that caused the teleport."
		,"Enemy is the relation between that player that teleported and the faction that owns the territory where player would've landed, and it is evaluated at the time of the teleport"
		,"This will not be triggered for /home or /back if you have set(to true) the option(s) to deny teleports into enemy territory (0.4.8+)"
	}
		,oldAliases_alwaysDotted={
		"Teleports.reportSuccessfulByCommandTeleportsIntoEnemyLand",//newest
		"teleports.reportSuccessfulByCommandTeleportsIntoEnemyLand"//newer even
		,"homesintegration.reportSuccessfulByCommandTeleportsIntoEnemyLand"//newer
		,"reportSuccessfulByCommandTeleportsIntoEnemyLand"//very old one
		}, realAlias_inNonDottedFormat = "ifTeleportCauseIs_Command" )
	public  final _boolean viaCommand=new _boolean(true);
																
	@Option(autoComment={
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
