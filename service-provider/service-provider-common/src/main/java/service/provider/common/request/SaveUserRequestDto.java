package service.provider.common.request;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import service.provider.common.dto.AbstractRequestDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveUserRequestDto extends AbstractRequestDto {
	private String username;
	private String password;
	private String email;
	private String lastname;
	private String firstname;

	protected SaveUserRequestDto(String requestUri) {
		super();
		this.setRequestUri(requestUri);
	}

	public SaveUserRequestDto() {

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public boolean isValid() {
		boolean isValid = false;
		if (email != null && password != null) {
			if (email.length() > 0 && password.length() > 0) {
				isValid = true;
			}
		}
		return isValid;
	}

	@Override
	public String toString() {
		return "SaveUserRequestDto [username=" + username + ", password=" + password + ", email=" + email + ", lastname=" + lastname + ", firstname=" + firstname + "]";
	}
}
