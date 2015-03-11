package service.provider.common.dto;

import java.util.List;

public class SemtDto {

	private String name;
	private Long id;
	private List<MahalleDto> mahalleList;

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

	public List<MahalleDto> getMahalleList() {
		return mahalleList;
	}

	public void setMahalleList(List<MahalleDto> mahalleList) {
		this.mahalleList = mahalleList;
	}

}
