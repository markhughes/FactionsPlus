package markehme.factionsplus.config;

import markehme.factionsplus.extras.*;



public class WYSection extends WY_IDBased {
	
	private WYItem firstChild,lastChild;

	
	public WYSection(int lineNumber, String id ){//, WYSection parent, WYItem prev) {
		super(lineNumber,id);//, parent, prev);
		firstChild=null;
		lastChild=null;
//		System.out.println("Section "+this+" parent="+parent+" prev="+prev);
	}
	
//	public String getSectionName(){
//		return getId();
//	}
	
	public WYItem getFirst() {
		return firstChild;
	}
	
	public boolean isEmpty() {
		return null == firstChild;
	}

	void append( WYItem child ) {
		if (lastChild != null) {//means first is also non-null
			assert lastChild.getNext() == null;
			lastChild.setNext( child );
			child.setPrev(lastChild);
			lastChild=child;
		}else {
			//was previously empty
			firstChild=lastChild=child;
			assert null == child.getPrev();
			assert null == child.getNext();
		}
		
		child.setParent(this);
	}

	public WYItem getLast() {
		return lastChild;
	}

}
