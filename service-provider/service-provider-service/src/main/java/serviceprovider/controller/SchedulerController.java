package serviceprovider.controller;

import java.util.List;
import java.util.Set;

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
import service.provider.common.dto.SchedulerDto;
import service.provider.common.exception.AbstractServiceException;
import service.provider.common.exception.ModelNotFoundException;
import service.provider.common.request.DeleteSchedulerRequestDto;
import service.provider.common.request.GetAllSchedulersRequestDto;
import service.provider.common.request.SaveSchedulerRequestDto;
import service.provider.common.response.DeleteSchedulerResponseDto;
import service.provider.common.response.GetAllSchedulersResponseDto;
import service.provider.common.response.SaveSchedulerResponseDto;
import serviceprovider.manager.SchedulerManager;
import serviceprovider.model.Scheduler;

@Controller
public class SchedulerController extends AbstractServiceController {

	private Log logger = LogFactory.getLog(getClass());

	@Autowired
	private SchedulerManager schedulerManager;

	@RequestMapping(value = "/saveScheduler.do", method = RequestMethod.POST)
	@ResponseBody
	public Object saveScheduler(HttpServletRequest request, HttpServletResponse response, @RequestBody final SaveSchedulerRequestDto saveSchedulerRequest) {
		logger.info("Save scheduler request is received." + saveSchedulerRequest);
		SaveSchedulerResponseDto responseDto = new SaveSchedulerResponseDto();
		try {
			validateRequest(saveSchedulerRequest);
			Scheduler scheduler = schedulerManager.createOrFindRememberer(saveSchedulerRequest.getSchedulerDto());
			schedulerManager.saveModel(scheduler);
			SchedulerDto savedScheduledDto = schedulerManager.createSchedulerDto(scheduler);
			logger.info("Save rememberer request is OK." + saveSchedulerRequest);
			responseDto.setResponseStatus(ResponseStatus.OK);
			responseDto.setSchedulerDto(savedScheduledDto);
		} catch (AbstractServiceException ase) {
			logger.info("Exception during save rememberer request." + saveSchedulerRequest);
			setMeaningfulException(responseDto, ase);
		}
		return responseDto;
	}

	@RequestMapping(value = "/getAllSchedulers.do", method = RequestMethod.POST)
	@ResponseBody
	public Object getAllSchedulers(HttpServletRequest request, HttpServletResponse response, @RequestBody final GetAllSchedulersRequestDto getAllSchedulersRequest) {
		logger.info("Get all schedulers request is received." + getAllSchedulersRequest);
		GetAllSchedulersResponseDto responseDto = new GetAllSchedulersResponseDto();
		try {
			validateRequest(getAllSchedulersRequest);
			List<Scheduler> allSchedulers = schedulerManager.getAllModelList();
			Set<SchedulerDto> allSchedulerDtos = schedulerManager.createSchedulerDtoSet(allSchedulers);
			responseDto.setResponseStatus(ResponseStatus.OK);
			responseDto.setAllSchedulers(allSchedulerDtos);
			logger.info("Get all schedulers request is OK." + getAllSchedulersRequest + " returning :" + responseDto);
		} catch (AbstractServiceException ase) {
			logger.info("Exception during save rememberer request." + getAllSchedulersRequest);
			setMeaningfulException(responseDto, ase);
		}
		return responseDto;
	}

	@RequestMapping(value = "/deleteScheduler.do", method = RequestMethod.POST)
	@ResponseBody
	public Object deleteScheduler(HttpServletRequest request, HttpServletResponse response, @RequestBody final DeleteSchedulerRequestDto deleteSchedulerRequestDto) {
		logger.info("Delete scheduler request is received." + deleteSchedulerRequestDto);
		DeleteSchedulerResponseDto responseDto = new DeleteSchedulerResponseDto();
		try {
			validateRequest(deleteSchedulerRequestDto);
			Long id = deleteSchedulerRequestDto.getSchedulerId();
			Scheduler scheduler = schedulerManager.findModelById(id);
			if (scheduler == null)
				throw new ModelNotFoundException("Scheduler not found in the database with given id");
			schedulerManager.deleteModel(scheduler);
			responseDto.setResponseStatus(ResponseStatus.OK);
			logger.info("Delete schedulerrequest is OK." + deleteSchedulerRequestDto + " returning :" + responseDto);
		} catch (AbstractServiceException ase) {
			logger.info("Exception during save rememberer request." + deleteSchedulerRequestDto);
			setMeaningfulException(responseDto, ase);
		}
		return responseDto;
	}

}
