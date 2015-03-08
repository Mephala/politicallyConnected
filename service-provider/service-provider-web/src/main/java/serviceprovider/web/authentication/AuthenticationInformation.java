package serviceprovider.web.authentication;

public interface AuthenticationInformation extends AuthenticationConstraints {

	public String getRequestedUri();

	public AuthenticationLevel getAuthenticationlevel();

	public String getIp();

	public String getSessionId();

}
