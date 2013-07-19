package markehme.factionsplus.Cmds;


import markehme.factionsplus.FactionsPlusJail;
import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;

import com.massivecraft.factions.EconomyParticipator;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.UConf;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;

public class CmdSetJail extends FCommand {
	public CmdSetJail() {
		this.aliases.add("setjail");
		
		this.addRequirements( ReqFactionsEnabled.get() );
		this.addRequirements( ReqIsPlayer.get() );
		this.addRequirements( ReqHasFaction.get() );
		
		this.setHelp( "sets the Faction's jail" );
		this.setDesc( "sets the Faction's jail" );
	}
	
	@Override
	public void perform() {
		FactionsPlusJail.setJail(Utilities.getOnlinePlayerExact(usender));
	}
	
}
