package me.markeh.factionsplus.wildernesschunks.integrations;

import me.botsko.prism.Prism;
import me.botsko.prism.actionlibs.ActionsQuery;
import me.botsko.prism.actionlibs.QueryParameters;
import me.botsko.prism.actionlibs.QueryResult;
import me.botsko.prism.appliers.ApplierCallback;
import me.botsko.prism.appliers.ApplierResult;
import me.botsko.prism.appliers.PrismProcessType;
import me.botsko.prism.appliers.Rollback;
import me.markeh.factionsplus.FactionsPlus;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

import com.helion3.prism.libs.elixr.ChunkUtils;

public class PrismRegenerator extends Regenerator {

	private Prism prism;
	
	public PrismRegenerator() {
		prism = (Prism) FactionsPlus.get().getServer().getPluginManager().getPlugin("Prism");
	}
	
	@Override
	public void regenerateChunk(Chunk chunk) {
		FactionsPlus.get().getServer().getScheduler().runTaskAsynchronously(FactionsPlus.get(), new Runnable() {
			public void run() {
				// Prepare to query for roll back
				QueryParameters parameters = new QueryParameters();
				parameters.setProcessType(PrismProcessType.ROLLBACK);
				
				// Major breakage events 
				parameters.addActionType("block-break");
				parameters.addActionType("block-place");
				parameters.addActionType("block-burn");
				parameters.addActionType("block-fall");
				
				// Confine to this chunk in this world 
				parameters.setWorld(chunk.getWorld().getName());
				parameters.setMinLocation(ChunkUtils.getChunkMinVector(chunk));
				parameters.setMaxLocation(ChunkUtils.getChunkMaxVector(chunk));
				
				ActionsQuery aq = new ActionsQuery(prism);
				QueryResult lookupResult = aq.lookup(parameters);
				
				if (lookupResult.getActionResults().isEmpty()) return;
				
				Rollback rb = new Rollback(prism, Bukkit.getConsoleSender(), lookupResult.getActionResults(), parameters,
					new ApplierCallback() {
						public void handle(CommandSender sender, ApplierResult result) {
							// .. do nothing at the moment  
						}
					}
				);
					
				rb.apply();
			}
		});
	}
}
