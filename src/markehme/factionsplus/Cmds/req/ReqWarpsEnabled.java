package markehme.factionsplus.Cmds.req;

import markehme.factionsplus.MCore.UConf;

import org.bukkit.command.CommandSender;

import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqAbstract;

public class ReqWarpsEnabled extends ReqAbstract {
	private static final long serialVersionUID = 1L;

	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static ReqWarpsEnabled i = new ReqWarpsEnabled();
	public static ReqWarpsEnabled get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public boolean apply(CommandSender sender, MCommand command) {
		return !UConf.isDisabled(sender);
	}

	@Override
	public String createErrorMessage(CommandSender sender, MCommand command) {
		return "Warps are not enabled.";
	}
}
