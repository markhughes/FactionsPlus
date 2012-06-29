package markehme.factionsplus.config;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import org.bukkit.configuration.file.*;
import org.yaml.snakeyaml.*;

import markehme.factionsplus.*;
import markehme.factionsplus.extras.*;



public abstract class WannabeYaml {
	
	public static final char	space				= ' ';
	private static final char	commentChar			= '#';
	public static final char	IDVALUE_SEPARATOR	= ':';
	private static final int	UNSET_INDEX			= -1;
	private static final int	END_OF_FILE			= -1;
	public static final int		maxLevels			= 128;
	public static final int		spacesPerLevel		= 2;
	public static final int		maxLevelSpaces		= maxLevels * spacesPerLevel;
	
	private enum ExpectingType {
		ID_START, IDENTIFIER, VALUESTART_OR_EOL,
	}
	
	
	public final static WYSection read( File fromFile ) throws IOException {
		assert Q.nn( fromFile );
		if ( ( !fromFile.exists() ) || ( fromFile.isDirectory() ) ) {
			throw new FileNotFoundException();
		}
		
		FileInputStream fis = null;
		BufferedReader br = null;
		try {
			fis = new FileInputStream( fromFile );
			br = new BufferedReader( new InputStreamReader( fis, Q.UTF8 ) );
			WYSection root = new WYSection( "root", null, null );
			// int currentLevelspaces = 0; // meaning expecting 0 spaces at first, can't have 1 or more
			synchronized ( WannabeYaml.class ) {
				int currentLevel = 0;// up to maxLevels
				lineNumber = 0;
				WYItem previousWYItem = null;
				int eof = parseSection( root, br, currentLevel, previousWYItem );
				assert END_OF_FILE == eof;
			}
			return root;
		} catch ( FileNotFoundException e ) {
			Q.rethrow( e );
		} finally {
			if ( null != br ) {
				try {
					br.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
			if ( null != fis ) {
				try {
					fis.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
			if ( null != br ) {
				try {
					br.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
		}// try/finally
		
		throw null;// not reached
	}
	
	private static int				lineNumber;
	private static int				idStartPos;
	private static ExpectingType	expecting;
	private static String			line;
	
	
	/**
	 * @param currentLevel
	 * @param previousWYItem
	 * @param destination
	 * @param fromFile
	 * @param destinationLList
	 * @return new level (which decreased), or it's just end of file aka -1
	 * @throws IOException
	 */
	private final static int parseSection( WYSection parentSection, BufferedReader br, int currentLevel,
		WYItem previousWYItem) throws IOException
	{
		
		// WYItem currentParentSection=root;
		
		
		nextLine:
		while ( null != ( line = br.readLine() ) ) {
			lineNumber++;
			char c;
			int pos0based = -1;// must be -1
			idStartPos = UNSET_INDEX;
			expecting = ExpectingType.ID_START;
			
			expectID:
			do {
				
				int idEndPos = UNSET_INDEX;
				int valueStartPos = UNSET_INDEX;
				
				
				// this will auto skip empty (no spaces) lines
				inLineScan:
				while ( pos0based++ < line.length() - 1 ) {// 0 first time
				
					c = line.charAt( pos0based );
					switch ( expecting ) {
					case ID_START:
						assert Q.assumedTrue( idStartPos == UNSET_INDEX );
						assert Q.assumedTrue( idEndPos == UNSET_INDEX );
						assert Q.assumedTrue( valueStartPos == UNSET_INDEX );
						
						if ( c == space ) {
							if ( pos0based > maxLevelSpaces ) {
								throw new RuntimeException( "you've reached beyond max allowed nesting" );
							}
							// skip all spaces until you meet non-space
							// also skips lines having only spaces
							continue inLineScan;
						} else {
							// non space encountered
							// is it comment?
							if ( c == commentChar ) {
								// the level of the comment is irrelevant, we don't check number of spaces before comments
								// add line as comment
								// previousWYItem.setNext
								
								// FIXME: if comment is at same level => store with previous, else don't
								previousWYItem = new WYComment( line, parentSection, previousWYItem );
//								destination.addLast( previousWYItem );
								continue nextLine;// continue scanning
							} else {
								// non-comment then it's identifier aka id
								// first make sure the level is right (aka number of spaces before the id)
								// should allow 1 space above the current level
								// now we know the level we're on
								// currentLevel = ( pos0based ) / spacesPerLevel;
								
								// Level means number of horizontal spaces from start of line until beginning of identifier
								expecting = ExpectingType.IDENTIFIER;// this is what we found starting now, and expecting it
																		// next
								idStartPos = pos0based;// marking the position of the start of the identifier
								// ie. "  identifier: value" (in file)
								// the above will be passed to prev caller, when section ends
								
								// System.out.println( "line=" + lineNumber + " pos=" + pos0based + " curlevel="
								// + currentLevel + " nowLevel=" + ( (double)pos0based / (double)spacesPerLevel ) + " "
								// + ( pos0based + 1 - ( spacesPerLevel * 1 ) ) + ">" + ( currentLevel * spacesPerLevel )
								// + '\n' + line );
								if ( ( pos0based ) % spacesPerLevel != 0 ) {
									throw new RuntimeException( "incorrect number of spaces at line " + lineNumber
										+ " at position " + ( pos0based + 1 ) + '\n' + line );
								}
								
								int theNewEncounteredLevelNow = ( pos0based / spacesPerLevel ); // this will be integer by now
								// if ( pos0based + 1 - ( spacesPerLevel * 1 ) > ( currentLevel * spacesPerLevel ) ) {
								// we could be at same level, lower or higher by 1
								if ( theNewEncounteredLevelNow > currentLevel + 1 ) {
									// "  some"
									// "     else" //notice it's 1 char is beyond the allowed +2 chars displacement
									// spacesPerLevel
									// == 2
									// if ( pos - 1 > currentLevelspaces ) {
									throw new RuntimeException( "you put too many spaces at line " + lineNumber
										+ " at position " + ( pos0based + 1 ) + " expected "
										+ ( currentLevel * spacesPerLevel ) + " or " + ( currentLevel + 1 )
										* spacesPerLevel + " spaces\n" + line );
								} else {// else it can be exact level or less, that's normal
									if ( theNewEncounteredLevelNow < currentLevel ) {
										// currentLevel = theNewEncounteredLevelNow;
										// must relinquish control to previous caller, ie. this section ended
										return theNewEncounteredLevelNow;// just in case we just went from ie. 10 spaces back to
																			// 2
																			// or 0
										
										// you go back to the section caller
									}
									// else i can be same or next level
									assert ( currentLevel == theNewEncounteredLevelNow )
										|| ( currentLevel + 1 == theNewEncounteredLevelNow );
								}
								
								// same level identifier ? then fallthru
								
							}
						}
						//$FALL-THROUGH$
					case IDENTIFIER:
						// ok get identifier
						// parse until ":" or non alphanumeric char
						if ( ( ( c >= 'a' ) && ( c <= 'z' ) ) || ( ( c >= 'A' ) && ( c <= 'Z' ) )
							|| ( ( c >= '0' ) && ( c <= '9' ) ) || ( c == '_' ) )
						{
							// ok valid id char
							// we don't actually do anything //FIXME: fix 'if'
						} else {
							// expecting ":" not space and not eol
							if ( c == IDVALUE_SEPARATOR ) {
								idEndPos = pos0based;
								// idStartPos=UNSET_INDEX;
								expecting = ExpectingType.VALUESTART_OR_EOL;
								continue inLineScan;// also skip this char
							} else {
								// TODO: replace with specific exception
								throw new RuntimeException( "unexpected char, should be `" + IDVALUE_SEPARATOR
									+ "` instead of `" + c + "` at line " + lineNumber + " pos " + ( pos0based + 1 )
									+ '\n' + line );
							}
						}
						break;
					case VALUESTART_OR_EOL:
						assert Q.assumedTrue( idStartPos != UNSET_INDEX );
						assert Q.assumedTrue( idEndPos != UNSET_INDEX );
						assert Q.nn( parentSection );
						if ( c != space ) {
							// by now we know it's an identifier of key=value and not a Section
							// valueStartPos=pos;
							// expecting=ExpectingType.VALUE_CONTENTS;
							previousWYItem =
								new WYIdentifier( line.substring( idStartPos, idEndPos ), line.substring( pos0based )
									.trim(), parentSection, previousWYItem );
//							destination.addLast( previousWYItem );
							// assert Q.nn( currentIdentifier );
							// currentIdentifier.setValue();
							continue nextLine;
						}// else it's space, we eat all spaces between "id:" and "value" or even if "value" doesn't exist here
						break;
					// case VALUE_CONTENTS:
					// assert Q.assumedTrue(valueStartPos != UNSET_INDEX);
					//
					// break;
					default:
						throw new RuntimeException( "invalid expecting type " + expecting );
					}
				}//'while' one line done
				
				//outside of the inline 'while'
				switch ( expecting ) {
				case VALUESTART_OR_EOL:
					assert Q.assumedTrue( idStartPos != UNSET_INDEX );
					assert Q.assumedTrue( idEndPos != UNSET_INDEX );
					// if we're here, then the identifier has no value (or there are spaces after it which were ignored)
					// this means, this is a section
					WYSection tmpSection = new WYSection( line.substring( idStartPos, idEndPos ), parentSection, null );
					// Q.nn(null);
//					destination.addLast( tmpSection );
					int actualLevelNow = parseSection( tmpSection, br, currentLevel + 1, null );
					
					
					if ( END_OF_FILE == actualLevelNow ) {// TODO: merge these 2 ifs in one, allowed now for clarity
						break nextLine;
					} else {
						if ( actualLevelNow < currentLevel ) {
							// i'm further below my currentLevel
							// means that I should fall back exit method to allow previous levels to carry on
							return actualLevelNow;
						}
						assert actualLevelNow == currentLevel;// can't really return anything higher, cause that would be
																// considered
																// in the same section that we exited from
					}
					// if you're here, the level then decreased, or end of file
					// but if level decreased you don't want to link next WYItem to the prev one
					
					// but you will link next item to the section here (not to the last encountered item within that section)
					previousWYItem = tmpSection;// this section is the prev item at this level
					expecting = ExpectingType.IDENTIFIER;
					// the prev for the next item in the same level is this section, after we're done with it
					// but we don't yet know what level we're one since the prev section finished
					
					// currentLevel++;
					pos0based = idStartPos;// currentLevel*spacesPerLevel;//aka to the first non-space char from ie. "  here:"
					continue expectID;// inLineScan;
					// we're at:
					// "      whateverhigher: true"
					// "    some: false"
					// _____^ <- there
					// and we cannot skip this line
				case ID_START:
					// done: allow empty lines with 0 or more spaces(limited to {@Link maxLevelSpaces} to be like comments (maybe later)
					// if we're here, we bumped into an empty line
					// we currently just ignore it, so it will not be added upon writing the config backs
					previousWYItem=new WYRawLine(line, parentSection, previousWYItem);
					continue nextLine;
				default:
					throw new RuntimeException( "unexpected end of line at line " + lineNumber + " pos"
						+ ( pos0based + 1 ) + '\n' + line );
				}
				// throw null;
			} while ( true );// evil goto hack, FIXME: try avoiding this, obviously
		}// all lines done aka EOF
		
		return END_OF_FILE;// end of file (aka end of all lines)
	}// end method
}
