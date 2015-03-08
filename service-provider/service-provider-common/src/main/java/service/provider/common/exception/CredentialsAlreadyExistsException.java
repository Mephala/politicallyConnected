package service.provider.common.exception;

public class CredentialsAlreadyExistsException extends AbstractServiceException {

	public CredentialsAlreadyExistsException() {
		super("User with same login criteria already exists in the database.");
	}
}
