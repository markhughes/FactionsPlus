package markehme.factionsplus.MCore;

import markehme.factionsplus.FactionsPlus;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.MStore;

public class FPUConfColl extends Coll<FPUConf> {
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public FPUConfColl(String name) {
		super(name, FPUConf.class, MStore.getDb(), FactionsPlus.instance);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void init() {
		super.init();
		this.get(MCore.INSTANCE, true);
	}

}
