package markehme.factionsplus.listeners;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
//Warning: REALLY high-resource use check that prevents water/lava flow between different faction owned territories (say good bye to cobble monsters)
public class LiquidFlowListener implements Listener{
	@EventHandler(priority=EventPriority.HIGHEST)
	public static void onBlockFromTo(BlockFromToEvent event) {
		Chunk fromchunk = event.getBlock().getChunk();
		Chunk tochunk = event.getToBlock().getChunk();
		if(fromchunk==tochunk) {
			return;
		}
		FLocation fromLoc = new FLocation(event.getBlock().getLocation());
		FLocation toLoc = new FLocation(event.getToBlock().getLocation());
	if(Board.getFactionAt(fromLoc) != Board.getFactionAt(toLoc)) {
		event.setCancelled(true);
		//Attempt to remove the source block to prevent lag:
		//Water and lava only flow down, and not up.
	}
		
	}

}
