package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public class Section_PowerBoosts {
	
	@Option(oldAliases_alwaysDotted={
		"powerboosts.enablePowerBoosts"
		,"enablePowerBoosts"
		}, realAlias_inNonDottedFormat = "enabled" )
	public _boolean enabled=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerWhenKillPlayer"
		}, realAlias_inNonDottedFormat = "extraPowerWhenKillPlayer" )
	public _double extraPowerWhenKillPlayer=new _double( 0.0d ); // wow it's _double in Factions, how odd, I'd think it'd be int
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathBySuicide"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathBySuicide" )
	public _double extraPowerLossIfDeathBySuicide=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByPVP"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByPVP" )
	public _double extraPowerLossIfDeathByPVP=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByMob"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByMob" )
	public _double extraPowerLossIfDeathByMob=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByCactus"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByCactus" )
	public _double extraPowerLossIfDeathByCactus=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByTNT"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByTNT" )
	public _double extraPowerLossIfDeathByTNT=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByFire"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByFire" )
	public _double extraPowerLossIfDeathByFire=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByPotion"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByPotion" )
	public _double extraPowerLossIfDeathByPotion=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByOther"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByOther" )
	public _double extraPowerLossIfDeathByOther=new _double( 0.0d );
	
	
	
}
