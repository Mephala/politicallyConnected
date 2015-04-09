package service.provider.common.response;

import service.provider.common.dto.SchedulerDto;

public class SaveSchedulerResponseDto extends AbstractResponseDto {
	
	private SchedulerDto schedulerDto;

	public SchedulerDto getSchedulerDto() {
		return schedulerDto;
	}

	public void setSchedulerDto(SchedulerDto schedulerDto) {
		this.schedulerDto = schedulerDto;
	}

	@Override
	public String toString() {
		return "SaveSchedulerResponseDto [schedulerDto=" + schedulerDto + "]";
	}

}
