package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class SubSection_TeleportsInto {
	
	@Section(realAlias_neverDotted="Ally")
	public final SubSection_TPIntoAlly _allyTerritory=new SubSection_TPIntoAlly();
	
	
	@Section(realAlias_neverDotted="Enemy")
	public final SubSection_TPIntoEnemy _enemyTerritory=new SubSection_TPIntoEnemy();
	
	
	@Section(realAlias_neverDotted="Neutral")
	public final SubSection_TPIntoNeutral _neutralTerritory=new SubSection_TPIntoNeutral();
	
	
	@Section(realAlias_neverDotted="SafeZone")
	public final SubSection_TPIntoSafeZone _safezone=new SubSection_TPIntoSafeZone();
	
	
	@Section(realAlias_neverDotted="WarZone")
	public final SubSection_TPIntoWarZone _warzone=new SubSection_TPIntoWarZone();


	public boolean isAnySet() {
		return _allyTerritory.isAnySet() || 
				_enemyTerritory.isAnySet() ||
				_neutralTerritory.isAnySet() ||
				_safezone.isAnySet() ||
				_warzone.isAnySet();
	}


	public boolean shouldReportCommands() {
		return _allyTerritory.shouldReportCommands() ||
				_enemyTerritory.shouldReportCommands() ||
				_neutralTerritory.shouldReportCommands();
	}


	public boolean shouldPreventHomeTelepors() {
		return _allyTerritory.shouldPreventHomeTelepors()
				||_enemyTerritory.shouldPreventHomeTelepors() || _neutralTerritory.shouldPreventHomeTelepors();
	}


	public boolean shouldPreventEnderPearlsTeleports() {
		return _allyTerritory.shouldPreventEnderPearlsTeleports()
				||_enemyTerritory.shouldPreventEnderPearlsTeleports() || _neutralTerritory.shouldPreventEnderPearlsTeleports()
				||_safezone.shouldPreventEnderPearlsTeleports()
				||_warzone.shouldPreventEnderPearlsTeleports();
	}
	

}
