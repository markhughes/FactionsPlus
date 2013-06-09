package markehme.factionsplus.Cmds;

import org.bukkit.ChatColor;

import markehme.factionsplus.FactionPlusChests;
import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.util.Q;

import com.massivecraft.factions.struct.Permission;

public class CmdAddChest extends FPCommand {
	public CmdAddChest() {
		this.aliases.add("addchest");
		this.errorOnToManyArgs = false;
		
		this.requiredArgs.add("name");
		this.optionalArgs.put("permission", "string");
		
		this.permission = Permission.HELP.node;
		this.disableOnLock = false;
		
		senderMustBePlayer = true;
		senderMustBeMember = false;
		
		this.setHelpShort("creates a virtual Faction chest");
	}
	
	@Override
	protected void performfp() {
		
		// Ensure they have permission to use this command.
		if( ! FactionsPlus.permission.has(sender, "factionsplus.managechests")) {
			msg( ChatColor.RED + "No permission!" );
			return;
		}
		
		String chestPerm = "S"; // Default is "S"
		String chestName = this.argAsString( 0 );
		
		// Check if they want open permissions 
		if( this.argAsString( 1 ) != null ) {
			
			// Ensure the correct permissions are being used
			if( this.argAsString( 1 ) != "S" && this.argAsString( 1 ) != "M") {
				msg( ChatColor.RED + "Chest not created: The only permissions allowed are S or M." );
				return;
			}
			
			permission = this.argAsString( 1 );
			
		}
		
		FactionPlusChests.createChest(fme.getFaction(), fme, chestName, permission);
		


		
	}

}
