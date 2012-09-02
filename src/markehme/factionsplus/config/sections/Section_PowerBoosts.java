package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;


public final class Section_PowerBoosts {
	
	@Option(oldAliases_alwaysDotted={
		"powerboosts.enablePowerBoosts"
		,"enablePowerBoosts"
		}, realAlias_inNonDottedFormat = "enabled" )
	public  final _boolean enabled=new _boolean(true);
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerWhenKillPlayer"
		}, realAlias_inNonDottedFormat = "extraPowerWhenKillPlayer" )
	public  final _double extraPowerWhenKillPlayer=new _double( 0.0d ); 
	
	@Option(oldAliases_alwaysDotted={
			"extraPowerWhenKillMonster"
			}, realAlias_inNonDottedFormat = "extraPowerWhenKillMonster" )
		public  final _double extraPowerWhenKillMonster=new _double( 0.0d );
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathBySuicide"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathBySuicide" )
	public  final _double extraPowerLossIfDeathBySuicide=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByPVP"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByPVP" )
	public  final _double extraPowerLossIfDeathByPVP=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByMob"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByMob" )
	public  final _double extraPowerLossIfDeathByMob=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByCactus"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByCactus" )
	public  final _double extraPowerLossIfDeathByCactus=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByTNT"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByTNT" )
	public  final _double extraPowerLossIfDeathByTNT=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByFire"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByFire" )
	public  final _double extraPowerLossIfDeathByFire=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByPotion"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByPotion" )
	public  final _double extraPowerLossIfDeathByPotion=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"extraPowerLossIfDeathByOther"
		}, realAlias_inNonDottedFormat = "extraPowerLossIfDeathByOther" )
	public  final _double extraPowerLossIfDeathByOther=new _double( 0.0d );


	@Option(autoComment={//FIXME: maybe remove this as an option, why would it be needed? it should always act as if it's true anyway
		"-this should always be true; it shouldn't even be an option",
		"-basically if the Factions plugin does powerloss to you",
		"then FactionsPlus also applies the extra powerlosses or gains from above",
		"if this option is false, then FactionsPlus applies the extras even when Factions Plugin doesn't"
		},
			oldAliases_alwaysDotted={
			"respectFactionsWarZonePowerLossRules",
			"powerboosts.respectFactionsWarZonePowerLossRules"
			}, realAlias_inNonDottedFormat = "respectFactionsPluginPowerLossRules" )
		public  final _boolean respectFactionsPluginPowerLossRules=new _boolean(true);	
	
	
}
