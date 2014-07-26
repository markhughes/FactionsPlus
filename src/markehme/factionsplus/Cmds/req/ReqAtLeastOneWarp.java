package markehme.factionsplus.Cmds.req;

import markehme.factionsplus.MCore.FactionData;
import markehme.factionsplus.MCore.FactionDataColls;

import org.bukkit.command.CommandSender;

import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.cmd.MCommand;
import com.massivecraft.massivecore.cmd.req.ReqAbstract;

/**
 * ReqAtLeastOneWarp isn't actually applicable in commands as all commands 
 * can specify alternative Factions that aren't of the command sender. So
 * this is just here as a test, and reference. 
 * 
 * @author MarkehMe <mark@markeh.me>
 * 
 */
public class ReqAtLeastOneWarp extends ReqAbstract {
	private static final long serialVersionUID = 1L;

	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static ReqAtLeastOneWarp i = new ReqAtLeastOneWarp();
	public static ReqAtLeastOneWarp get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public boolean apply(CommandSender sender, MCommand command) {
		FactionData fData = FactionDataColls.get().getForUniverse(UPlayer.get(sender).getUniverse()).get(UPlayer.get(sender).getFaction().getId());
		
		if(fData.warpLocation.size() > 0) {
			return true;
		}
		
		return false;
		
	}

	@Override
	public String createErrorMessage(CommandSender sender, MCommand command) {
		return "This Faction has no warps.";
	}

}