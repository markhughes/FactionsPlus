package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.Option;

public class SubSection_Flight {
	
	@Option(
			realAlias_inNonDottedFormat = "allowAttackingWhileFlying" )
	public  final _boolean allowAttackingWhileFlying = new _boolean(true);
	
	@Option(
			realAlias_inNonDottedFormat = "allowSplashPotionsWhileFlying" )
	public  final _boolean allowSplashPotionsWhileFlying = new _boolean(true);
	
	
	
}
