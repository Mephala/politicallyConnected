package serviceprovider.util;

import service.provider.common.dto.UserDto;
import serviceprovider.model.User;

public class ModelToDtoConverter {

	public static UserDto createUserDto(User user) {
		UserDto userDto = new UserDto();
		userDto.setEmail(user.getEmail());
		userDto.setLastname(user.getLastname());
		userDto.setFirstname(user.getFirstname());
		userDto.setUserName(user.getUsername());
		userDto.setId(user.getId());
		return userDto;
	}
}
