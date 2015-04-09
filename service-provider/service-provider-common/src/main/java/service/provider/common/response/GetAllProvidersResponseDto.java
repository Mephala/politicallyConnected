package service.provider.common.response;

import java.util.List;

import service.provider.common.dto.ProviderDto;

public class GetAllProvidersResponseDto extends AbstractResponseDto {

	private List<ProviderDto> providers;

	public GetAllProvidersResponseDto() {
		super();
	}

	public List<ProviderDto> getProviders() {
		return providers;
	}

	public void setProviders(List<ProviderDto> providers) {
		this.providers = providers;
	}

	@Override
	public String toString() {
		return "GetAllProvidersResponseDto [providers=" + providers + "]";
	}

}
