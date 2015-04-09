package serviceprovider.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import serviceprovider.web.authentication.AuthenticationManager;
import serviceprovider.web.authentication.AuthenticationResult;
import serviceprovider.web.form.AuthenticateUserForm;

@Controller
public class AuthenticationController extends BaseController {

	private AuthenticationManager authenticationManager = AuthenticationManager.getInstance();

	@RequestMapping(value = "/showAuthenticateUser")
	public Object showAuthenticateUser(HttpServletRequest request, HttpServletResponse response) {
		return showStaticPage(Model.MAIN_AUTHENTICATION_PAGE);
	}

	@RequestMapping(value = "/authenticateUser", method = RequestMethod.POST)
	public Object authenticateUser(HttpServletRequest request, HttpServletResponse response, @ModelAttribute AuthenticateUserForm authenticaUserForm) {
		AuthenticationResult ar = authenticationManager.authenticate(request, authenticaUserForm);
		if (ar.redirectNeeded())
			return redirect(ar.redirectUri());
		else
			return redirect(Model.HOMEPAGE.getUri());
	}

}
