package markehme.factionsplus.extras;

import java.util.*;

import markehme.factionsplus.config.*;


public final class TypedLinkedList<TYPE> {
	private LinkedList<TYPE> ll=new LinkedList<TYPE>();
	
	public int indexOf(TYPE obj){
		return ll.indexOf( obj );
	}

	public void addLast( TYPE obj ) {
		ll.addLast( obj );
	}

	public boolean contains( TYPE obj ) {
		return ll.contains( obj );
	}

	public TYPE get( int index ) {
		return ll.get( index );
	}
}
