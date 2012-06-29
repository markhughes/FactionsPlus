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
	
	// private static final char[] validIdentifierRange = { };
	// private static boolean sortedVIR=false;
	
	private enum ExpectingType {
		LINE_START, IDENTIFIER, VALUE_START,
		//VALUE_CONTENTS,
	}
	
	
	public final static void read( File fromFile, Class destinationClass ) throws IOException {
		if ( ( !fromFile.exists() ) || ( fromFile.isDirectory() ) ) {
			throw new FileNotFoundException();
		}
		LinkedList llist = new LinkedList<WYItem>();
		// if (!sortedVIR) {
		// Arrays.sort( validIdentifierRange );
		// sortedVIR=true;
		// }
		BufferedReader in = null;
		try {
			in = new BufferedReader( new FileReader( fromFile ) );
			int currentLevelspaces = 0; // meaning expecting 0 spaces at first, can't have 1 or more
			String line;
			int lineNumber = 0;
			
			while ( null != ( line = in.readLine() ) ) {
				lineNumber++;
				ExpectingType expecting = ExpectingType.LINE_START;
				char c;
				int pos = 0;
				int idStartPos = UNSET_INDEX;
				int valueStartPos=UNSET_INDEX;
				WYIdentifier currentIdentifier = null;
				
				linescan:
				while ( pos++ < line.length() ) {// 0 first time
					c = line.charAt( pos );
					switch ( expecting ) {
					case LINE_START:
						assert Q.assumedTrue(idStartPos == UNSET_INDEX);
						if ( c == space ) {
							continue;
						} else {
							// non space encountered
							// is it comment?
							if ( c == commentChar ) {
								// the level of the comment is irrelevant, we don't check number of spaces before comments
								// add line as comment
								llist.addLast( new WYComment( line ) );
								break linescan;// auto updating to line start expect
							} else {
								// non-comment then it's identifier aka id
								// first make sure the level is right (aka number of spaces before the id)
								// should allow 1 space above the current level
								if ( pos-1 > currentLevelspaces ) {
									throw new RuntimeException( "you put too many spaces at line " + lineNumber
										+ " at position " + pos + " in file " + fromFile.getAbsolutePath()+'\n'+line );
								}// else it can be exact level or less, that's normal
								
								expecting = ExpectingType.IDENTIFIER;
								idStartPos = pos;
								// continue linescan;
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
							//we don't actually do anything //FIXME: fix 'if' 
						} else {
							// expecting ":" not space and not eol
							if ( c == idEnder ) {
								currentIdentifier=new WYIdentifier(line.substring( idStartPos, pos ));
								idStartPos=UNSET_INDEX;
								expecting=ExpectingType.VALUE_START;
								continue linescan;
							} else {
								// TODO: replace with specific exception
								throw new RuntimeException( "unexpected char, should be `" + idEnder + "` instead of `"
									+ c + "` at line " + lineNumber + " pos " + pos+'\n'+line );
							}
						}
						break;
					case VALUE_START:
						assert Q.assumedTrue(idStartPos == UNSET_INDEX);
						if (c != space) {
//							valueStartPos=pos;
//							expecting=ExpectingType.VALUE_CONTENTS;
							assert Q.nn( currentIdentifier );
							currentIdentifier.setValue(line.substring( pos ).trim());
							continue linescan;
						}//else it's space, we eat all spaces between "id:" and "value" or even if "value" doesn't exist here
						break;
//					case VALUE_CONTENTS:
//						assert Q.assumedTrue(valueStartPos != UNSET_INDEX);
//						
//						break;
					default:
						throw new RuntimeException( "invalid expecting type " + expecting );
					}
				}//one line done
				
				switch (expecting) {
				case VALUE_START:
					
					break;
				default:
					throw null;
				}
			}//all lines done
			
		} finally {
			if ( null != in ) {
				try {
					in.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
		}
	}
}
