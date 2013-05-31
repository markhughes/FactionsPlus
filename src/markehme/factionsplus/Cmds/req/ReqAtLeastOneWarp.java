package markehme.factionsplus.Cmds.req;

import java.io.File;

import markehme.factionsplus.config.Config;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.mcore.cmd.MCommand;
import com.massivecraft.mcore.cmd.req.ReqAbstract;

/**
 * ReqAtLeastOneWarp isn't actually applicable in commands as all commands 
 * can sepcify alternative Factions that aren't of the command sender. So
 * this is just here as a test, and reference. 
 * 
 * @author markhughes
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
		File currentWarpFile = new File( Config.folderWarps, Faction.get(sender).getId() );
		
	    if ( ! currentWarpFile.exists() ) {
	    	return false;
	    } else {
	    	return true;
	    }
	}

	@Override
	public String createErrorMessage(CommandSender sender, MCommand command) {
		return "Your Faction has no warps!";
	}

}