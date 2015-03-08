package serviceprovider.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import service.provider.common.core.ResponseStatus;
import service.provider.common.dto.UserDto;
import service.provider.common.exception.AbstractServiceException;
import service.provider.common.exception.AuthenticationFailureException;
import service.provider.common.exception.CredentialsAlreadyExistsException;
import service.provider.common.request.LoginUserRequestDto;
import service.provider.common.request.SaveUserRequestDto;
import service.provider.common.response.LoginUserResponseDto;
import service.provider.common.response.SaveUserResponseDto;
import serviceprovider.manager.UserManager;
import serviceprovider.model.User;
import serviceprovider.util.ModelToDtoConverter;
import serviceprovider.util.RequestToObjectConverter;

@Controller
public class UserController extends AbstractServiceController {

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private UserManager userManager;

	@RequestMapping(value = "/saveUser.do", method = RequestMethod.POST)
	@ResponseBody
	public Object saveUser(HttpServletRequest request, HttpServletResponse response, @RequestBody final SaveUserRequestDto saveUserRequest) {
		logger.info("saveUser request received. Request:" + saveUserRequest);
		SaveUserResponseDto responseDto = new SaveUserResponseDto();
		try {
			validateRequest(saveUserRequest);
			User user = RequestToObjectConverter.convertUser(saveUserRequest);
			// FIXME Email uniqueness saglanmali
			// Checking if someone has this password & email combination.
			User registeredUser = userManager.getUserByPasswordAndEmail(user);
			if (registeredUser != null) {
				throw new CredentialsAlreadyExistsException();
			}
			userManager.saveModel(user);
			responseDto.setResponseStatus(ResponseStatus.OK);
			return responseDto;
		} catch (AbstractServiceException ase) {
			logger.info("saveUser request encountered serviceException. Exception:" + ase);
			responseDto.setResponseStatus(ResponseStatus.EXCEPTION);
			responseDto.setServiceException(ase);
			responseDto.setError(ase.getMessage());
		} catch (Exception ex) {
			logger.info("saveUser request received error. Exception :" + ex);
			responseDto.setResponseStatus(ResponseStatus.ERROR);
			responseDto.setError(ex.getMessage());
		} finally {
			logger.info("saveUser method has been completed. Request:" + saveUserRequest);
		}
		return responseDto;
	}

	@RequestMapping(value = "/loginUser.do", method = RequestMethod.POST)
	@ResponseBody
	public Object loginUser(HttpServletRequest request, HttpServletResponse response, @RequestBody final LoginUserRequestDto loginUserRequest) {
		logger.info("loginUser request received. Request:" + loginUserRequest);
		LoginUserResponseDto responseDto = new LoginUserResponseDto();
		try {
			validateRequest(loginUserRequest);
			User user = RequestToObjectConverter.convertUser(loginUserRequest);
			User registeredUser = userManager.getUserByPasswordAndEmail(user);
			if (registeredUser == null) {
				// Invalid login parameters.
				logger.info("loginUser credentials are failed." + loginUserRequest);
				throw new AuthenticationFailureException();
			}
			UserDto userDto = ModelToDtoConverter.createUserDto(registeredUser);
			responseDto.setUserDto(userDto);
			responseDto.setResponseStatus(ResponseStatus.OK);
			return responseDto;
		} catch (AbstractServiceException ase) {
			logger.info("loginUser request encountered serviceException. Exception:" + ase);
			responseDto.setResponseStatus(ResponseStatus.EXCEPTION);
			responseDto.setServiceException(ase);
			responseDto.setError(ase.getMessage());
		} catch (Exception ex) {
			logger.info("loginUser request received error. Exception :" + ex);
			responseDto.setResponseStatus(ResponseStatus.ERROR);
			responseDto.setError(ex.getMessage());
		} finally {
			logger.info("loginUser method has been completed. Request:" + loginUserRequest);
		}
		return responseDto;
	}

	/**
	 * This method is a work-around solution to a db problem. Connection resets itself frequently. Running an sql request every 2 minutes.
	 * 
	 */
	@Scheduled(fixedDelay = 60 * 1000 * 2)
	public void keepDbusy() {
		try {
			logger.info("Pinging database...");
			User user = new User();
			user.setEmail("aa");
			user.setPassword("bb");
			userManager.getUserByPasswordAndEmail(user);
		} catch (Exception e) {
			logger.info("Database Ping failed...");
		}
		logger.info("Database Ping success...");
	}
}
