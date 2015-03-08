package serviceprovider.web.session;

import serviceprovider.web.authentication.AuthenticationInformation;
import serviceprovider.web.authentication.AuthenticationLevel;
import serviceprovider.web.configuration.ConfigurationManager;

class CommonUserAuthenticationInformation implements AuthenticationInformation {

	private int failedPasswordAttempts = 0;
	private String requestUri;
	private AuthenticationLevel authenticationLevel;
	private final String ip;
	private final String sessionId;

	private CommonUserAuthenticationInformation(String reqUri, final UniqueUserSessionData userSessionCredentialsData) {
		this.requestUri = reqUri;
		this.authenticationLevel = AuthenticationLevel.COMMON; // defaul auth. level
		this.ip = userSessionCredentialsData.getIp();
		this.sessionId = userSessionCredentialsData.getSessionId();
	}

	@Override
	public boolean canAuthenticate() {
		final ConfigurationManager configurationManager = ConfigurationManager.getInstance();
		return failedPasswordAttempts < configurationManager.getMaxAuthenticationPasswordKey();
	}

	@Override
	public String getRequestedUri() {
		return requestUri;
	}

	@Override
	public AuthenticationLevel getAuthenticationlevel() {
		return authenticationLevel;
	}

	public static CommonUserAuthenticationInformation createCommonAuthenticationInformation(String requestUri, UniqueUserSessionData userSessionCredentialsData) {
		return new CommonUserAuthenticationInformation(requestUri, userSessionCredentialsData);
	}

	@Override
	public String getIp() {
		return ip;
	}

	@Override
	public String getSessionId() {
		return sessionId;
	}

	protected void setAuthenticationLevel(AuthenticationLevel authenticationLevel) {
		this.authenticationLevel = authenticationLevel;
	}

	protected void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public synchronized void incrementFailedPasswordAttempt() {
		failedPasswordAttempts++;
	}
}
