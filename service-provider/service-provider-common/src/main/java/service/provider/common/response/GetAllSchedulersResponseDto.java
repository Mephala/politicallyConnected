package service.provider.common.response;

import java.util.Set;

import service.provider.common.dto.SchedulerDto;

public class GetAllSchedulersResponseDto extends AbstractResponseDto {

	public GetAllSchedulersResponseDto() {

	}

	private Set<SchedulerDto> allSchedulers;

	public Set<SchedulerDto> getAllSchedulers() {
		return allSchedulers;
	}

	public void setAllSchedulers(Set<SchedulerDto> allSchedulers) {
		this.allSchedulers = allSchedulers;
	}

	@Override
	public String toString() {
		return "GetAllSchedulersResponseDto [allSchedulers=" + allSchedulers + "]";
	}

}
