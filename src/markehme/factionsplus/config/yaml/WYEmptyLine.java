package markehme.factionsplus.config.yaml;


/**
 * this is practically a fully empty line without ever having had a level(aka leading spaces)<br>
 */
public class WYEmptyLine<METADATA_TYPE> extends WYWhitespacedLine<METADATA_TYPE> {

	public WYEmptyLine(int lineNumber){//, WYSection parent, WYItem prev ) {
		super( lineNumber,"");//, parent, prev );
	}
	
}
