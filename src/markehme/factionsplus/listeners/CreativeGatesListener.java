package markehme.factionsplus.listeners;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.FactionsPlusPlugin;
import markehme.factionsplus.config.Config;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;

import com.griefcraft.model.Protection;
import com.massivecraft.creativegates.CreativeGates;
import com.massivecraft.creativegates.MainListener;
import com.massivecraft.creativegates.Perm;
import com.massivecraft.creativegates.entity.UConf;
import com.massivecraft.creativegates.entity.UGate;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.FactionsEventChunkChange;
import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.util.MUtil;
import com.massivecraft.mcore.util.Txt;

public class CreativeGatesListener implements Listener {
	
	public static boolean isCreativeGatesIntegrated = false;
	public static CreativeGatesListener creativegateslistener;

	public static final void enableOrDisable(FactionsPlus instance) {
 		PluginManager pm = Bukkit.getServer().getPluginManager();
			
		boolean isMVPplugin = pm.isPluginEnabled("CreativeGates");
		
		if ( isMVPplugin && !isCreativeGatesIntegrated ) {
			assert ( null == creativegateslistener );
			
			creativegateslistener = new CreativeGatesListener();
			pm.registerEvents( creativegateslistener, instance );
			
			if (null == creativegateslistener) {
				creativegateslistener = new CreativeGatesListener();
				Bukkit.getServer().getPluginManager().registerEvents(creativegateslistener, instance);
			}
			
			FactionsPlusPlugin.info( "Hooked into CreativeGates." );
		}	
	}

	/*
	 * Ok, so what're we're trying to do here is replicate the event that is done inside
	 * CreativeGates - with a higher priority, but change it so it cancels if required.
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void useGateCheck(PlayerMoveEvent event) {
		
		// BEGIN: Original code 
		
		// If a player is moving from one block to another ...
		if (MUtil.isSameBlock(event)) return;
		
		// ... and there is a gate in the new block ...
		UGate ugate = UGate.get(event.getTo());
		if (ugate == null) return;

		// ... and if the gate is intact ...
		if (!ugate.isIntact())
		{
			// We try to detect that a gate was destroyed once it happens by listening to a few events.
			// However there will always be cases we miss and by checking at use we catch those as well.
			// Examples could be map corruption or use of WorldEdit.
			ugate.destroy();
			return;
		}

		// ... and gates are enabled here ...
		UConf uconf = UConf.get(event.getTo());
		if (!uconf.isEnabled()) return;

		// ... and we have permission to use gates ...
		Player player = event.getPlayer();
		if (!Perm.USE.has(player, true)) return;

		// ... and the gate has enter enabled ...
		if (!ugate.isEnterEnabled())
		{
			String message = Txt.parse("<i>This gate has enter disabled.");
			player.sendMessage(message);
			return;
		}

		// ... and the player is alive ...
		if (player.isDead()) return;
		
		// END: original code
		// XXX: Do NOT transport the player, leave it to the other plugin still! 
		
		if(!Config._extras._CreativeGates.allowUsingCreativeGatesInEnemyTerritory._) {
			if(BoardColls.get().getFactionAt(PS.valueOf(player.getLocation())).getRelationTo(UPlayer.get(player).getFaction()).equals(Rel.ENEMY) ) {
				// XXX: Maybe better to push the player a bit back too? 
				event.setCancelled(true);
			}
		}
	}
	
	/**
	 * This attempts to remove any CreativeGates in the chunk
	 * @param event
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true )
	public void onLandClaim( FactionsEventChunkChange event ) {
		
		// Confirm this is still enabled 
		if(!Config._extras._CreativeGates.destroyCreativeGatesOnClaimUnclaim._) {
			return;
		}
		// Grab the bukkit chunk 
		Chunk chunk = event.getChunk().asBukkitChunk(); 
		
		// Here begins the loop
		for ( int x = 0; x < 16; x++ ) {
			for ( int z = 0; z < 16; z++ ) {
				for ( int y = 0; y < 256; y++ ) {
					
					// Fetch the block
					Block block = chunk.getBlock( x, y, z );
					
					// Ignore air blocks (too many of them)
					if(block.getType() == Material.AIR) {
						continue; 
					}
					
					// Confirm there is a gate nearby 
					if(MainListener.isGateNearby(block)) {
						// Destroy the gate nearby
						MainListener.destroyGate(block);
					}
				}
			}
		}
	}	
}
