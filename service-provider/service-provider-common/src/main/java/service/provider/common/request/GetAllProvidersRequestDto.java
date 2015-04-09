package service.provider.common.request;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import service.provider.common.dto.AbstractRequestDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAllProvidersRequestDto extends AbstractRequestDto {

	private Integer providerLimit;

	private BigDecimal lattitude;

	private BigDecimal longitude;

	private BigDecimal searchDiameter;

	public GetAllProvidersRequestDto() {
	}

	protected GetAllProvidersRequestDto(String getAllProvidersUri) {
		this.setRequestUri(getAllProvidersUri);
	}

	public boolean isValid() {
		return true; // This request is always valid unless it is not secure.
	}

	public Integer getProviderLimit() {
		return providerLimit;
	}

	public void setProviderLimit(Integer providerLimit) {
		this.providerLimit = providerLimit;
	}

	public BigDecimal getLattitude() {
		return lattitude;
	}

	public void setLattitude(BigDecimal lattitude) {
		this.lattitude = lattitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getSearchDiameter() {
		return searchDiameter;
	}

	public void setSearchDiameter(BigDecimal searchDiameter) {
		this.searchDiameter = searchDiameter;
	}

	@Override
	public String toString() {
		return "GetAllProvidersRequestDto [providerLimit=" + providerLimit + ", lattitude=" + lattitude + ", longitude=" + longitude + ", searchDiameter=" + searchDiameter + "]";
	}

}
