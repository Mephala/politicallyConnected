package serviceprovider.web.authentication;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import serviceprovider.web.controller.Model;
import serviceprovider.web.form.AuthenticateUserForm;
import serviceprovider.web.session.UserSession;
import serviceprovider.web.session.UserSessionManager;

public class AuthenticationManager {

	private static AuthenticationManager instance;
	private List<AuthenticationRule> rules;
	private UserSessionManager userSessionManager;

	private AuthenticationManager() {
		rules = AuthenticationRules.getAuthenticationRules();
		Collections.sort(rules, new AuthenticationRulePriorityComparator());
		userSessionManager = UserSessionManager.getInstance();
	}

	public static synchronized AuthenticationManager getInstance() {
		if (instance == null)
			instance = new AuthenticationManager();
		return instance;
	}

	public AuthenticationResult authenticate(AuthenticationInformation authInfo) throws AuthenticationException {
		AuthenticationResult result = null;
		for (AuthenticationRule rule : rules) {
			result = rule.authenticate(authInfo);
			if (result.redirectNeeded())
				break;
		}
		return result;
	}

	public AuthenticationResult authenticate(UserSession userSession) throws AuthenticationException {
		return authenticate(userSession.getAuthenticationInformation());
	}

	public AuthenticationResult authenticate(HttpServletRequest request, AuthenticateUserForm authenticaUserForm) {
		// FIXME Hardcoded.
		UserSession suser = userSessionManager.getUserSession(request);
		if ("1234567890".equals(authenticaUserForm.getUsername()) && "0987654321".equals(authenticaUserForm.getPassword())) {
			suser.markUserAuthenticated(AuthenticationLevel.ADMIN);
			return SimpleAuthenticationResult.SUCCESS_AUTHENTICATION_RESULT;
		} else {
			suser.incrementFailedPasswordAttempt();
			return new SimpleAuthenticationResult(true, Model.MAIN_AUTHENTICATION_PAGE.getUri());
		}
	}
}
