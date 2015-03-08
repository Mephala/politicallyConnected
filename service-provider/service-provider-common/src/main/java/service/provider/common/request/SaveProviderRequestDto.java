package service.provider.common.request;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import service.provider.common.dto.AbstractRequestDto;
import service.provider.common.dto.ProviderDto;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveProviderRequestDto extends AbstractRequestDto {

	private ProviderDto providerDto;

	public SaveProviderRequestDto() {

	}

	public SaveProviderRequestDto(String saveProviderRequestUri) {
		this.setRequestUri(saveProviderRequestUri);
	}

	public boolean isValid() {
		if (this.providerDto == null) {
			return false;
		}
		boolean hasValidPhoneNumber = (providerDto.getGsm() != null || providerDto.getStaticPhone() != null);
		if (!hasValidPhoneNumber) {
			return false;
		}
		boolean phonesHaveData = providerDto.getGsm().length() > 0 || providerDto.getStaticPhone().length() > 0;
		if (!phonesHaveData) {
			return false;
		}
		if (providerDto.getTckn() == null || providerDto.getTckn().length() == 0) {
			return false;
		}
		return true;
	}

	public ProviderDto getProviderDto() {
		return providerDto;
	}

	public void setProviderDto(ProviderDto providerDto) {
		this.providerDto = providerDto;
	}

}
