package service.provider.common.request;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import service.provider.common.dto.AbstractRequestDto;
import service.provider.common.dto.SchedulerDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveSchedulerRequestDto extends AbstractRequestDto{
	
	private SchedulerDto schedulerDto;
	
	public SaveSchedulerRequestDto(){
		
	}

	public SaveSchedulerRequestDto(String saveSchedulerRequestUri) {
		this.setRequestUri(saveSchedulerRequestUri);
	}

	public boolean isValid() {
		return true;
	}

	public SchedulerDto getSchedulerDto() {
		return schedulerDto;
	}

	public void setSchedulerDto(SchedulerDto schedulerDto) {
		this.schedulerDto = schedulerDto;
	}

}
