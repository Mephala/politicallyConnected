package service.provider.common.exception;

public class MultipleUserWithSameEmailAndPassword extends AbstractServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6896063290913580771L;

	public MultipleUserWithSameEmailAndPassword(String email, String password) {
		super("Multiple users found with same email and password. Email:" + email + ", Password:" + password);

	}

}
