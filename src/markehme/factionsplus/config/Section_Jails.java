package markehme.factionsplus.config;


public class Section_Jails {
	
	@ConfigOption(oldAliases={
		"jails.enableJails"
		,"enableJails"
		})
	public boolean enabled=true;
	
	
	@ConfigOption(oldAliases={
//		"jails.leadersCanSetJails"
		"leadersCanSetJails"
		})
	public boolean leadersCanSetJails=true;
	
	
	@ConfigOption(oldAliases={
//		"jails.officersCanSetJails"
		"officersCanSetJails"
		})
	public boolean officersCanSetJails=true;
	
	
	@ConfigOption(oldAliases={
//		"jails.membersCanSetJails"
		"membersCanSetJails"
		})
	public boolean membersCanSetJails=false;
	
	
	@ConfigOption(oldAliases={
//		"jails.leadersCanJail"
		"leadersCanJail"
		})
	public boolean leadersCanJail=true;
	
	
	@ConfigOption(oldAliases={
//		"jails.officersCanJail"
		"officersCanJail"
		})
	public boolean officersCanJail=true;
	
	
	
}
