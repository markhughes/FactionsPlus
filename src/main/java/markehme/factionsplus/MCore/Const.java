package markehme.factionsplus.MCore;

/**
 * Const class is what (appears) to define the folders in MStore 
 */
public class Const {
	// Our Basename
	public static final String BASENAME = "factionsplus";
	public static final String BASENAME_ = BASENAME+"_";
	
	// Where we store our Faction data
	public static final String COLLECTION_FACTION = BASENAME_+"faction";
	
	// Where we store our Universe Configuration 
	public static final String COLLECTION_UCONF = BASENAME_+"uconf";
	
	// Where we store our MConf
	public static final String COLLECTION_MCONF = BASENAME_+"mconf";
	
	// Where we store our Language strings
	public static final String COLLECTION_LCONF = BASENAME_+"lconf";

	// The main of reference ASPECT -> BASENAME confuses me. 
	// Why don't we just refernce to BASENAME, instead of
	// increasing more static references..
	public static final String ASPECT = BASENAME;

}
