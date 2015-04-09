package service.provider.common.response;

import service.provider.common.dto.UserDto;

public class LoginUserResponseDto extends AbstractResponseDto {

	private UserDto userDto;

	public LoginUserResponseDto() {
		super();
	}

	public UserDto getUserDto() {
		return userDto;
	}

	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}

}
