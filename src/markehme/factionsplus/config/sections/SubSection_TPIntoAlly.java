package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;



public class SubSection_TPIntoAlly {
	
	
	@Section(realAlias_neverDotted="deny")
	public final SubSection_TeleportsIntoAllyDeny _deny=new SubSection_TeleportsIntoAllyDeny();
	
	
	
	@Section(realAlias_neverDotted="reportOnConsole")
	public final SubSection_TeleportsIntoAllyReport _report=new SubSection_TeleportsIntoAllyReport();



	public boolean isAnySet() {
		return _deny.isAnySet() || _report.isAnySet();
	}



	public boolean shouldReportCommands() {
		return _report.shouldReportCommands();
	}



	public boolean shouldPreventHomeTelepors() {
		return _deny.shouldPreventHomeTelepors();
	}
	
	public boolean shouldPreventBackTelepors() {
		return _deny.shouldPreventBackTelepors();
	}


	public boolean shouldPreventEnderPearlsTeleports() {
		return _deny.shouldPreventEnderPearlsTeleports();
	}
}
