package service.provider.common.response;

import java.util.List;

import service.provider.common.dto.PcaPersonDto;

public class GetAllPcaDataResponseDto extends AbstractResponseDto {

	public GetAllPcaDataResponseDto() {

	}

	private List<PcaPersonDto> allPersonDtoList;

	public synchronized List<PcaPersonDto> getAllPersonDtoList() {
		return allPersonDtoList;
	}

	public synchronized void setAllPersonDtoList(List<PcaPersonDto> allPersonDtoList) {
		this.allPersonDtoList = allPersonDtoList;
	}

}
