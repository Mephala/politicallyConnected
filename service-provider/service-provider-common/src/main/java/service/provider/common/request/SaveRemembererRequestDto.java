package service.provider.common.request;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import service.provider.common.dto.AbstractRequestDto;
import service.provider.common.dto.RemembererDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveRemembererRequestDto extends AbstractRequestDto {

	private RemembererDto remembererDto;

	public SaveRemembererRequestDto() {

	}

	public SaveRemembererRequestDto(String getSaveRemembererRequestUri) {
		this.setRequestUri(getSaveRemembererRequestUri);
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
		return "SaveRemembererRequestDto [remembererDto=" + remembererDto + "]";
	}

}
