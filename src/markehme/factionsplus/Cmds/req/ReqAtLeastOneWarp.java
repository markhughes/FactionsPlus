package markehme.factionsplus.Cmds.req;

import java.io.File;

import markehme.factionsplus.config.Config;

import org.bukkit.command.CommandSender;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.cmd.MassiveCommand;
import com.massivecraft.massivecore.cmd.req.ReqAbstract;

/**
 * ReqAtLeastOneWarp isn't actually applicable in commands as all commands 
 * can specify alternative Factions that aren't of the command sender. So
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
	public boolean apply(CommandSender sender, MassiveCommand command) {
		File currentWarpFile = new File( Config.folderWarps, Faction.get(sender).getId() );
		
	    if ( ! currentWarpFile.exists() ) {
	    	return false;
	    } else {
	    	return true;
	    }
	}

	@Override
	public String createErrorMessage(CommandSender sender, MassiveCommand command) {
		return "Your Faction has no warps!";
	}

}