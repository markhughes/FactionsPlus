package markehme.factionsplus.listeners;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.mcore.ps.PS;


// Warning: REALLY high-resource use check that prevents lava flow between different faction owned territories (say good bye to cobble monsters)
//			probably best to only be used on servers that can handle it. 

public class LiquidFlowListener implements Listener {
		
	@EventHandler(priority=EventPriority.HIGHEST)
	public static void onBlockFromTo(BlockFromToEvent event) {
		
		Chunk fromchunk 	= event.getBlock().getChunk();
		Chunk tochunk 		= event.getToBlock().getChunk();
		
		if( (!((event.getBlock().getTypeId() == 10 ) || ( event.getBlock().getTypeId() == 11 ) ) )
				|| fromchunk == tochunk ) {
			return;
		}
		
		PS fromLoc 			= 		PS.valueOf(event.getBlock().getLocation());
		PS toLoc 			= 		PS.valueOf(event.getToBlock().getLocation());
			
		if( BoardColls.get().getFactionAt( fromLoc ) != BoardColls.get().getFactionAt( toLoc ) ) {
			event.setCancelled(true);
			
			// Replace border with cobblestone to prevent major lagging
			event.getBlock().setType(Material.COBBLESTONE);
		}
	}
}
