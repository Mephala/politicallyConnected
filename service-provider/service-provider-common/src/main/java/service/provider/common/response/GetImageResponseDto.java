package service.provider.common.response;

import service.provider.common.dto.ImageDto;

public class GetImageResponseDto extends AbstractResponseDto {

	private ImageDto imageDto;

	public GetImageResponseDto() {

	}

	public synchronized ImageDto getImageDto() {
		return imageDto;
	}

	public synchronized void setImageDto(ImageDto imageDto) {
		this.imageDto = imageDto;
	}

}
