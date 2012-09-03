package markehme.factionsplus.listeners;

import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;

import com.massivecraft.factions.*;
//Warning: REALLY high-resource use check that prevents lava flow between different faction owned territories (say good bye to cobble monsters)
public class LiquidFlowListener implements Listener{
	@EventHandler(priority=EventPriority.HIGHEST)
	public static void onBlockFromTo(BlockFromToEvent event) {
		Chunk fromchunk = event.getBlock().getChunk();
		Chunk tochunk = event.getToBlock().getChunk();
		if(!((event.getBlock().getTypeId() == 10)||(event.getBlock().getTypeId() == 11))) {
			return;
		}
		if(fromchunk==tochunk) {
			return;
		}
		FLocation fromLoc = new FLocation(event.getBlock().getLocation());
		FLocation toLoc = new FLocation(event.getToBlock().getLocation());
	if(Board.getFactionAt(fromLoc) != Board.getFactionAt(toLoc)) {
		event.setCancelled(true);
		//Replace border with cobblestone to prevent major lagging
		event.getBlock().setType(Material.COBBLESTONE);
		
	}
		
	}

}
