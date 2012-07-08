package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class SubSection_TPIntoNeutral {
	@Section(realAlias_neverDotted="deny")
	public final SubSection_TeleportsIntoNeutralDeny _deny=new SubSection_TeleportsIntoNeutralDeny();
	
	
	@Section(realAlias_neverDotted="reportOnConsole")
	public final SubSection_TeleportsIntoNeutralReport _report=new SubSection_TeleportsIntoNeutralReport();


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
