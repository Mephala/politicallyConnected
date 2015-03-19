package service.provider.common.request;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import service.provider.common.dto.AbstractRequestDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetImageRequestDto extends AbstractRequestDto {

	public GetImageRequestDto() {

	}

	public GetImageRequestDto(String uri) {
		setRequestUri(uri);
	}

	private Long imageId;

	public boolean isValid() {
		return imageId != null && imageId > 0;
	}

	public synchronized Long getImageId() {
		return imageId;
	}

	public synchronized void setImageId(Long imageId) {
		this.imageId = imageId;
	}

}
