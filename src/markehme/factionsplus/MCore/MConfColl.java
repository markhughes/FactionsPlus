package markehme.factionsplus.MCore;

import markehme.factionsplus.FactionsPlus;

import com.massivecraft.mcore.MCore;
import com.massivecraft.mcore.store.Coll;
import com.massivecraft.mcore.store.MStore;

public class MConfColl extends Coll<MConf> {
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static MConfColl i = new MConfColl();
	public static MConfColl get() { return i; }
	private MConfColl() {
		super(Const.COLLECTION_MCONF, MConf.class, MStore.getDb(), FactionsPlus.instance);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void init() {
		super.init();
		MConf.i = this.get(MCore.INSTANCE, true);
	}

}
