package markehme.factionsplus.MCore;

import markehme.factionsplus.FactionsPlus;

import com.massivecraft.mcore.ps.PS;
import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.MStore;

public class FactionDataColl extends Coll<FactionData> {
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public FactionDataColl(String name) {
		super(name, FactionData.class, MStore.getDb(), FactionsPlus.instance);
	}

	// -------------------------------------------- //
	// OVERRIDE: COLL
	// -------------------------------------------- //

	@Override
	public void init() {
		super.init();
	}

	@Override
	public FactionData get(Object oid) {
		FactionData ret = super.get(oid);

		return ret;
	}
	


}
