package me.markeh.factionsplus.wildernesschunks.integrations;

import me.markeh.factionsplus.FactionsPlus;
import me.markeh.factionsplus.util.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;

import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.PlayerSession;
import uk.co.oliwali.HawkEye.Rollback.RollbackType;
import uk.co.oliwali.HawkEye.SearchParser;
import uk.co.oliwali.HawkEye.callbacks.RollbackCallback;
import uk.co.oliwali.HawkEye.database.SearchQuery;
import uk.co.oliwali.HawkEye.database.SearchQuery.SearchDir;

public class HawkEyeRegegenerator extends Regenerator {

	@Override
	public void regenerateChunk(Chunk chunk) {
		FactionsPlus.get().getServer().getScheduler().runTaskAsynchronously(FactionsPlus.get(), new Runnable() {
			public void run() {
				SearchParser sp = new SearchParser(Bukkit.getConsoleSender());
				
				sp.loc = null;
				
				sp.maxLoc = Utils.get().getChunkMaxVector(chunk);
				sp.minLoc = Utils.get().getChunkMinVector(chunk);
				
				for (DataType type : DataType.values())
					if (type.canRollback()) sp.actions.add(type);
		
				new SearchQuery(new RollbackCallback(new PlayerSession(Bukkit.getConsoleSender()), RollbackType.GLOBAL), sp, SearchDir.DESC);
				
			}
		});
	}
}
