package markehme.factionsplus.extras;

import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.MCore.LConf;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

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
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;


/**
 * this is a LWC specific module, to hook into lwc events
 */
public class LWCModule extends JavaModule {//to fix 
//	implements Module { this won't yet work, this will cause no methods/events to be registered unless you use JavaModule instead
		
	public LWCModule() {
	}

    @Override
    public void load(LWC lwc1) {
//        this may not even be called from LWC, if I remember right (but this may change as LWC changes)
    }

	@Override
	public void onReload(LWCReloadEvent event) {
		//do nothing
	}

	@Override
	public void onAccessRequest(LWCAccessEvent event) {
		//do nothing
	}

	@Override
	public void onDropItem(LWCDropItemEvent event) {
		//do nothing
	}

	@Override
	public void onCommand(LWCCommandEvent event) {
		//do nothing
	}

	@Override
	public void onRedstone(LWCRedstoneEvent event) {
		//do nothing
	}

	@Override
	public void onDestroyProtection(LWCProtectionDestroyEvent event) {
		//do nothing
	}

	@Override
	public void onProtectionInteract(LWCProtectionInteractEvent event) {
		if(FPUConf.get(UPlayer.get(event.getPlayer()).getUniverse()).disallowPublicAccessToLWCProtectionsInLands) {
			
			PS floc = PS.valueOf(event.getProtection().getBlock().getLocation());
			Player p = event.getPlayer();
			
			Faction owner = BoardColls.get().getFactionAt(floc);
			UPlayer fp = UPlayer.get(p);
			
			if(fp.getFaction() != owner && !FType.valueOf(owner).equals(FType.WILDERNESS)) {
				event.setResult(CANCEL);
				return;
			}
		}

	}

	@Override
	public void onBlockInteract(LWCBlockInteractEvent event) {
		//do nothing
	}

	@Override
	public void onRegisterProtection(LWCProtectionRegisterEvent event) {
		//other modules can already have this cancelled
		if (event.isCancelled()) {
			return;
		}
		
		
		Player p = event.getPlayer();
		
		Block b = event.getBlock();
		UPlayer fp = UPlayer.get(p);
		PS floc = PS.valueOf(b.getLocation());
		Faction owner = BoardColls.get().getFactionAt(floc);

		if(FType.valueOf(owner).equals(FType.WILDERNESS) || FPPerm.DONTPREVENTLWCLOCKING.has(p)|| p.isOp() || owner.equals(fp.getFaction())) {
			return;
		} else {
			event.setCancelled(true);
			fp.sendMessage(Txt.parse(LConf.get().LWCCantLockHere));
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
