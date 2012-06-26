package markehme.factionsplus.extras;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.*;

import com.griefcraft.scripting.event.LWCProtectionRegisterEvent;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.event.LandClaimEvent;

import markehme.factionsplus.*;
import markehme.factionsplus.extras.LWCFunctions;



public class LWCListener implements Listener {
	
	private final static String	msg	= "internal error clearing LWC locks on land claim, inform admin to check console.";
	private static final Permission	permForAllowLWCLocksAnywhere	= new Permission( "allowLWCLocksAnywhere" );
	
	
	@EventHandler(
			priority = EventPriority.MONITOR )
	public void onLandClaim( LandClaimEvent event ) {
		if ( event.isCancelled() ) {
			return;
		} else {
			FPlayer fPlayer = event.getFPlayer();
			try {
				int removedProtections = LWCFunctions.clearLocks( event.getLocation(), fPlayer );
				if ( removedProtections > 0 ) {
					fPlayer.sendMessage( ChatColor.GOLD + "Automatically removed " + removedProtections
						+ " LWC protections in the claimed chunk." );
				}
			} catch ( Exception e ) {
				event.setCancelled( true );// disallow claim
				e.printStackTrace();
				FactionsPlus.severe( msg );
				fPlayer.sendMessage( FactionsPlus.FP_TAG_IN_LOGS + msg );
			}
		}
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onLWCProtectionRegister(LWCProtectionRegisterEvent event){
//		if(event.isCancelled()){
//			return;
//		}
		
		Player p = event.getPlayer();
		Block b = event.getBlock();
		FPlayer fp = FPlayers.i.get(p);
		FLocation floc = new FLocation(b.getLocation());
		Faction owner = Board.getFactionAt(floc);

		if (owner.isNone() || Utilities.hasPermissionOrIsOp( p, permForAllowLWCLocksAnywhere ) || owner.equals( fp.getFaction() )) {
			//allow locks if it's in wilderness, or is op or has perm, or is in own faction land
			return;//the 'if' is easier to read this way
		}else {
			event.setCancelled( true );
			fp.sendMessage( ChatColor.RED + "You can lock only within your faction or unclaimed land!" );
		}
	}
}
