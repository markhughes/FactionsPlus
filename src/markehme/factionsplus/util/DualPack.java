package markehme.factionsplus.util;

import javax.management.*;

import markehme.factionsplus.extras.*;
import markehme.factionsplus.util.*;


/**
 * holds a tuple of non-nulls, where .equals() is applied only on the first one<br>
 * 
 * @param <FIRST>
 * @param <SECOND>
 */
public final class DualPack<FIRST, SECOND> {
	private final FIRST firstObject;
	private final SECOND secondObject;
	
//	public DualPack(FIRST first) {
//		Q.nn( first );
//		firstObject=first;
//		secondObject=null;
//	}
		
	public DualPack(FIRST first,SECOND second){
		Q.nn( first );
		Q.nn( second );
		firstObject=first;
		secondObject=second;
	}
	
	@Override
	public boolean equals( Object obj ) {
		if (null == obj) {
			return false;
		}
		if (!(obj instanceof DualPack) ) {
			throw new RuntimeException("bad call, you tried to compare "+DualPack.class+" against "+obj.getClass()
				+"\nOR you mixed different objects together in a HashMap<Object,>/HashSet<Object>");
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

	public FIRST getFirst() {
		return firstObject;
	}
}
