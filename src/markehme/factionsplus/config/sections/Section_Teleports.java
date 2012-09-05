package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.Section;


public final class Section_Teleports {
	
	@Section(realAlias_neverDotted="intoTerritory")
	public final SubSection_TeleportsInto _into=new SubSection_TeleportsInto();
	
	
	public final boolean isAnySet() {
		return _into.isAnySet();
	}
	
	public final boolean shouldReportCommands() {
		return _into.shouldReportCommands();
	}

	public final boolean shouldPreventHomeTelepors() {
		return _into.shouldPreventHomeTelepors();
	}
	
	public final boolean shouldPreventBackTelepors() {
		return _into.shouldPreventBackTelepors();
	}

	public final boolean shouldPreventEnderPearlsTeleports() {
		return _into.shouldPreventEnderPearlsTeleports();
	}
}
