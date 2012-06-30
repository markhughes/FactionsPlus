package markehme.factionsplus.config;


/**
 * this is practically a fully empty line without ever having had a level(aka leading spaces)<br>
 */
public class WYEmptyLine extends WYWhitespacedLine {

	public WYEmptyLine(int lineNumber, WYSection parent, WYItem prev ) {
		super( lineNumber,"", parent, prev );
	}
	
}
