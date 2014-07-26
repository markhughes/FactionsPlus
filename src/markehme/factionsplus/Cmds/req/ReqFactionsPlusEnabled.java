package markehme.factionsplus.Cmds.req;

import org.bukkit.command.CommandSender;

import markehme.factionsplus.MCore.FPUConf;

import com.massivecraft.massivecore.cmd.MCommand;
import com.massivecraft.massivecore.cmd.req.ReqAbstract;

public class ReqFactionsPlusEnabled extends ReqAbstract {
	private static final long serialVersionUID = 1L;

	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static ReqFactionsPlusEnabled i = new ReqFactionsPlusEnabled();
	public static ReqFactionsPlusEnabled get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public boolean apply(CommandSender sender, MCommand command) {
		return !FPUConf.isDisabled(sender);
	}

	@Override
	public String createErrorMessage(CommandSender sender, MCommand command) {
		return FPUConf.getDisabledMessage(sender);
	}

}
