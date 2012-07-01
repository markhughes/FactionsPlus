package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public final class Section_PowerBoosts {
	
	@Option(oldAliases_alwaysDotted={
		"powerboosts.enablePowerBoosts"
		,"enablePowerBoosts"
		}, realAlias_inNonDottedFormat = "enabled" )
	public static final _boolean enabled=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerWhenKillPlayer"
		}, realAlias_inNonDottedFormat = "extraPowerWhenKillPlayer" )
	public static final _double extraPowerWhenKillPlayer=new _double( 0.0d ); // wow it's _double in Factions, how odd, I'd think it'd be int
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathBySuicide"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathBySuicide" )
	public static final _double extraPowerLossIfDeathBySuicide=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByPVP"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByPVP" )
	public static final _double extraPowerLossIfDeathByPVP=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByMob"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByMob" )
	public static final _double extraPowerLossIfDeathByMob=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByCactus"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByCactus" )
	public static final _double extraPowerLossIfDeathByCactus=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByTNT"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByTNT" )
	public static final _double extraPowerLossIfDeathByTNT=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByFire"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByFire" )
	public static final _double extraPowerLossIfDeathByFire=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByPotion"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByPotion" )
	public static final _double extraPowerLossIfDeathByPotion=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByOther"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByOther" )
	public static final _double extraPowerLossIfDeathByOther=new _double( 0.0d );
	
	
	
}
