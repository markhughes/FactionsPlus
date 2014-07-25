package markehme.factionsplus.Cmds;


import markehme.factionsplus.FactionsPlusJail;
import markehme.factionsplus.Utilities;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;

public class CmdSetJail extends FPCommand {
	public CmdSetJail() {
		this.aliases.add("setjail");
		
		this.fpidentifier = "setjail";
		
		this.addRequirements( ReqFactionsEnabled.get() );
		this.addRequirements( ReqIsPlayer.get() );
		this.addRequirements( ReqHasFaction.get() );
		
		this.setHelp( "sets the Faction's jail" );
		this.setDesc( "sets the Faction's jail" );
	}
	
	@Override
	public void performfp() {
		FactionsPlusJail.setJail(Utilities.getOnlinePlayerExact(usender));
	}
	
}
