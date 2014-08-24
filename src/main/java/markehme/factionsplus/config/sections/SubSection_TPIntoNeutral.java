package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.Section;


public class SubSection_TPIntoNeutral {
	@Section(realAlias_neverDotted="deny")
	public final SubSection_TeleportsIntoNeutralDeny _deny=new SubSection_TeleportsIntoNeutralDeny();
	
	
	@Section(realAlias_neverDotted="reportOnConsole")
	public final SubSection_TeleportsIntoNeutralReport _report=new SubSection_TeleportsIntoNeutralReport();


	public final  boolean isAnySet() {
		return _deny.isAnySet() || _report.isAnySet();
	}


	public final  boolean shouldReportCommands() {
		return _report.shouldReportCommands();
	}


	public final  boolean shouldPreventHomeTelepors() {
		return _deny.shouldPreventHomeTelepors();
	}
	
	public final  boolean shouldPreventBackTelepors() {
		return _deny.shouldPreventBackTelepors();
	}


	public final  boolean shouldPreventEnderPearlsTeleports() {
		return _deny.shouldPreventEnderPearlsTeleports();
	}
	
	
}
