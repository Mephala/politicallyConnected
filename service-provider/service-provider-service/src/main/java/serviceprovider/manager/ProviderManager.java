package serviceprovider.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import service.provider.common.dto.ProviderDto;
import service.provider.common.request.GetAllProvidersRequestDto;
import serviceprovider.dao.ProviderDao;
import serviceprovider.model.Provider;

@Service
public class ProviderManager extends AbstractServiceManager<Provider> {

	@Autowired
	private ProviderDao providerDao;

	@PostConstruct
	public void initialize() {
		initializeDAO(providerDao);
	}

	public List<Provider> getProviderListByRequest(GetAllProvidersRequestDto requestDto) {
		// FIXME Returning everything!
		return providerDao.getAllModelList();
	}

	public List<ProviderDto> createProviderDtoList(List<Provider> filteredProviders) {
		List<ProviderDto> filteredProviderDtoList = new ArrayList<ProviderDto>();
		if (!CollectionUtils.isEmpty(filteredProviders)) {
			for (Provider provider : filteredProviders) {
				ProviderDto providerDto = createProviderDto(provider);
				filteredProviderDtoList.add(providerDto);
			}
		}
		return filteredProviderDtoList;
	}

	public ProviderDto createProviderDto(Provider provider) {
		ProviderDto providerDto = null;
		if (provider != null) {
			providerDto = new ProviderDto();
			providerDto.setGsm(provider.getGsm());
			providerDto.setId(provider.getId());
			providerDto.setLattitute(provider.getLattitute());
			providerDto.setLongitute(provider.getLongitute());
			providerDto.setStaticPhone(provider.getStaticPhone());
			providerDto.setTitle(provider.getTitle());
			providerDto.setTckn(provider.getTckn());
		}
		return providerDto;
	}

	/**
	 * Disregarding user for the time being.
	 */
	public void convertAndSaveProviderDto(ProviderDto providerDto) {
		// FIXME Add user connection!
		if (providerDto != null) {
			Provider provider = new Provider();
			provider.setGsm(providerDto.getGsm());
			provider.setLattitute(providerDto.getLattitute());
			provider.setLongitute(providerDto.getLongitute());
			provider.setStaticPhone(providerDto.getStaticPhone());
			provider.setTckn(providerDto.getTckn());
			provider.setTitle(providerDto.getTitle());
			// provider.setUser(user);
			saveModel(provider);
		}
	}

}
