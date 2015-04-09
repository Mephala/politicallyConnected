package service.provider.common.request;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import service.provider.common.dto.AbstractRequestDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteSchedulerRequestDto extends AbstractRequestDto {

	public DeleteSchedulerRequestDto() {

	}

	DeleteSchedulerRequestDto(String uri) {
		setRequestUri(uri);
	}

	private Long schedulerId;

	public boolean isValid() {
		return schedulerId != null;
	}

	public Long getSchedulerId() {
		return schedulerId;
	}

	public void setSchedulerId(Long schedulerId) {
		this.schedulerId = schedulerId;
	}

}
