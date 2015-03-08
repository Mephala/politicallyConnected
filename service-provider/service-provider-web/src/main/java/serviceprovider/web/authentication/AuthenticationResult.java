package serviceprovider.web.authentication;

public interface AuthenticationResult {

	public boolean redirectNeeded();

	public String redirectUri();

}
