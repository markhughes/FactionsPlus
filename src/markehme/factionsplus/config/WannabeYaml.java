package markehme.factionsplus.config;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import org.bukkit.configuration.file.*;
import org.yaml.snakeyaml.*;

import markehme.factionsplus.*;
import markehme.factionsplus.extras.*;



public abstract class WannabeYaml {
	
	public static final char	space			= ' ';
	private static final char	commentChar		= '#';
	private static final char	idEnder			= ':';
	private static final int	UNSET_INDEX		= -1;
	public static final int		maxLevels		= 128;
	public static final int		spacesPerLevel	= 2;
	public static final int		maxLevelSpaces	= maxLevels * spacesPerLevel;
	
	private enum ExpectingType {
		ID_START, IDENTIFIER, VALUESTART_OR_EOL,
	}
	
	
	public final static WYItem read( File fromFile ) throws IOException {
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
			int currentLevel = 0;// up to maxLevels
			lineNumber = 0;
			parseSection( root, br, currentLevel );
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
	
	private static int lineNumber;
	
	/**
	 * @param currentLevel
	 * @param fromFile
	 * @param destinationLList
	 * @return root
	 * @throws IOException
	 */
	private final static WYItem parseSection( WYItem parentSection, BufferedReader br, int currentLevel )
		throws IOException
	{
		
		String line;
		// WYItem currentParentSection=root;
		WYItem previousWYItem = null;
		
		nextLine:
		while ( null != ( line = br.readLine() ) ) {
			lineNumber++;
			ExpectingType expecting = ExpectingType.ID_START;
			char c;
			int pos0based = -1;// must be -1
			int idStartPos = UNSET_INDEX;
			int idEndPos = UNSET_INDEX;
			int valueStartPos = UNSET_INDEX;
			
			
			// FiXME: allow empty lines to be like comments
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
						continue inLineScan;
					} else {
						// non space encountered
						// is it comment?
						if ( c == commentChar ) {
							// the level of the comment is irrelevant, we don't check number of spaces before comments
							// add line as comment
							// previousWYItem.setNext(
							previousWYItem = new WYComment( line, parentSection, previousWYItem );
							continue nextLine;// continue scanning
						} else {
							// non-comment then it's identifier aka id
							// first make sure the level is right (aka number of spaces before the id)
							// should allow 1 space above the current level
//							currentLevel = ( pos0based ) / spacesPerLevel;
							System.out.println( "line=" + lineNumber + " pos=" + pos0based + " curlevel="
								+ currentLevel + " " + ( pos0based + 1 - ( spacesPerLevel * 1 ) ) + ">"
								+ ( currentLevel * spacesPerLevel ) );
							
							if ( pos0based + 1 - ( spacesPerLevel * 1 ) > ( currentLevel * spacesPerLevel ) ) {
								// ie. "  some"
								// "     else" //notice it's 1 char is beyond the allowed +2 chars displacement spacesPerLevel
								// == 2
								// if ( pos - 1 > currentLevelspaces ) {
								throw new RuntimeException( "you put too many spaces at line " + lineNumber
									+ " at position " + ( pos0based + 1 ) + '\n' + line );
							}// else it can be exact level or less, that's normal
							if ( ( pos0based ) % spacesPerLevel != 0 ) {
								throw new RuntimeException( "incorrect number of spaces at line " + lineNumber
									+ " at position " + ( pos0based + 1 ) + '\n' + line );
							}
							// currentLevelspaces = pos;// just in case we just went from ie. 10 spaces back to 2 or 0
							expecting = ExpectingType.IDENTIFIER;
							idStartPos = pos0based;
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
						if ( c == idEnder ) {
							idEndPos = pos0based;
							// idStartPos=UNSET_INDEX;
							expecting = ExpectingType.VALUESTART_OR_EOL;
							continue inLineScan;// also skip this char
						} else {
							// TODO: replace with specific exception
							throw new RuntimeException( "unexpected char, should be `" + idEnder + "` instead of `" + c
								+ "` at line " + lineNumber + " pos " + ( pos0based + 1 ) + '\n' + line );
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
							new WYIdentifier( "!1!" + line.substring( idStartPos, idEndPos ) + "!2!", "!3!"
								+ line.substring( pos0based ).trim() + "!4!", parentSection, previousWYItem );
						
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
			}// one line done
			
			switch ( expecting ) {
			case VALUESTART_OR_EOL:
				// if we're here, then the identifier has no value (or there are spaces after it which were ignored)
				// this means, this is a section
				previousWYItem=parseSection( new WYSection( "!5!" + line.substring( idStartPos, idEndPos ) + "!6!", parentSection,
					null ), br, currentLevel + 1 );
				// currentLevel++;
				continue nextLine;
			default:
				throw new RuntimeException( "unexpected end of line at line " + lineNumber + " pos" + ( pos0based + 1 )
					+ '\n' + line );
			}
		}// all lines done
		
		
		return parentSection;
	}// end method
}
