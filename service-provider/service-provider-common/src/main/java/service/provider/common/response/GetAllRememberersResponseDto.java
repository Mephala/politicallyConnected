package service.provider.common.response;

import java.util.List;

import service.provider.common.dto.RemembererDto;

public class GetAllRememberersResponseDto extends AbstractResponseDto {

	private List<RemembererDto> allRememberers;

	public GetAllRememberersResponseDto() {

	}

	public List<RemembererDto> getAllRememberers() {
		return allRememberers;
	}

	public void setAllRememberers(List<RemembererDto> allRememberers) {
		this.allRememberers = allRememberers;
	}

	@Override
	public String toString() {
		return "GetAllRememberersResponseDto [allRememberers=" + allRememberers + "]";
	}

}
