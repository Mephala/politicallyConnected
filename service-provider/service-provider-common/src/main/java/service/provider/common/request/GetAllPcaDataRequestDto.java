package service.provider.common.request;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import service.provider.common.dto.AbstractRequestDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAllPcaDataRequestDto extends AbstractRequestDto {

	public GetAllPcaDataRequestDto() {

	}

	public GetAllPcaDataRequestDto(String reqUri) {
		setRequestUri(reqUri);
	}

	public boolean isValid() {
		return true;
	}

}
