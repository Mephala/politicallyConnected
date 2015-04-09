package service.provider.common.dto;

import java.util.List;

public class CityDto {

	private String name;
	private Long id;
	private List<IlceDto> ilceList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<IlceDto> getIlceList() {
		return ilceList;
	}

	public void setIlceList(List<IlceDto> ilceList) {
		this.ilceList = ilceList;
	}

}
