package markehme.factionsplus.util;

import java.io.File;
import java.nio.charset.Charset;


public abstract class Q {

	public static final Charset	UTF8 = Charset.forName("UTF-8");
	
	public final static RuntimeException ni() {
		throw new RuntimeException("not implemented");
	}
	
	public final static boolean isInconsistencyFileBug(){
		//it's this bug(which is NOT fixed yet June 2012) http://bugs.sun.com/view_bug.do?bug_id=4117557
		//to test this just do this before calling: System.setProperty("user.dir", "c:\\invalid\\folder");
		File theRealCurrentFolder = new File(".");
		File theSpecifiedFolder=new File(theRealCurrentFolder.getAbsolutePath());
		if (!theRealCurrentFolder.exists()) {
			//wait, the current folder doesn't exist?(an no SecurityException was thrown?since we're here) what trickery is this?
			//cannot really check for this bug unless .exists() returns true
			throw new SecurityException("current folder doesn't exist ?!");
		  }
		  return !theSpecifiedFolder.exists();
		  //Note: the current folder cannot really be changed, well maybe via native (JNI?)
	}


	public final static RuntimeException rethrow( Throwable t ) {
		throw new RethrownException(t);
	}
	
	
	public final static boolean nn( Object nonNullObject ) {
		if ( null == nonNullObject ) {
			throw new RuntimeException( "the expected non-null object was null" );
		} else {
			return true;
		}
	}
	
	
	public final static boolean assumedTrue( boolean expectedTrueExpression ) {
		if ( !expectedTrueExpression ) {
			throw new RuntimeException( "the expected true expression was false" );
		} else {
			return true;
		}
	}
}
