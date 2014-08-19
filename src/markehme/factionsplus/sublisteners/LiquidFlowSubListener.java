package markehme.factionsplus.sublisteners;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.event.block.BlockFromToEvent;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.UConf;
import com.massivecraft.massivecore.ps.PS;

public class LiquidFlowSubListener {
	
	public BlockFromToEvent blockFromToEvent(BlockFromToEvent event, Boolean createCobble) {
		Chunk fromchunk = event.getBlock().getChunk();
		Chunk tochunk = event.getToBlock().getChunk();
		
		if(fromchunk == tochunk) return event;
		
		if(!event.getBlock().getType().equals(Material.WATER) || !event.getBlock().getType().equals(Material.LAVA)) {
			return event;
		}
		
		PS fromLoc	= PS.valueOf(event.getBlock().getLocation());
		PS toLoc	= PS.valueOf(event.getToBlock().getLocation());
		
		// If it's not a Faction, let it go
		if(BoardColls.get().getFactionAt(toLoc).getId().equals(UConf.get(toLoc).factionIdNone)) {
			return event;
		}
		
		// Check if the Factions match 
		if(BoardColls.get().getFactionAt(fromLoc) != BoardColls.get().getFactionAt(toLoc)) {
			
			event.setCancelled(true);
			
			if(createCobble) event.getBlock().setType(Material.COBBLESTONE);
		}
		
		return event;
	}
}
