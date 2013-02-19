package markehme.factionsplus.extras;

import markehme.factionsplus.Utilities;
import markehme.factionsplus.config.Config;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import com.griefcraft.lwc.LWC;
import com.griefcraft.scripting.JavaModule;
import com.griefcraft.scripting.event.LWCAccessEvent;
import com.griefcraft.scripting.event.LWCBlockInteractEvent;
import com.griefcraft.scripting.event.LWCCommandEvent;
import com.griefcraft.scripting.event.LWCDropItemEvent;
import com.griefcraft.scripting.event.LWCProtectionDestroyEvent;
import com.griefcraft.scripting.event.LWCProtectionInteractEvent;
import com.griefcraft.scripting.event.LWCProtectionRegisterEvent;
import com.griefcraft.scripting.event.LWCProtectionRegistrationPostEvent;
import com.griefcraft.scripting.event.LWCProtectionRemovePostEvent;
import com.griefcraft.scripting.event.LWCRedstoneEvent;
import com.griefcraft.scripting.event.LWCReloadEvent;
import com.griefcraft.scripting.event.LWCSendLocaleEvent;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;


/**
 * this is a LWC specific module, to hook into lwc events
 */
public class LWCModule extends JavaModule {//to fix 
//	implements Module { this won't yet work, this will cause no methods/events to be registered unless you use JavaModule instead
	
	private static final Permission	permForDontPreventLWCLocking	= new Permission( "factionsplus.dontPreventLWCLocking" );
	
	public LWCModule() {
	}

    @Override
    public void load(LWC lwc1) {
//        this may not even be called from LWC, if I remember right (but this may change as LWC changes)
    }

	@Override
	public void onReload( LWCReloadEvent event ) {
		//do nothing
	}

	@Override
	public void onAccessRequest( LWCAccessEvent event ) {
		//do nothing
	}

	@Override
	public void onDropItem( LWCDropItemEvent event ) {
		//do nothing
	}

	@Override
	public void onCommand( LWCCommandEvent event ) {
		//do nothing
	}

	@Override
	public void onRedstone( LWCRedstoneEvent event ) {
		//do nothing
	}

	@Override
	public void onDestroyProtection( LWCProtectionDestroyEvent event ) {
		//do nothing
	}

	@Override
	public void onProtectionInteract( LWCProtectionInteractEvent event ) {
		if(Config._extras._protection._lwc.blockCPublicAccessOnNonOwnFactionTerritory._ ) {
			FLocation floc = new FLocation(event.getProtection().getBlock().getLocation());
			Player p = event.getPlayer();
			Faction owner = Board.getFactionAt(floc);
			FPlayer fp = FPlayers.i.get(p);
			if(fp.getFaction() != owner && !Utilities.isWilderness(owner)) {
				event.setResult(CANCEL);
				return;
			}
		}

	}

	@Override
	public void onBlockInteract( LWCBlockInteractEvent event ) {
		//do nothing
	}

	@Override
	public void onRegisterProtection( LWCProtectionRegisterEvent event ) {
		//other modules can already have this cancelled
		if (event.isCancelled()) {
			return;
		}
		
		
		Player p = event.getPlayer();//it wouldn't be null
		Block b = event.getBlock();
		FPlayer fp = FPlayers.i.get(p);
		FLocation floc = new FLocation(b.getLocation());
		Faction owner = Board.getFactionAt(floc);

//		if(!LWCFunctions.checkInTerritory(p,b)) {
		if (Utilities.isWilderness(owner) || Utilities.hasPermissionOrIsOp( p, permForDontPreventLWCLocking ) || owner.equals( fp.getFaction() )) {
			//allow locks if it's in wilderness, or is op or has perm, or is in own faction land
			return;//the 'if' is easier to read this way
		}else {
			event.setCancelled( true );
			fp.sendMessage( ChatColor.RED + "You can lock only within your faction or unclaimed land!" );
		}
	}

	@Override
	public void onPostRegistration( LWCProtectionRegistrationPostEvent event ) {
		//do nothing
	}

	@Override
	public void onPostRemoval( LWCProtectionRemovePostEvent event ) {
		//do nothing
	}

	@Override
	public void onSendLocale( LWCSendLocaleEvent event ) {
		//do nothing
	}

	
	
}
