package service.provider.common.dto;

import java.util.List;

public class PcaPersonDto {

	private Long id;

	private String name;

	List<ManagementJobDto> managementJobs;

	List<PoliticalJobDto> politicalJobs;

	public synchronized Long getId() {
		return id;
	}

	public synchronized void setId(Long id) {
		this.id = id;
	}

	public synchronized String getName() {
		return name;
	}

	public synchronized void setName(String name) {
		this.name = name;
	}

	public synchronized List<ManagementJobDto> getManagementJobs() {
		return managementJobs;
	}

	public synchronized void setManagementJobs(List<ManagementJobDto> managementJobs) {
		this.managementJobs = managementJobs;
	}

	public synchronized List<PoliticalJobDto> getPoliticalJobs() {
		return politicalJobs;
	}

	public synchronized void setPoliticalJobs(List<PoliticalJobDto> politicalJobs) {
		this.politicalJobs = politicalJobs;
	}

	@Override
	public String toString() {
		return "PcaPersonDto [id=" + id + ", name=" + name + ", managementJobs=" + managementJobs + ", politicalJobs=" + politicalJobs + "]";
	}

}
