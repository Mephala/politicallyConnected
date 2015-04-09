package service.provider.common.request;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import service.provider.common.dto.AbstractRequestDto;
import service.provider.common.dto.ConfigurationDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveConfigurationRequestDto extends AbstractRequestDto {

	private ConfigurationDto configurationDto;

	public SaveConfigurationRequestDto() {

	}

	public SaveConfigurationRequestDto(String requestUri) {
		setRequestUri(requestUri);
	}

	public boolean isValid() {
		return (getRequestApp() != null) && configurationDto != null;
	}

	public ConfigurationDto getConfigurationDto() {
		return configurationDto;
	}

	public void setConfigurationDto(ConfigurationDto configurationDto) {
		this.configurationDto = configurationDto;
	}

}
