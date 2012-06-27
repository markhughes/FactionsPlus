package markehme.factionsplus.extras;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.permissions.*;

import markehme.factionsplus.*;

import com.griefcraft.lwc.*;
import com.griefcraft.scripting.*;
import com.griefcraft.scripting.event.*;
import com.massivecraft.factions.*;


/**
 * this is a LWC specific module, to hook into lwc events
 */
public class LWCModule extends JavaModule {//to fix 
//	implements Module { this won't yet work, this will cause no methods/events to be registered unless you use JavaModule instead
	
	private static final Permission	permForDontPreventLWCLocking	= new Permission( "factionsplus.dontPreventLWCLocking" );
	
	private LWC lwc;

    @Override
    public void load(LWC lwc1) {
        this.lwc = lwc1;
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
		//do nothing
	}

	@Override
	public void onBlockInteract( LWCBlockInteractEvent event ) {
		//do nothing
	}

	@Override
	public void onRegisterProtection( LWCProtectionRegisterEvent event ) {
		//it cannot be already cancelled but oh well
		if (event.isCancelled()) {
			return;
		}
		
		
		Player p = event.getPlayer();//it wouldn't be null
		Block b = event.getBlock();
		FPlayer fp = FPlayers.i.get(p);
		FLocation floc = new FLocation(b.getLocation());
		Faction owner = Board.getFactionAt(floc);

//		if(!LWCFunctions.checkInTerritory(p,b)) {
		if (owner.isNone() || Utilities.hasPermissionOrIsOp( p, permForDontPreventLWCLocking ) || owner.equals( fp.getFaction() )) {
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
