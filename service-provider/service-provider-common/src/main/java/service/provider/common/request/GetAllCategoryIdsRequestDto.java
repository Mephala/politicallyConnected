package service.provider.common.request;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import service.provider.common.dto.AbstractRequestDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAllCategoryIdsRequestDto extends AbstractRequestDto {

	public GetAllCategoryIdsRequestDto() {

	}

	public GetAllCategoryIdsRequestDto(String getAllCategoryIdsRequestsUri) {
		this.setRequestUri(getAllCategoryIdsRequestsUri);
	}

	public boolean isValid() {
		return true;
	}

}
