package markehme.factionsplus.config;


public class RulesSection {
	
	@ConfigOption(oldAliases={
		"warps.enableRules"
		,"enableRules"
		})
	public boolean enabled=true;
	
	
	@ConfigOption(oldAliases={
		"leadersCanSetRules"
		})
	public boolean leadersCanSetRules=true;
	
	
	@ConfigOption(oldAliases={
		"officersCanSetRules"
		})
	public boolean officersCanSetRules=true;
	
	
	
}
