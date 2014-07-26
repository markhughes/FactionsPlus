package markehme.factionsplus.listeners;

import markehme.factionsplus.MCore.FPUConf;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.massivecore.ps.PS;


/**
 * This stops liquids flowing into other Factions, it can be intensive sometimes so
 * it should only be used on servers that can handle it 
 *
 */
public class LiquidFlowListener implements Listener {
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public static void onBlockFromTo(BlockFromToEvent event) {
		
		if(FPUConf.isDisabled(event.getBlock().getLocation())) return;
		
		Chunk fromchunk = event.getBlock().getChunk();
		Chunk tochunk = event.getToBlock().getChunk();
		
		// Ensure it is water or lava, and is not just inside the same chunk
		if((!((event.getBlock().getType() == Material.WATER) || (event.getBlock().getType() == Material.LAVA)))
				|| fromchunk == tochunk ) {
			return;
		}
		
		PS fromLoc = PS.valueOf(event.getBlock().getLocation());
		PS toLoc = PS.valueOf(event.getToBlock().getLocation());
		
		if(!FPUConf.get(fromLoc).fixes.containsKey("crossBorderLiquidFlowBlock")) return;
		if(!FPUConf.get(fromLoc).fixes.get("crossBorderLiquidFlowBlock")) return;
		
		// Check if the chunks we are switching between are different factions 
		if(BoardColls.get().getFactionAt(fromLoc) != BoardColls.get().getFactionAt(toLoc)) {
			event.setCancelled(true);
			
			// This is enabled by default to lower the chance of lag 
			if(FPUConf.get(fromLoc).fixes.get("crossBorderLiquidFlowBlock"))  {
				event.getBlock().setType(Material.COBBLESTONE);
			}
		}
	}
}
