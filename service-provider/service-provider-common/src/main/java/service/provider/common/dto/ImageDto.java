package service.provider.common.dto;

public class ImageDto {

	private Long id;

	private String encodedData;

	public synchronized Long getId() {
		return id;
	}

	public synchronized void setId(Long id) {
		this.id = id;
	}

	public synchronized String getEncodedData() {
		return encodedData;
	}

	public synchronized void setEncodedData(String encodedData) {
		this.encodedData = encodedData;
	}

}
