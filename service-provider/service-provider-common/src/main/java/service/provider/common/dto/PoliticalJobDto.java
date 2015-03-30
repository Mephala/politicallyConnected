package service.provider.common.dto;

public class PoliticalJobDto {

	private String year;

	private String name;

	public synchronized String getYear() {
		return year;
	}

	public synchronized void setYear(String year) {
		this.year = year;
	}

	public synchronized String getName() {
		return name;
	}

	public synchronized void setName(String name) {
		this.name = name;
	}

}
