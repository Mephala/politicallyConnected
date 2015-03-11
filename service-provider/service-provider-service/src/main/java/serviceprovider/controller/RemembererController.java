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
import service.provider.common.dto.RemembererDto;
import service.provider.common.exception.AbstractServiceException;
import service.provider.common.exception.InvalidRequestException;
import service.provider.common.request.DeleteRemembererRequestDto;
import service.provider.common.request.GetAllRememberersRequestDto;
import service.provider.common.request.SaveRemembererRequestDto;
import service.provider.common.response.DeleteRemembererResponseDto;
import service.provider.common.response.GetAllRememberersResponseDto;
import service.provider.common.response.SaveRemembererResponseDto;
import serviceprovider.manager.RemembererManager;
import serviceprovider.model.Rememberer;
import serviceprovider.service.address.AddressService;

@Controller
public class RemembererController extends AbstractServiceController {

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private RemembererManager remembererManager;

	@RequestMapping(value = "/saveRememberer.do", method = RequestMethod.POST)
	@ResponseBody
	public Object saveRememberer(HttpServletRequest request, HttpServletResponse response, @RequestBody final SaveRemembererRequestDto saveRemembererRequest) {
		AddressService as = AddressService.getInstance();
		System.out.println(as != null);
		as.getTurkishCitiesList();
		logger.info("Save rememberer request is received." + saveRemembererRequest);
		SaveRemembererResponseDto responseDto = new SaveRemembererResponseDto();
		try {
			validateRequest(saveRemembererRequest);
			Rememberer rememberer = remembererManager.createOrFindRememberer(saveRemembererRequest.getRemembererDto());
			remembererManager.saveModel(rememberer);
			RemembererDto savedRemembererDto = remembererManager.createRemembererDto(rememberer);
			logger.info("Save rememberer request is OK." + saveRemembererRequest);
			responseDto.setResponseStatus(ResponseStatus.OK);
			responseDto.setRemembererDto(savedRemembererDto);
		} catch (AbstractServiceException ase) {
			logger.info("Exception during save rememberer request." + saveRemembererRequest);
			setMeaningfulException(responseDto, ase);
		}
		return responseDto;
	}

	@RequestMapping(value = "/deleteRememberer.do", method = RequestMethod.POST)
	@ResponseBody
	public Object deleteRememberer(HttpServletRequest request, HttpServletResponse response, @RequestBody final DeleteRemembererRequestDto deleteRemembererRequest) {
		logger.info("Delete rememberer request is received." + deleteRemembererRequest);
		DeleteRemembererResponseDto responseDto = new DeleteRemembererResponseDto();
		try {
			validateRequest(deleteRemembererRequest);
			Rememberer rememberer = remembererManager.createOrFindRememberer(deleteRemembererRequest.getRemembererDto());
			if (rememberer == null) {
				logger.info("Delete rememberer request is FAILED because item could't find on DB.." + deleteRemembererRequest);
				throw new InvalidRequestException();
			}
			remembererManager.deleteModel(rememberer);
			logger.info("Delete rememberer request is OK." + deleteRemembererRequest);
			responseDto.setResponseStatus(ResponseStatus.OK);
		} catch (AbstractServiceException ase) {
			logger.info("Exception during delete rememberer request." + deleteRemembererRequest);
			setMeaningfulException(responseDto, ase);
		}
		return responseDto;
	}

	@RequestMapping(value = "/getAllRememberers.do", method = RequestMethod.POST)
	@ResponseBody
	public Object getAllRemembererList(HttpServletRequest request, HttpServletResponse response, @RequestBody final GetAllRememberersRequestDto getAllRememberersRequest) {
		logger.info("Get all  rememberers request is received." + getAllRememberersRequest);
		GetAllRememberersResponseDto responseDto = new GetAllRememberersResponseDto();
		try {
			validateRequest(getAllRememberersRequest);
			List<Rememberer> allRememberers = remembererManager.getAllModelList();
			List<RemembererDto> allRememberersAsDto = remembererManager.createRemembererDtoList(allRememberers);
			responseDto.setResponseStatus(ResponseStatus.OK);
			responseDto.setAllRememberers(allRememberersAsDto);
			logger.info("Get all  rememberers request is OK." + getAllRememberersRequest);
		} catch (AbstractServiceException ase) {
			logger.info("Exception during get all rememberer request." + getAllRememberersRequest);
			setMeaningfulException(responseDto, ase);
		}
		return responseDto;
	}
}
