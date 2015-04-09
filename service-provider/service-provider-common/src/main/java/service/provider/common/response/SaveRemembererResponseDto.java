package service.provider.common.response;

import service.provider.common.dto.RemembererDto;

public class SaveRemembererResponseDto extends AbstractResponseDto {

	private RemembererDto remembererDto;

	public RemembererDto getRemembererDto() {
		return remembererDto;
	}

	public void setRemembererDto(RemembererDto remembererDto) {
		this.remembererDto = remembererDto;
	}

	@Override
	public String toString() {
		return "SaveRemembererResponseDto [remembererDto=" + remembererDto + "]";
	}

}
