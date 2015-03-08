package service.provider.common.request;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import service.provider.common.dto.AbstractRequestDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetCategoryRequestDto extends AbstractRequestDto {

	private Long id;

	private boolean withoutProviders;

	public GetCategoryRequestDto() {

	}

	public GetCategoryRequestDto(String getCategoryRequestUri) {
		this.setRequestUri(getCategoryRequestUri);
	}

	public boolean isValid() {
		return id != null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isWithoutProviders() {
		return withoutProviders;
	}

	/**
	 * If this flag is set to true, server responds with included providers. Call it with false (default value is false ) if you just want information about category.
	 */
	public void setWithoutProviders(boolean withoutProviders) {
		this.withoutProviders = withoutProviders;
	}

	@Override
	public String toString() {
		return "GetCategoryRequestDto [id=" + id + ", withoutProviders=" + withoutProviders + "]";
	}

}
