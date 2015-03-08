package serviceprovider.web.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import serviceprovider.web.authentication.AuthenticationException;
import serviceprovider.web.authentication.AuthenticationManager;
import serviceprovider.web.authentication.AuthenticationResult;
import serviceprovider.web.configuration.ConfigurationManager;
import serviceprovider.web.session.UserSession;
import serviceprovider.web.session.UserSessionManager;

public class SecurityInterceptor implements HandlerInterceptor {

	private UserSessionManager userSessionManager = UserSessionManager.getInstance();
	private AuthenticationManager authenticationManager = AuthenticationManager.getInstance();
	private ConfigurationManager configurationManager = ConfigurationManager.getInstance();
	private Log logger = LogFactory.getLog(getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		UserSession userSession = userSessionManager.getUserSession(request);
		if (!userSession.getAuthenticationInformation().canAuthenticate()) {
			logger.info("Sending forbidden message to user due to failed authentication.");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return false;
		}
		try {
			AuthenticationResult authResult = authenticationManager.authenticate(userSession);
			if (authResult.redirectNeeded()) {
				logger.info("Redirection is needed after authentication rules. Redirecting to uri:" + authResult.redirectUri());
				redirectToUri(request, response, authResult.redirectUri());
			}
		} catch (AuthenticationException ae) {
			logger.info("Authentication Exception handled. User request is intercepted. Ip:" + userSession.getAuthenticationInformation().getIp());
			return false;
		}
		return true;
	}

	private void redirectToUri(HttpServletRequest request, HttpServletResponse response, String uri) throws IOException {
		response.sendRedirect(uri);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		;
	}

}
