package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;



public class SubSection_TPIntoEnemy {
	
	@Section(
			realAlias_neverDotted = "deny" )
	public final SubSection_TeleportsIntoEnemyDeny		_deny	= new SubSection_TeleportsIntoEnemyDeny();
	
	
	
	@Section(
			realAlias_neverDotted = "reportOnConsole" )
	public final SubSection_TeleportsIntoEnemyReport	_report	= new SubSection_TeleportsIntoEnemyReport();
	
	
	
	public boolean isAnySet() {
		return _deny.isAnySet() || _report.isAnySet();
	}
	
	
	
	public boolean shouldReportCommands() {
		return _report.shouldReportCommands();
	}
	
	
	
	public boolean shouldPreventHomeTelepors() {
		return _deny.shouldPreventHomeTelepors();
	}



	public boolean shouldPreventEnderPearlsTeleports() {
		return _deny.shouldPreventEnderPearlsTeleports();
	}
	
}
