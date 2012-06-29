package markehme.factionsplus.config;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import markehme.factionsplus.extras.*;



public abstract class WannabeYaml {
	
	private static final char	space		= ' ';
	private static final char	commentChar	= '#';
	private static final char	idEnder		= ':';
	private static final int	UNSET_INDEX	= -1;
	
	private enum ExpectingType {
		ID_START, IDENTIFIER, VALUESTART_OR_EOL,
	}
	
	
	public final static void read( File fromFile, LinkedList<WYItem> destinationLList) throws IOException {
		if ( ( !fromFile.exists() ) || ( fromFile.isDirectory() ) ) {
			throw new FileNotFoundException();
		}
		assert Q.nn(destinationLList);
		
		FileInputStream fis = null;
		BufferedReader br = null;
		try {
			fis = new FileInputStream( fromFile );
			br = new BufferedReader( new InputStreamReader( fis, Q.UTF8 ) );
			int currentLevelspaces = 0; // meaning expecting 0 spaces at first, can't have 1 or more
			String line;
			int lineNumber = 0;
			
			nextLine:
			while ( null != ( line = br.readLine() ) ) {
				lineNumber++;
				ExpectingType expecting = ExpectingType.ID_START;
				char c;
				int pos = 0;
				int idStartPos = UNSET_INDEX;
				int idEndPos = UNSET_INDEX;
				int valueStartPos = UNSET_INDEX;
				
				inLineScan:
				while ( pos++ < line.length() ) {// 0 first time
					c = line.charAt( pos );
					switch ( expecting ) {
					case ID_START:
						assert Q.assumedTrue( idStartPos == UNSET_INDEX );
						if ( c == space ) {
							// skip all spaces until you meet non-space
							continue;
						} else {
							// non space encountered
							// is it comment?
							if ( c == commentChar ) {
								// the level of the comment is irrelevant, we don't check number of spaces before comments
								// add line as comment
								destinationLList.addLast( new WYComment( line ) );
								continue nextLine;// continue scanning
							} else {
								// non-comment then it's identifier aka id
								// first make sure the level is right (aka number of spaces before the id)
								// should allow 1 space above the current level
								if ( pos - 1 > currentLevelspaces ) {
									throw new RuntimeException( "you put too many spaces at line " + lineNumber
										+ " at position " + pos + " in file " + fromFile.getAbsolutePath() + '\n'
										+ line );
								}// else it can be exact level or less, that's normal
								currentLevelspaces = pos;// just in case we just went from ie. 10 spaces back to 2 or 0
								expecting = ExpectingType.IDENTIFIER;
								idStartPos = pos;
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
								idEndPos = pos;
								// idStartPos=UNSET_INDEX;
								expecting = ExpectingType.VALUESTART_OR_EOL;
								continue inLineScan;// also skip this char
							} else {
								// TODO: replace with specific exception
								throw new RuntimeException( "unexpected char, should be `" + idEnder + "` instead of `"
									+ c + "` at line " + lineNumber + " pos " + pos + '\n' + line );
							}
						}
						break;
					case VALUESTART_OR_EOL:
						assert Q.assumedTrue( idStartPos == UNSET_INDEX );
						if ( c != space ) {
							// by now we know it's an identifier of key=value and not a Section
							// valueStartPos=pos;
							// expecting=ExpectingType.VALUE_CONTENTS;
							destinationLList.addLast( new WYIdentifier( "!1!" + line.substring( idStartPos, idEndPos ) + "!2!",
								line.substring( pos ).trim() ) );
							
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
					destinationLList.addLast( new WYSection( "!3!" + line.substring( idStartPos, idEndPos ) + "!4!" ) );
					currentLevelspaces++;
					continue nextLine;
				default:
					throw new RuntimeException( "unexpected end of line at line " + lineNumber + " pos" + pos + '\n'
						+ line );
				}
			}// all lines done
			
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
		}//try/finally
	}//end method
}
