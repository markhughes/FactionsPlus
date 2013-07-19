package markehme.factionsplus.Cmds;

import org.bukkit.ChatColor;

import com.massivecraft.factions.cmd.req.ReqFactionsEnabled;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.mcore.cmd.req.ReqIsPlayer;

public class CmdAddChest extends FPCommand {
	public CmdAddChest() {
		this.aliases.add( "addchest" );
		
		this.requiredArgs.add( "name" );

		this.errorOnToManyArgs = false;
		
		this.addRequirements( ReqFactionsEnabled.get() );
		this.addRequirements( ReqIsPlayer.get() );
		this.addRequirements( ReqHasFaction.get() );
		
		this.setHelp( "create a faction chest, can be set up with permissions access" );

	}

	@Override
	public void performfp() {
		String chestName = this.arg(0);
		
		if( this.arg(1) != null ) {
			if( this.arg(1) == "O" ) {
				
				String chestPermission = "O";
				
			} else if( this.arg(1) == "A" ) {
				
				String chestPermission = "A";
				
			} else if( this.arg(1).trim() == "") {
				
				String chestPermission = "A";
				
			} else {
				
				msender.msg( ChatColor.RED + "It appears that the permission you set was not O (officer and above) or A (all)." );
				
				return;
				
			}
		} else {
			String chestPermission = "A";
		}
		
	}
}
