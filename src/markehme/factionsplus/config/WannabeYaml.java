package markehme.factionsplus.config;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import markehme.factionsplus.extras.*;


public abstract class WannabeYaml {
	private static final char space=' ';
	private static final char commentChar='#';
//	private static final char[]	validIdentifierRange	= {	};
//	private static boolean sortedVIR=false;
	
	private enum ExpectingType {
		LINE_START, IDENTIFIER,
	}
	
	public final static void read(File fromFile, Class destinationClass) throws IOException {
		if ((!fromFile.exists())|| (fromFile.isDirectory())){
			throw new FileNotFoundException();
		}
		LinkedList llist=new LinkedList<WYItem>();
//		if (!sortedVIR) {
//			Arrays.sort( validIdentifierRange );
//			sortedVIR=true;
//		}
		BufferedReader in = null;
		try {
			in=new BufferedReader( new FileReader( fromFile) );
			int level=0;
			String line;
			int lineNumber=0;
			while (null != (line=in.readLine()) ) {
				lineNumber++;
				ExpectingType expecting = ExpectingType.LINE_START;
				char c;
				int index=0;
				
				linescan:
				while (index++ < line.length()) {//0 first time 
					c=line.charAt( index );
					switch ( expecting ) {
					case LINE_START:
						if ( c == space ) {
							continue;
						} else {
							// non space encountered
							// is it comment?
							if ( c == commentChar ) {
								// the level of the comment is irrelevant, we don't check number of spaces before comments
								// add line as comment
								llist.addLast( new WYComment( line ) );
								break linescan;
							} else {
								// non-comment then it's identifier aka id
								// first make sure the level is right (aka number of spaces before the id)
								if ( index > level ) {
									throw new RuntimeException( "you put too many spaces at line " + lineNumber
										+ " in file " + fromFile.getAbsolutePath() );
								}//else it can be exact level or less, that's normal

								expecting=ExpectingType.IDENTIFIER;
//								continue linescan;
							}
						}
						//$FALL-THROUGH$
					case IDENTIFIER:
						// ok get identifier
						// parse until ":" or non alphanumeric char
						Arrays.sort( a )
						if (c in ['a'])
						break;
					default:
						throw new RuntimeException("invalid expecting type "+expecting);
					}
				}
				 
			}
		}finally{
			if (null != in) {
				try {
					in.close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
			}
		}
	}
}
