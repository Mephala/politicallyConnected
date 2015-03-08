package serviceprovider.web.configuration;

import java.util.HashMap;
import java.util.Map;

class AppConf {

	protected static final String MAX_AUTHENTICATION_PASSWORD_KEY = "maxAuthenticationPassword";
	protected static final String ADMIN_RESTRICTED_URI_LIST = "adminRestrictedUriList";
	protected static final String REGISTERED_RESTRICTED_URI_LIST = "registeredRestrictedUriList";

	protected static Map<String, String> defaultConfigurationsMap = new HashMap<>();

	static {
		defaultConfigurationsMap.put(MAX_AUTHENTICATION_PASSWORD_KEY, "3");
		defaultConfigurationsMap.put(ADMIN_RESTRICTED_URI_LIST, "/homepage");
		defaultConfigurationsMap.put(REGISTERED_RESTRICTED_URI_LIST, "/baskets");
	}

}
