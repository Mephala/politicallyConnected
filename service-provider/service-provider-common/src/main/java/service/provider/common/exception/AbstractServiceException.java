package service.provider.common.exception;

public class AbstractServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8537921789884400341L;
	private final String message;

	public AbstractServiceException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "AbstractServiceException [message=" + message + "]";
	}

}
