package service.provider.common.request;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import service.provider.common.dto.AbstractRequestDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAllImageIdsRequestDto extends AbstractRequestDto {

	public GetAllImageIdsRequestDto() {

	}

	public GetAllImageIdsRequestDto(String getAllImageIdsUri) {
		setRequestUri(getAllImageIdsUri);
	}

	public boolean isValid() {
		return true;
	}

}
