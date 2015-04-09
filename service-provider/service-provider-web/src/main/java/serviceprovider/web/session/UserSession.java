package serviceprovider.web.session;

import serviceprovider.web.authentication.AuthenticationInformation;
import serviceprovider.web.authentication.AuthenticationLevel;

public interface UserSession {

	public AuthenticationInformation getAuthenticationInformation();

	public UniqueUserSessionData getUserUniqueSessionData();

	public void markUserAuthenticated(AuthenticationLevel authLevel);

	public void incrementFailedPasswordAttempt();

}
