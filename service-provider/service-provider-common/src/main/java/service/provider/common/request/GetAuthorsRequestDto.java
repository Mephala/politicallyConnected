package service.provider.common.request;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import service.provider.common.dto.AbstractRequestDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAuthorsRequestDto extends AbstractRequestDto {

	GetAuthorsRequestDto() {

	}

	GetAuthorsRequestDto(String authorsUrı) {
		setRequestUri(authorsUrı);
	}

	public boolean isValid() {
		return true;
	}

}
