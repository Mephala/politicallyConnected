package service.provider.common.response;

import service.provider.common.dto.ConfigurationDto;

public class SaveConfigurationResponseDto extends AbstractResponseDto {

	private ConfigurationDto configurationDto;

	public SaveConfigurationResponseDto() {

	}

	public ConfigurationDto getConfigurationDto() {
		return configurationDto;
	}

	public void setConfigurationDto(ConfigurationDto configurationDto) {
		this.configurationDto = configurationDto;
	}

	@Override
	public String toString() {
		return "SaveConfigurationResponseDto [configurationDto=" + configurationDto + "]";
	}

}
