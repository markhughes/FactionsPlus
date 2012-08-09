package markehme.factionsplus.config.yaml;


/**
 * the comment should contain the comment char, but not the leading spaces before it<br>
 */
public class WYComment<METADATA_TYPE> extends WYRawButLeveledLine<METADATA_TYPE> {

	/**
	 * @param lineNumber
	 * @param theCommentWithoutLeadingSpaces but including the "#" comment char
	 */
	public WYComment(int lineNumber, String theCommentWithoutLeadingSpaces){
		super(lineNumber, theCommentWithoutLeadingSpaces);
	}
	
}
