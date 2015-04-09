package service.provider.common.request;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import service.provider.common.dto.AbstractRequestDto;
import service.provider.common.dto.RemembererDto;


@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteRemembererRequestDto extends AbstractRequestDto {
	private RemembererDto remembererDto;

	public DeleteRemembererRequestDto() {

	}

	DeleteRemembererRequestDto(String uri) {
		this.setRequestUri(uri);
	}

	public boolean isValid() {
		return remembererDto != null;
	}

	public RemembererDto getRemembererDto() {
		return remembererDto;
	}

	public void setRemembererDto(RemembererDto remembererDto) {
		this.remembererDto = remembererDto;
	}

	@Override
	public String toString() {
		return "DeleteRemembererRequestDto [remembererDto=" + remembererDto + "]";
	}

}
