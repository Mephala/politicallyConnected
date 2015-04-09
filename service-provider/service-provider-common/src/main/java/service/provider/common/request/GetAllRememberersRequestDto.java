package service.provider.common.request;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import service.provider.common.dto.AbstractRequestDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAllRememberersRequestDto extends AbstractRequestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3938583475052783872L;

	GetAllRememberersRequestDto() {

	}

	GetAllRememberersRequestDto(String getAllRememberersRequestUri) {
		setRequestUri(getAllRememberersRequestUri);
	}

	public boolean isValid() {
		return true;
	}

}
