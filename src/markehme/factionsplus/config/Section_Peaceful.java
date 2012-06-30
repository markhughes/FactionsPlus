package markehme.factionsplus.config;


public class Section_Peaceful {
	
	@ConfigOption(oldAliases={
		"leadersCanToggleState"
		})
	public boolean leadersCanToggleState=false;
	
	
	@ConfigOption(oldAliases={
		"officersCanToggleState"
		})
	public boolean officersCanToggleState=false;
	
	
	@ConfigOption(oldAliases={
		"membersCanToggleState"
		})
	public boolean membersCanToggleState=false;
	
	
	@ConfigOption(oldAliases={
		"enablePeacefulBoosts"
		})
	public boolean enablePeacefulBoosts=true;
	
	
	@ConfigOption(oldAliases={
		"powerBoostIfPeaceful"
		})
	public int powerBoostIfPeaceful=20;
	

}
