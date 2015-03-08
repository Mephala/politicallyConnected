package service.provider.common.exception;

public class AuthenticationFailureException extends AbstractServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3861614712352614325L;

	public AuthenticationFailureException() {
		super("Wrong user credentials.");
	}

}
