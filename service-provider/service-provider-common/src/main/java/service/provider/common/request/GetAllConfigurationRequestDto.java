package service.provider.common.request;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import service.provider.common.dto.AbstractRequestDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAllConfigurationRequestDto extends AbstractRequestDto {

	public GetAllConfigurationRequestDto() {

	}

	public GetAllConfigurationRequestDto(String reqUri) {
		setRequestUri(reqUri);
	}

	public boolean isValid() {
		return getRequestApp() != null;
	}

}
