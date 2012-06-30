package markehme.factionsplus.config;

import markehme.factionsplus.extras.*;


/**
 * holds a tuple of non-nulls, where .equals() is applied only on the first one<br>
 * 
 * @param <FIRST>
 * @param <SECOND>
 */
public final class DualPack<FIRST, SECOND> {
	private final FIRST firstObject;
	private final SECOND secondObject;
	
	public DualPack(FIRST first,SECOND second){
		Q.nn( first );
		Q.nn( second );
		firstObject=first;
		secondObject=second;
	}
	
	@Override
	public boolean equals( Object obj ) {
		if ((null == obj)||(!(obj instanceof DualPack) )) {
			return false;
		}
		return firstObject.equals( ((DualPack)obj).firstObject);
	}

	@Override
	public int hashCode() {
		return firstObject.hashCode();
	}

	public SECOND getSecond() {
		return secondObject;
	}
}
