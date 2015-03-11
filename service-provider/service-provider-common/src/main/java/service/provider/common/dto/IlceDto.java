package service.provider.common.dto;

import java.util.List;

public class IlceDto {

	private String name;
	private Long id;
	private List<SemtDto> semtList;

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

	public List<SemtDto> getSemtList() {
		return semtList;
	}

	public void setSemtList(List<SemtDto> semtList) {
		this.semtList = semtList;
	}

}
