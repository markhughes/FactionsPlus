package markehme.factionsplus.extras;


public class BailingOutException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public BailingOutException( Throwable cause ) {
		super(cause);
	}
	
	public BailingOutException( String message, Throwable cause) {
		super(message, cause);
	}
}
