package markehme.factionsplus.Cmds;


public abstract class RunnableWithParams<FIRSTPARAM, STATUS> implements Runnable {

	private FIRSTPARAM storedParam;
//	private SECONDPARAM storedParam2;
	private STATUS status;
	
	public RunnableWithParams(FIRSTPARAM param) {//, SECONDPARAM param2) {
		storedParam=param;
//		storedParam2=param2;
	}
	
	@Override
	public final void run() {
		run2(storedParam);
	}

	public abstract void run2(FIRSTPARAM param1);
	
	public void setParam(FIRSTPARAM param){
		storedParam=param;
	}
	
//	public void setParam2(SECONDPARAM param2){
//		storedParam2=param2;
//	}
	
	public void setStatus(STATUS _status) {
		status=_status;
	}
	
	public STATUS getStatus() {
		return status;
	}
}
