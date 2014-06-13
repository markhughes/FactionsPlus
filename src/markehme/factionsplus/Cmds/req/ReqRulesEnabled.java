package markehme.factionsplus.Cmds.req;

import markehme.factionsplus.MCore.FPUConf;

import org.bukkit.command.CommandSender;

import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqAbstract;

public class ReqRulesEnabled extends ReqAbstract {
	private static final long serialVersionUID = 1L;

	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static ReqRulesEnabled i = new ReqRulesEnabled();
	public static ReqRulesEnabled get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public boolean apply(CommandSender sender, MCommand command) {
		return FPUConf.get(UPlayer.get(sender).getUniverse()).rulesEnabled;
	}

	@Override
	public String createErrorMessage(CommandSender sender, MCommand command) {
		return "Faction Rules are not enabled.";
	}
}
