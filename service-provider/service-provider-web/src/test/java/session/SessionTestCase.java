package session;

import static org.junit.Assert.assertTrue;

import java.util.UUID;

import mockit.integration.junit4.JMockit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import serviceprovider.web.authentication.AuthenticationInformation;
import serviceprovider.web.authentication.AuthenticationLevel;
import serviceprovider.web.session.UniqueUserSessionData;
import serviceprovider.web.session.UserSession;
import serviceprovider.web.session.UserSessionManager;

@RunWith(JMockit.class)
public class SessionTestCase {

	private TestRequest request;
	private UserSessionManager usm;

	@Before
	public void initializeMocks() {
		request = new TestRequest();
		usm = UserSessionManager.getInstance();
	}

	@Test
	public void testMocks() {
		request.setRequestUri("gokhan");
		assertTrue(request != null);
		assertTrue(request.getSession() != null);
		assertTrue(request.getRequestURI().equals("gokhan"));
	}

	@Test
	public void testCreatingUserSession() {
		String randomString = UUID.randomUUID().toString();
		request.setRequestUri(randomString);
		UserSession us = usm.getUserSession(request);
		assertTrue(us != null);
		AuthenticationInformation authInfo = us.getAuthenticationInformation();
		UniqueUserSessionData uusd = us.getUserUniqueSessionData();
		assertTrue(authInfo != null);
		assertTrue(uusd != null);
		assertTrue(authInfo.getRequestedUri().equals(randomString));
		assertTrue(authInfo.getAuthenticationlevel().equals(AuthenticationLevel.COMMON));
	}

	@Test
	public void testChangingRequestUri() {
		String randomString = UUID.randomUUID().toString();
		request.setRequestUri(randomString);
		UserSession us = usm.getUserSession(request);
		assertTrue(us != null);
		AuthenticationInformation authInfo = us.getAuthenticationInformation();
		UniqueUserSessionData uusd = us.getUserUniqueSessionData();
		assertTrue(authInfo != null);
		assertTrue(uusd != null);
		assertTrue(authInfo.getRequestedUri().equals(randomString));
		request = new TestRequest();
		request.setRequestUri("gokhan");
		us = usm.getUserSession(request);
		authInfo = us.getAuthenticationInformation();
		assertTrue(authInfo.getRequestedUri().equals("gokhan"));
	}

	@Test
	public void testIncrementingFailedPassword() {
		String randomString = UUID.randomUUID().toString();
		request.setRequestUri(randomString);
		UserSession us = usm.getUserSession(request);
		assertTrue(us != null);
		AuthenticationInformation authInfo = us.getAuthenticationInformation();
		UniqueUserSessionData uusd = us.getUserUniqueSessionData();
		assertTrue(authInfo != null);
		assertTrue(uusd != null);
		assertTrue(authInfo.getRequestedUri().equals(randomString));
		us.incrementFailedPasswordAttempt();
		us.incrementFailedPasswordAttempt();
		us.incrementFailedPasswordAttempt();
		us.incrementFailedPasswordAttempt();
		assertTrue(us.getAuthenticationInformation().canAuthenticate() == false);
	}

}
