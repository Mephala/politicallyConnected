package serviceprovider.web.authentication;

import java.util.Set;

import serviceprovider.web.configuration.ConfigurationManager;

class CommonUserAuthenticationRule implements AuthenticationRule {

	private ConfigurationManager configurationManager;
	protected static final CommonUserAuthenticationRule COMMON_USER_AUTHENTICATION_RULE = new CommonUserAuthenticationRule();

	private CommonUserAuthenticationRule() {
		configurationManager = ConfigurationManager.getInstance();
	}

	@Override
	public AuthenticationResult authenticate(AuthenticationInformation authInfo) throws AuthenticationException {
		AuthenticationLevel userAuthLevel = authInfo.getAuthenticationlevel();
		Set<String> adminUris = configurationManager.getRestrictedUris(AuthenticationLevel.ADMIN);
		Set<String> registeredUserUris = configurationManager.getRestrictedUris(AuthenticationLevel.REGISTERED);
		String requestUri = authInfo.getRequestedUri();
		boolean notAdminUri = !adminUris.contains(requestUri);
		boolean notRegisteredUri = !registeredUserUris.contains(requestUri);
		switch (userAuthLevel) {
		case COMMON:
			if (notAdminUri && notRegisteredUri) {
				return SimpleAuthenticationResult.SUCCESS_AUTHENTICATION_RESULT;
			} else {
				return redirectAuthenticationPage();
			}
		case REGISTERED:
			if (notAdminUri) {
				return SimpleAuthenticationResult.SUCCESS_AUTHENTICATION_RESULT;
			} else {
				return redirectAuthenticationPage();
			}
		case ADMIN:
			return SimpleAuthenticationResult.SUCCESS_AUTHENTICATION_RESULT;
		}
		return SimpleAuthenticationResult.SUCCESS_AUTHENTICATION_RESULT;
	}

	private AuthenticationResult redirectAuthenticationPage() {
		String authenticationUri = configurationManager.getAuthenticationPageUri();
		return new SimpleAuthenticationResult(true, authenticationUri);
	}

	@Override
	public int getPriority() {
		return 10;
	}

}
