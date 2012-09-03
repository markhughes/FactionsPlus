package markehme.factionsplus.Cmds;


import markehme.factionsplus.*;
import markehme.factionsplus.config.*;

import com.massivecraft.factions.*;
import com.massivecraft.factions.cmd.*;
import com.massivecraft.factions.integration.*;
import com.massivecraft.factions.struct.*;

public class CmdSetJail extends FCommand {
	public CmdSetJail() {
		this.aliases.add("setjail");
		
		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = false;
		
		this.setHelpShort("sets the Faction's jail");
	}
	
	@Override
	public void perform() {
		FactionsPlusJail.setJail(Utilities.getOnlinePlayerExact(fme));
	}
	
	public static boolean doFinanceCrap(double cost, String toDoThis, String forDoingThis, FPlayer player) {
		if ( !Config._economy.isHooked() || ! Econ.shouldBeUsed() || Utilities.getOnlinePlayerExact( player ) == null || cost == 0.0) return true;

		if(Conf.bankEnabled && Conf.bankFactionPaysCosts && player.hasFaction())
			return Econ.modifyMoney(player.getFaction(), -cost, toDoThis, forDoingThis);
		else
			return Econ.modifyMoney(player, -cost, toDoThis, forDoingThis);
	}
}
