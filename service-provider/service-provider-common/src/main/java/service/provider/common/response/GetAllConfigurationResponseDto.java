package service.provider.common.response;

import java.util.List;

import service.provider.common.dto.ConfigurationDto;

public class GetAllConfigurationResponseDto extends AbstractResponseDto {

	public GetAllConfigurationResponseDto() {

	}

	private List<ConfigurationDto> allConfigurations;

	public List<ConfigurationDto> getAllConfigurations() {
		return allConfigurations;
	}

	public void setAllConfigurations(List<ConfigurationDto> allConfigurations) {
		this.allConfigurations = allConfigurations;
	}

}
