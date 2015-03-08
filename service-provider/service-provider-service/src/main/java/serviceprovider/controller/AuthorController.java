package serviceprovider.controller;

import java.util.Set;

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
import service.provider.common.dto.AuthorDto;
import service.provider.common.exception.AbstractServiceException;
import service.provider.common.request.GetAuthorsRequestDto;
import service.provider.common.response.GetAllAuthorsResponseDto;
import serviceprovider.authorFetcher.AuthorManager;

@Controller
public class AuthorController extends AbstractServiceController {

	private AuthorManager authorManager = AuthorManager.getInstance();
	private final Log logger = LogFactory.getLog(getClass());

	@RequestMapping(value = "/getAllAuthors.do", method = RequestMethod.POST)
	@ResponseBody
	public Object getAllAuthors(HttpServletRequest request, HttpServletResponse response, @RequestBody final GetAuthorsRequestDto getAllAuthorsRequest) {
		logger.info("Getting all authors for request:" + getAllAuthorsRequest);
		GetAllAuthorsResponseDto responseDto = new GetAllAuthorsResponseDto();
		try {
			validateRequest(getAllAuthorsRequest);
			Set<AuthorDto> authorDtoSet = authorManager.getAuthors();
			responseDto.setAuthors(authorDtoSet);
			responseDto.setResponseStatus(ResponseStatus.OK);
			logger.info("Authors are serialized and responded upon request.");
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
