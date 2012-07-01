package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class Section_PowerBoosts {
	
	@Option(oldAliases_alwaysDotted={
		"powerboosts.enablePowerBoosts"
		,"enablePowerBoosts"
		}, realAlias_inNonDottedFormat = "enabled" )
	public boolean enabled=true;
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerWhenKillPlayer"
		}, realAlias_inNonDottedFormat = "extraPowerWhenKillPlayer" )
	public double extraPowerWhenKillPlayer=0.0d; // wow it's double in Factions, how odd, I'd think it'd be int
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathBySuicide"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathBySuicide" )
	public double extraPowerLossIfDeathBySuicide=0.0d;
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByPVP"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByPVP" )
	public double extraPowerLossIfDeathByPVP=0.0d;
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByMob"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByMob" )
	public double extraPowerLossIfDeathByMob=0.0d;
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByCactus"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByCactus" )
	public double extraPowerLossIfDeathByCactus=0.0d;
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByTNT"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByTNT" )
	public double extraPowerLossIfDeathByTNT=0.0d;
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByFire"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByFire" )
	public double extraPowerLossIfDeathByFire=0.0d;
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByPotion"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByPotion" )
	public double extraPowerLossIfDeathByPotion=0.0d;
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByOther"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByOther" )
	public double extraPowerLossIfDeathByOther=0.0d;
	
	
	
}
