package serviceprovider.web.authentication;

class SimpleAuthenticationResult implements AuthenticationResult {

	protected static final SimpleAuthenticationResult SUCCESS_AUTHENTICATION_RESULT = new SimpleAuthenticationResult(false, null);
	private final boolean redirectNeeded;
	private final String redirectUri;

	@Override
	public boolean redirectNeeded() {
		return redirectNeeded;
	}

	@Override
	public String redirectUri() {
		return redirectUri;
	}

	protected SimpleAuthenticationResult(boolean redirectNeeded, String redirectUri) {
		super();
		this.redirectNeeded = redirectNeeded;
		this.redirectUri = redirectUri;
	}

}
