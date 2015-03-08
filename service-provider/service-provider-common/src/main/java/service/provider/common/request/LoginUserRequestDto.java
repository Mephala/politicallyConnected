package service.provider.common.request;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import service.provider.common.dto.AbstractRequestDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginUserRequestDto extends AbstractRequestDto {

	private String email;
	private String password;

	protected LoginUserRequestDto(String requestUri) {
		super();
		this.setRequestUri(requestUri);
	}

	public LoginUserRequestDto() {

	}

	public boolean isValid() {
		boolean validity = false;
		if (email != null && password != null) {
			if (email.length() > 0 && password.length() > 0) {
				validity = true;
			}
		}
		return validity;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginUserRequestDto [email=" + email + ", password=" + password + "]";
	}

}
