package serviceprovider.web.controller;

import org.springframework.web.servlet.ModelAndView;

import serviceprovider.web.Language;
import serviceprovider.web.property.PageWordingManager;
import serviceprovider.web.property.PropertyManager;

public abstract class BaseController {

	protected PropertyManager propertyManager = PropertyManager.getInstance();

	protected ModelAndView createModelAndView(Model modelInstance) {
		ModelAndView model = new ModelAndView(modelInstance.getModelName());
		PageWordingManager pageWordingManager = getPageManager(Language.TR);
		model.addObject("pageManager", pageWordingManager.convertToMap());
		return model;
	}

	protected ModelAndView finalizeControllerReturn(ModelAndView model) {
		return model;
	}

	protected String redirect(String redirectUri) {
		return "redirect:" + redirectUri;
	}

	protected PageWordingManager getPageManager(Language lan) {
		return propertyManager.getWordingManager(lan);
	}

	protected Object showStaticPage(Model staticModelName) {
		ModelAndView model = createModelAndView(staticModelName);
		return finalizeControllerReturn(model);
	}

}
