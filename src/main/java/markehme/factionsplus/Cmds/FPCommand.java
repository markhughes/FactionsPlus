package markehme.factionsplus.Cmds;

import markehme.factionsplus.FactionsPlus;
import markehme.factionsplus.MCore.FPUConf;
import markehme.factionsplus.MCore.FactionData;
import markehme.factionsplus.MCore.FactionDataColls;
import markehme.factionsplus.MCore.LConf;

import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.massivecore.util.Txt;

public abstract class FPCommand extends FCommand {

	// This method is called for FP Specific commands 
	protected abstract void performfp();
	
	// Our identifier for FP commands
	protected String fpidentifier = null;
	
	protected FactionData fData = null;
	
	protected FPUConf fpuconf = null;
	
	protected String universe = null;
	
	// Fetch the identifier for FP commands
	public final String getIdentifier() {
		return this.fpidentifier;
	}
	
	@Override
	public final void perform() {
		// if FactionsPlus is not enabled, then don't run the command
		if (!FactionsPlus.instance.isEnabled()) {
			msg(Txt.parse(LConf.get().fpNotEnabled));
			return;
		}
		
		if(usender.hasFaction()) {
			fData = FactionDataColls.get().getForUniverse(usender.getUniverse()).get(usenderFaction.getId());
			
			if(fData == null) {
				fData = FactionDataColls.get().getForUniverse(usender.getUniverse()).create(usenderFaction.getId());
			}
		}
		
		// Some helpful variables for later
		this.universe = usender.getUniverse();
		this.fpuconf = FPUConf.get(this.universe);
		
		// Perform our command 
		performfp();
	}
}
