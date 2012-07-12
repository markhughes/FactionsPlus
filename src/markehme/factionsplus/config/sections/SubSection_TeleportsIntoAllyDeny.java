package markehme.factionsplus.config.sections;

import markehme.factionsplus.config.*;



public class SubSection_TeleportsIntoAllyDeny {
	
	@Option(
			oldAliases_alwaysDotted = {}, realAlias_inNonDottedFormat = "ViaHomeCommand" )
	public final _boolean	viaHome		= new _boolean( false );	
	
	@Option(
		oldAliases_alwaysDotted = {}, realAlias_inNonDottedFormat = "ViaBackCommand" )
	public final _boolean	viaBack		= new _boolean( false );	
																	
	@Option(
			oldAliases_alwaysDotted = {}, realAlias_inNonDottedFormat = "ViaEnderPeals" )
	public final _boolean	viaPearls	= new _boolean( false );	

	public  final boolean isAnySet() {
		return viaHome._ || viaPearls._ || viaBack._;
	}

	public  final boolean shouldPreventHomeTelepors() {
		return viaHome._;
	}
	
	public  final boolean shouldPreventBackTelepors() {
		return viaBack._;
	}

	public  final boolean shouldPreventEnderPearlsTeleports() {
		return viaPearls._;
	}
}
