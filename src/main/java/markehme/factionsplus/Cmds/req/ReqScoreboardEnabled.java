package markehme.factionsplus.Cmds.req;

import markehme.factionsplus.MCore.FPUConf;

import org.bukkit.command.CommandSender;

import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqAbstract;

public class ReqScoreboardEnabled  extends ReqAbstract {
	private static final long serialVersionUID = 1L;

	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static ReqScoreboardEnabled i = new ReqScoreboardEnabled();
	public static ReqScoreboardEnabled get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public boolean apply(CommandSender sender, MassiveCommand command) {
		return FPUConf.get(UPlayer.get(sender).getUniverse()).scoreboardTopFactions || FPUConf.get(UPlayer.get(sender).getUniverse()).scoreboardMapOfFactions;
	}

	@Override
	public String createErrorMessage(CommandSender sender, MassiveCommand command) {
		return "Scoreboards are not enabled.";
	}
}