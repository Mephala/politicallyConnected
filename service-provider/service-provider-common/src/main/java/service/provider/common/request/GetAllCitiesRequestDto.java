package service.provider.common.request;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import service.provider.common.dto.AbstractRequestDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAllCitiesRequestDto extends AbstractRequestDto {

	public GetAllCitiesRequestDto() {

	}

	public GetAllCitiesRequestDto(String getAllCitiesRequestUri) {
		setRequestUri(getAllCitiesRequestUri);
	}

	public boolean isValid() {
		return true;
	}

}
