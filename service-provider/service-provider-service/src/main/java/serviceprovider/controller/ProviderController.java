package serviceprovider.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import service.provider.common.core.ResponseStatus;
import service.provider.common.dto.ProviderDto;
import service.provider.common.exception.AbstractServiceException;
import service.provider.common.request.GetAllProvidersRequestDto;
import service.provider.common.request.SaveProviderRequestDto;
import service.provider.common.response.AbstractResponseDto;
import service.provider.common.response.GetAllProvidersResponseDto;
import service.provider.common.response.SaveProviderResponseDto;
import serviceprovider.manager.ProviderManager;
import serviceprovider.model.Provider;

@Controller
public class ProviderController extends AbstractServiceController {

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private ProviderManager providerManager;

	@RequestMapping(value = "/getAllProviders.do", method = RequestMethod.POST)
	@ResponseBody
	public Object getAllProviders(HttpServletRequest request, HttpServletResponse response, @RequestBody final GetAllProvidersRequestDto requestDto) {
		logger.info("Get all providers request is received." + requestDto);
		GetAllProvidersResponseDto responseDto = new GetAllProvidersResponseDto();
		try {
			validateRequest(requestDto);
			List<Provider> filteredProviders = providerManager.getProviderListByRequest(requestDto);
			List<ProviderDto> filteredProvidersDto = providerManager.createProviderDtoList(filteredProviders);
			responseDto.setProviders(filteredProvidersDto);
			responseDto.setResponseStatus(ResponseStatus.OK);

		} catch (AbstractServiceException ase) {
			setMeaningfulException(responseDto, ase);
		} catch (Exception e) {
			setNonMeaningfulException(responseDto, e);
		} finally {

		}
		return responseDto;
	}

	@RequestMapping(value = "/saveProvider.do", method = RequestMethod.POST)
	@ResponseBody
	public Object saveProvider(HttpServletRequest request, HttpServletResponse response, @RequestBody final SaveProviderRequestDto requestDto) {
		logger.info("Get all providers request is received." + requestDto);
		SaveProviderResponseDto responseDto = new SaveProviderResponseDto();
		try {
			validateRequest(requestDto);
			providerManager.convertAndSaveProviderDto(requestDto.getProviderDto());
			responseDto.setResponseStatus(ResponseStatus.OK);
		} catch (AbstractServiceException ase) {
			setMeaningfulException(responseDto, ase);
		} catch (Exception e) {
			setNonMeaningfulException(responseDto, e);
		} finally {

		}
		return responseDto;
	}

	protected void setNonMeaningfulException(AbstractResponseDto responseDto, Exception e) {
		responseDto.setResponseStatus(ResponseStatus.ERROR);
		responseDto.setError(e.getMessage());
	}

}
