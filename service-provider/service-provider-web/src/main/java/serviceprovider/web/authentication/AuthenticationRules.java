package serviceprovider.web.authentication;

import java.util.ArrayList;
import java.util.List;

class AuthenticationRules {

	private static List<AuthenticationRule> rules = new ArrayList<>();
	static {
		rules.add(CommonUserAuthenticationRule.COMMON_USER_AUTHENTICATION_RULE);
	}

	protected static List<AuthenticationRule> getAuthenticationRules() {
		return rules;
	}

}
