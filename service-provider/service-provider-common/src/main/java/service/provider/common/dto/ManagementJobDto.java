package service.provider.common.dto;

public class ManagementJobDto {

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

	@Override
	public String toString() {
		return "ManagementJobDto [year=" + year + ", name=" + name + "]";
	}

}
