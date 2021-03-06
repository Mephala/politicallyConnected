package serviceprovider.controller;

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
import service.provider.common.exception.AbstractServiceException;
import service.provider.common.request.GetAllPcaDataRequestDto;
import service.provider.common.request.SavePcaPersonListRequestDto;
import service.provider.common.response.GetAllPcaDataResponseDto;
import service.provider.common.response.SavePcaPersonListResponseDto;
import serviceprovider.manager.PcaManager;

@Controller
public class PcaController extends AbstractServiceController {

	private final Log logger = LogFactory.getLog(getClass());
	private final PcaManager pcaManager = PcaManager.getInstance();

	@RequestMapping(value = "/savePcaPersonList.do", method = RequestMethod.POST, produces = "application/json; charset=utf-8", consumes = "application/json; charset=utf-8")
	@ResponseBody
	public Object savePcaPersonList(HttpServletRequest request, HttpServletResponse response, @RequestBody final SavePcaPersonListRequestDto savePersonListRequest) {
		logger.info("Saving pca persons upon request:" + savePersonListRequest);
		SavePcaPersonListResponseDto responseDto = new SavePcaPersonListResponseDto();
		try {
			validateRequest(savePersonListRequest);
			pcaManager.savePcaListAsync(savePersonListRequest.getPersonListToSave());
			logger.info("Pca persons are qued for saving successfully.");
			responseDto.setResponseStatus(ResponseStatus.OK);
		} catch (AbstractServiceException ase) {
			logger.error("request  encountered serviceException. Exception:" + ase, ase);
			setMeaningfulException(responseDto, ase);
		} catch (Exception e) {
			logger.error("request  encountered serviceException. Exception:" + e, e);
			responseDto.setError("Error occured");
			responseDto.setResponseStatus(ResponseStatus.ERROR);
		}
		return responseDto;
	}

	@RequestMapping(value = "/getAllPcaListDto.do", method = RequestMethod.POST, produces = "application/json; charset=utf-8", consumes = "application/json; charset=utf-8")
	@ResponseBody
	public Object getAllPcaDto(HttpServletRequest request, HttpServletResponse response, @RequestBody final GetAllPcaDataRequestDto getAllPcaDataRequest) {
		logger.info("Getting all pca data and serializing it upon request:" + getAllPcaDataRequest);
		GetAllPcaDataResponseDto responseDto = new GetAllPcaDataResponseDto();
		try {
			validateRequest(getAllPcaDataRequest);
			responseDto.setAllPersonDtoList(pcaManager.covertDbDataToDto());
			logger.info("Pca persons are converted to dto successfully.");
			responseDto.setResponseStatus(ResponseStatus.OK);
		} catch (AbstractServiceException ase) {
			logger.error("request  encountered serviceException. Exception:" + ase, ase);
			setMeaningfulException(responseDto, ase);
		} catch (Exception e) {
			logger.error("request  encountered serviceException. Exception:" + e, e);
			responseDto.setError("Error occured");
			responseDto.setResponseStatus(ResponseStatus.ERROR);
		}
		return responseDto;
	}

}
