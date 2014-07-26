package markehme.factionsplus.MCore;

import markehme.factionsplus.FactionsPlus;

import com.massivecraft.massivecore.Aspect;


public class FactionDataColls extends XColls<FactionDataColl, FactionData> {
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static FactionDataColls i = new FactionDataColls();
	public static FactionDataColls get() { return i; }

	// -------------------------------------------- //
	// OVERRIDE: COLLS
	// -------------------------------------------- //

	@Override
	public FactionDataColl createColl(String collName) {
		return new FactionDataColl(collName);
	}

	@Override
	public Aspect getAspect() {
		return FactionsPlus.instance.getAspect();
	}

	@Override
	public String getBasename() {
		return Const.COLLECTION_FACTION;
	}

	@Override
	public void init() {
		super.init();

		this.migrate();
	}
	
	public void migrate() {
		// Create file objects

	}
}
