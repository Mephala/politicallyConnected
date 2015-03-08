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
import service.provider.common.dto.ConfigurationDto;
import service.provider.common.exception.AbstractServiceException;
import service.provider.common.request.GetAllConfigurationRequestDto;
import service.provider.common.request.SaveConfigurationRequestDto;
import service.provider.common.response.GetAllConfigurationResponseDto;
import service.provider.common.response.SaveConfigurationResponseDto;
import serviceprovider.Application;
import serviceprovider.manager.ConfigurationManager;
import serviceprovider.model.Configuration;
import serviceprovider.util.RequestToObjectConverter;

@Controller
public class ConfigurationController extends AbstractServiceController {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private ConfigurationManager configurationManager;

	@RequestMapping(value = "/getAllConfigurations.do", method = RequestMethod.POST)
	@ResponseBody
	public Object getAllConfigurations(HttpServletRequest request, HttpServletResponse response, @RequestBody GetAllConfigurationRequestDto getAllConfigurationsRequestDto) {
		logger.info("Get all  configuration request is received." + getAllConfigurationsRequestDto);
		GetAllConfigurationResponseDto responseDto = new GetAllConfigurationResponseDto();
		try {
			validateRequest(getAllConfigurationsRequestDto);
			Application convertedApplication = RequestToObjectConverter.converDtoApplicationToServiceApplication(getAllConfigurationsRequestDto.getRequestApp());
			List<Configuration> allConfigurations = configurationManager.findConfigurationByApplication(convertedApplication);
			List<ConfigurationDto> allConfigurationDtos = configurationManager.createConfigurationDtoList(allConfigurations);
			responseDto.setResponseStatus(ResponseStatus.OK);
			responseDto.setAllConfigurations(allConfigurationDtos);
			logger.info("Get all  configuration request is OK." + getAllConfigurationsRequestDto);
		} catch (AbstractServiceException ase) {
			logger.info("Exception during get all configurations request." + getAllConfigurationsRequestDto);
			setMeaningfulException(responseDto, ase);
		}
		return responseDto;
	}

	@RequestMapping(value = "/saveConfiguration.do", method = RequestMethod.POST)
	@ResponseBody
	public Object saveConfiguration(HttpServletRequest request, HttpServletResponse response, @RequestBody SaveConfigurationRequestDto saveConfigurationRequest) {
		logger.info("Save configuration request is received." + saveConfigurationRequest);
		SaveConfigurationResponseDto responseDto = new SaveConfigurationResponseDto();
		try {
			validateRequest(saveConfigurationRequest);
			Application convertedApplication = RequestToObjectConverter.converDtoApplicationToServiceApplication(saveConfigurationRequest.getRequestApp());
			Configuration configToSave = configurationManager.createConfiguration(saveConfigurationRequest.getConfigurationDto(), convertedApplication);
			configurationManager.saveModel(configToSave);
			responseDto.setResponseStatus(ResponseStatus.OK);
			responseDto.setConfigurationDto(configurationManager.createConfigurationDtoFromConfiguration(configToSave));
			logger.info("Get all  configuration request is OK." + saveConfigurationRequest);
		} catch (AbstractServiceException ase) {
			logger.info("Exception during get all configurations request." + saveConfigurationRequest);
			setMeaningfulException(responseDto, ase);
		}
		return responseDto;
	}
}
