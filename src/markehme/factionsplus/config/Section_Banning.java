package markehme.factionsplus.config;


public class Section_Banning {
	
	@ConfigOption(oldAliases={
		"banning.enableBans"
		,"enableBans"
	})
	public boolean enabled=true;
	
	
	@ConfigOption(oldAliases={
		"leaderCanNotBeBanned"
	})
	public boolean leaderCanNotBeBanned=true;
	
	
	@ConfigOption(oldAliases={
		"leadersCanFactionBan"
	})
	public boolean leadersCanFactionBan=true;
	
	
	@ConfigOption(oldAliases={
		"officersCanFactionBan"
	})
	public boolean officersCanFactionBan=true;
	
	
	@ConfigOption(oldAliases={
		"leadersCanFactionUnban"
	})
	public boolean leadersCanFactionUnban=true;
	
	
	@ConfigOption(oldAliases={
		"officersCanFactionUnban"
	})
	public boolean officersCanFactionUnban=true;
}
