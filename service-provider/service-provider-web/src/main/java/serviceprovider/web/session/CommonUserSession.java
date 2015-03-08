package serviceprovider.web.session;

import serviceprovider.web.authentication.AuthenticationInformation;
import serviceprovider.web.authentication.AuthenticationLevel;

class CommonUserSession implements UserSession {

	private CommonUserAuthenticationInformation userAuthInformation;
	private final UniqueUserSessionData uniqueSessionData;

	protected static CommonUserSession createCommonUserSession(String requestUri, UniqueUserSessionData userData) {
		return new CommonUserSession(requestUri, userData);
	}

	private CommonUserSession(String requestUri, UniqueUserSessionData userData) {
		this.userAuthInformation = CommonUserAuthenticationInformation.createCommonAuthenticationInformation(requestUri, userData);
		this.uniqueSessionData = userData;
	}

	@Override
	public AuthenticationInformation getAuthenticationInformation() {
		return userAuthInformation;
	}

	@Override
	public void markUserAuthenticated(AuthenticationLevel authLevel) {
		userAuthInformation.setAuthenticationLevel(authLevel);
	}

	@Override
	public UniqueUserSessionData getUserUniqueSessionData() {
		return uniqueSessionData;
	}

	public void changeRequestUri(String requestUri) {
		userAuthInformation.setRequestUri(requestUri);
	}

	@Override
	public void incrementFailedPasswordAttempt() {
		userAuthInformation.incrementFailedPasswordAttempt();
	}
}
