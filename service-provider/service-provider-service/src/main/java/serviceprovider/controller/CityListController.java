package serviceprovider.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import service.provider.common.core.ResponseStatus;
import service.provider.common.dto.CityDto;
import service.provider.common.exception.AbstractServiceException;
import service.provider.common.request.GetAllCitiesRequestDto;
import service.provider.common.response.GetAllCitiesResponseDto;
import serviceprovider.service.address.AddressService;

@Controller
public class CityListController extends AbstractServiceController {

	private Log logger = LogFactory.getLog(this.getClass());
	private AddressService addressService = AddressService.getInstance();

	@RequestMapping(value = "/getAllCities.do", method = RequestMethod.POST)
	@ResponseBody
	public Object getAllCities(HttpServletRequest request, HttpServletResponse response, @RequestBody final GetAllCitiesRequestDto getAllCitiesRequest) {
		logger.info("Getting all cities for request:" + getAllCitiesRequest);
		GetAllCitiesResponseDto responseDto = new GetAllCitiesResponseDto();
		try {
			validateRequest(getAllCitiesRequest);
			List<CityDto> citiesDto = addressService.getTurkishCitiesAsDto();
			responseDto.setCityList(citiesDto);
			responseDto.setResponseStatus(ResponseStatus.OK);
			logger.info("Cities are serialized and responded upon request.");
		} catch (AbstractServiceException ase) {
			logger.info("request  encountered serviceException. Exception:" + ase);
			setMeaningfulException(responseDto, ase);
		} catch (Exception e) {
			logger.info("request  encountered serviceException. Exception:" + e);
			responseDto.setError("Error occured");
			responseDto.setResponseStatus(ResponseStatus.ERROR);
		}
		return responseDto;
	}
}
