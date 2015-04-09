package service.provider.common.exception;

public class InvalidRequestException extends AbstractServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4219179703565640967L;

	public InvalidRequestException() {
		super("Invalid request call detected.");
	}

}
