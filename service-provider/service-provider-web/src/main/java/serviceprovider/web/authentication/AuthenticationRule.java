package serviceprovider.web.authentication;

public interface AuthenticationRule {

	public AuthenticationResult authenticate(AuthenticationInformation authInfo) throws AuthenticationException;

	public int getPriority();
}
