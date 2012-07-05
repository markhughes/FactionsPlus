package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public final class Section_Teleports {
	
	@Section(realAlias_neverDotted="intoTerritory")
	public final SubSection_TeleportsInto _into=new SubSection_TeleportsInto();
	
	
	public final boolean isAnySet() {
		return _into.isAnySet();
	}
	
	public final boolean shouldReportCommands() {
		return _into.shouldReportCommands();
	}

	public boolean shouldPreventHomeTelepors() {
		return _into.shouldPreventHomeTelepors();
	}

	public boolean shouldPreventEnderPearlsTeleports() {
		return _into.shouldPreventEnderPearlsTeleports();
	}
}
