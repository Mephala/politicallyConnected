package service.provider.common.request;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import service.provider.common.dto.AbstractRequestDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAllSchedulersRequestDto extends AbstractRequestDto {

	public GetAllSchedulersRequestDto() {

	}

	GetAllSchedulersRequestDto(String uri) {
		setRequestUri(uri);
	}

	public boolean isValid() {
		return true;
	}

}
