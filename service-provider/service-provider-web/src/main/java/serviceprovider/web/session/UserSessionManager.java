package serviceprovider.web.session;

import javax.servlet.http.HttpServletRequest;

import serviceprovider.web.util.CommonHtmlUtils;

public class UserSessionManager {

	private static UserSessionManager instance;
	private static final String USER_SESSION_KEY = "suser";

	private UserSessionManager() {

	}

	public static synchronized UserSessionManager getInstance() {
		if (instance == null)
			instance = new UserSessionManager();
		return instance;
	}

	/**
	 * Return current session Info, creates if not exists.
	 */
	public UserSession getUserSession(HttpServletRequest request) {
		CommonUserSession commonUserSession = (CommonUserSession) request.getSession().getAttribute(USER_SESSION_KEY);
		if (commonUserSession == null) {
			UserSession userSession = createCommonUserSession(request);
			request.getSession().setAttribute(USER_SESSION_KEY, userSession);
			return userSession;
		}
		commonUserSession.changeRequestUri(request.getRequestURI());
		return commonUserSession;
	}

	private UserSession createCommonUserSession(HttpServletRequest request) {
		UserSessionCredentialsData credentialsData = createUserSessionCredentials(request);
		String requestUri = request.getRequestURI();
		CommonUserSession cus = CommonUserSession.createCommonUserSession(requestUri, credentialsData);
		return cus;
	}

	private UserSessionCredentialsData createUserSessionCredentials(HttpServletRequest request) {
		String ip = CommonHtmlUtils.getClientIpAddr(request);
		String sessionId = request.getSession().getId();
		return new UserSessionCredentialsData(ip, sessionId);
	}

}
