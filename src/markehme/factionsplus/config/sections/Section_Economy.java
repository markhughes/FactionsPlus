package markehme.factionsplus.config.sections;

import net.milkbowl.vault.economy.*;

import org.bukkit.*;
import org.bukkit.plugin.*;

import markehme.factionsplus.*;
import markehme.factionsplus.config.*;


public final class Section_Economy {
	
	@Option(oldAliases_alwaysDotted={
		"economy.enableEconomy"
		,"enableEconomy"
		,"economy_enable"
	}, realAlias_inNonDottedFormat = "enabled" )
	private final _boolean enabled=new _boolean(false);//it's private because you're supposed to use .isHooked() instead ;)
	
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToWarp"//newest
		,"economy.economycostToWarp"//newer
		,"economy_costToWarp"//very old one
		}, realAlias_inNonDottedFormat = "costToWarp" )
	public  final _double costToWarp=new _double( 0.0d );//current one (the field name)
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToCreateWarp"//newest
		,"economy.economycostToCreateWarp"//newer
		,"economy_costToCreateWarp"//very old one
		}, realAlias_inNonDottedFormat = "costToCreateWarp" )
	public  final _double costToCreateWarp=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToDeleteWarp"//newest
		,"economy.economycostToDeleteWarp"//newer
		,"economy_costToDeleteWarp"//very old one
		}, realAlias_inNonDottedFormat = "costToDeleteWarp" )
	public  final _double costToDeleteWarp=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToAnnounce"//newest
		,"economy.economycostToAnnounce"//newer
		,"economy_costToAnnounce"//very old one
		}, realAlias_inNonDottedFormat = "costToAnnounce" )
	public  final _double costToAnnounce=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToJail"//newest
		,"economy.economycostToJail"//newer
		,"economy_costToJail"//very old one
		}, realAlias_inNonDottedFormat = "costToJail" )
	public  final _double costToJail=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToSetJail"//newest
		,"economy.economycostToSetJail"//newer
		,"economy_costToSetJail"//very old one
		}, realAlias_inNonDottedFormat = "costToSetJail" )
	public  final _double costToSetJail=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToUnJail"//newest
		,"economy.economycostToUnJail"//newer
		,"economy_costToUnJail"//very old one
		}, realAlias_inNonDottedFormat = "costToUnJail" )
	public  final _double costToUnJail=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToToggleUpPeaceful"//newest
		,"economy.economycostToToggleUpPeaceful"//newer
		,"economy_costToToggleUpPeaceful"//very old one
		}, realAlias_inNonDottedFormat = "costToToggleUpPeaceful" )
	public  final _double costToToggleUpPeaceful=new _double( 0.0d );
	
	
	@Option(oldAliases_alwaysDotted={
		"economy.economyCostToToggleDownPeaceful"//newest
		,"economy.economycostToToggleDownPeaceful"//newer
		,"economy_costToToggleDownPeaceful"//very old one
		}, realAlias_inNonDottedFormat = "costToToggleDownPeaceful" )
	public  final _double costToToggleDownPeaceful=new _double( 0.0d );
	
	
	
	
	private static Economy economyInstance = null;
	
	public synchronized boolean isHooked() {
		return null != economyInstance;
	}
	
	/**
	 * @return true if it's enabled(aka existed & enabled)<br>
	 * false if it's disabled (ie. was disabled in config or was enabled by not found)
	 */
	public synchronized boolean enableOrDisableEconomy() {
		boolean wanted=Config._economy.enabled._;
		//check is the state differs from the current state
		if (wanted && !isHooked()) {
			return turnOnEconomy();
		}else if (!wanted && isHooked()) {
			turnOffEconomy();
			return false;
		}
		
		//state is the same if we're here
		FactionsPlus.info( "Economy integration is still "+(wanted?"ON":"OFF") );
		return wanted;
	}

	private synchronized void turnOffEconomy() {
		assert isHooked();
		economyInstance=null;
		FactionsPlus.info( "Economy integration is OFF" );
	}

	private synchronized boolean turnOnEconomy() {
		assert !isHooked();
		
    	RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().
    			getRegistration(net.milkbowl.vault.economy.Economy.class);
    	
    	if (economyProvider != null) {
        	economyInstance = economyProvider.getProvider();
        	//we need the message here, to say on console when economy was turned on or off, because we may call this from
        	//the /f reloadfp command too, not just from onEnable()
        	FactionsPlus.info( "Economy integration is ON" );
        	return true;
    	}else {
    		FactionsPlus.info( "Economy is not found and thus integration is DISABLED." );
    		return false;
    	}
	}
}
