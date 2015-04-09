package service.provider.common.response;

import java.util.List;

import service.provider.common.dto.CityDto;

public class GetAllCitiesResponseDto extends AbstractResponseDto {

	private List<CityDto> cityList;

	public GetAllCitiesResponseDto() {

	}

	public List<CityDto> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityDto> cityList) {
		this.cityList = cityList;
	}

}
