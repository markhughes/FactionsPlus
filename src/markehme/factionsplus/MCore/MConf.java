package markehme.factionsplus.MCore;


import java.util.Map;

import com.massivecraft.mcore.store.Entity;
import com.massivecraft.mcore.util.MUtil;


public class MConf extends Entity<MConf> {
	
	/*
	 * Meta 
	 */
	protected static transient MConf i;
	
	public static MConf get() { return i; }
	
	/*
	 * Fields
	 */
	
	// Should we enable FactionsPlus at all?
	public Boolean enabled = true;
	
	// Enable update checks?
	public Boolean updateCheck = true;
	
	// Enable metrics?
	public Boolean metrics = true;
	
	// Command aliases: /f <x> -> <y>
	public Map<String, String> factionCommandAliases = MUtil.map(
			"show", "/f f",
			"power", "/f p"
		);
	
}
