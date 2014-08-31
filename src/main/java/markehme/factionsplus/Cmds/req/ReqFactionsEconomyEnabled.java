package markehme.factionsplus.Cmds.req;

import org.bukkit.command.CommandSender;

import com.massivecraft.factions.entity.UConf;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqAbstract;

public class ReqFactionsEconomyEnabled extends ReqAbstract {
	private static final long serialVersionUID = 1L;

	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static ReqFactionsEconomyEnabled i = new ReqFactionsEconomyEnabled();
	public static ReqFactionsEconomyEnabled get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public boolean apply(CommandSender sender, MassiveCommand command) {
		return UConf.get(UPlayer.get(sender).getUniverse()).econEnabled;
	}

	@Override
	public String createErrorMessage(CommandSender sender, MassiveCommand command) {
		return "Factions economy is not enabled.";
	}
}
