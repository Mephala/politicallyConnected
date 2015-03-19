package service.provider.common.response;

import java.util.List;

public class GetAllImageIdsResponseDto extends AbstractResponseDto {

	private List<Long> imageIds;

	public GetAllImageIdsResponseDto() {

	}

	public synchronized List<Long> getImageIds() {
		return imageIds;
	}

	public synchronized void setImageIds(List<Long> imageIds) {
		this.imageIds = imageIds;
	}

}
