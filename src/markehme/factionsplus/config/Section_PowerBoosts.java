package markehme.factionsplus.config;


public class Section_PowerBoosts {
	
	@ConfigOption(oldAliases={
		"powerboosts.enablePowerBoosts"
		,"enablePowerBoosts"
		})
	public boolean enabled=true;
	
	
	@ConfigOption(oldAliases={
		"extraPowerWhenKillPlayer"
		})
	public double extraPowerWhenKillPlayer=0.0d; // wow it's double in Factions, how odd, I'd think it'd be int
	
	
	@ConfigOption(oldAliases={
		"extraPowerLossIfDeathBySuicide"
		})
	public double extraPowerLossIfDeathBySuicide=0.0d;
	
	
	@ConfigOption(oldAliases={
		"extraPowerLossIfDeathByPVP"
		})
	public double extraPowerLossIfDeathByPVP=0.0d;
	
	
	@ConfigOption(oldAliases={
		"extraPowerLossIfDeathByMob"
		})
	public double extraPowerLossIfDeathByMob=0.0d;
	
	
	@ConfigOption(oldAliases={
		"extraPowerLossIfDeathByCactus"
		})
	public double extraPowerLossIfDeathByCactus=0.0d;
	
	
	@ConfigOption(oldAliases={
		"extraPowerLossIfDeathByTNT"
		})
	public double extraPowerLossIfDeathByTNT=0.0d;
	
	
	@ConfigOption(oldAliases={
		"extraPowerLossIfDeathByFire"
		})
	public double extraPowerLossIfDeathByFire=0.0d;
	
	
	@ConfigOption(oldAliases={
		"extraPowerLossIfDeathByPotion"
		})
	public double extraPowerLossIfDeathByPotion=0.0d;
	
	
	@ConfigOption(oldAliases={
		"extraPowerLossIfDeathByOther"
		})
	public double extraPowerLossIfDeathByOther=0.0d;
	
	
	
}
