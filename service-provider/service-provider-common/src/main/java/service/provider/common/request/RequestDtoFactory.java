package service.provider.common.request;

import service.provider.common.core.RequestApplication;

public class RequestDtoFactory {

	public static SavePcaPersonListRequestDto createSavePcaPersonListRequestDto(RequestApplication application) {
		SavePcaPersonListRequestDto request = new SavePcaPersonListRequestDto(RequestDtoConstants.SAVE_PCA_PERSON_LIST_REQUEST_URI);
		request.setRequestApp(application);
		return request;
	}

	public static GetAllCitiesRequestDto createGetAllCitiesRequestDto(RequestApplication application) {
		GetAllCitiesRequestDto request = new GetAllCitiesRequestDto(RequestDtoConstants.GET_ALL_CITIES_REQUEST_URI);
		request.setRequestApp(application);
		return request;
	}

	public static SaveConfigurationRequestDto createSaveConfigurationRequestDto(RequestApplication application) {
		SaveConfigurationRequestDto request = new SaveConfigurationRequestDto(RequestDtoConstants.SAVE_CONFIGURATION_REQUEST_URI);
		request.setRequestApp(application);
		return request;
	}

	public static GetAllConfigurationRequestDto createGetAllConfigurationRequest(RequestApplication application) {
		GetAllConfigurationRequestDto request = new GetAllConfigurationRequestDto(RequestDtoConstants.GET_ALL_CONFIGURATIONS_REQUEST_URI);
		request.setRequestApp(application);
		return request;
	}

	public static SaveCategoryRequestDto createSaveCategoryRequestDto() {
		SaveCategoryRequestDto request = new SaveCategoryRequestDto(RequestDtoConstants.SAVE_CATEGORY_URI);
		return request;
	}

	public static GetAuthorsRequestDto createGetAuthorsRequest() {
		GetAuthorsRequestDto request = new GetAuthorsRequestDto(RequestDtoConstants.GET_AUTHORS_URI);
		return request;
	}

	public static DeleteSchedulerRequestDto createDeleteSchedulerRequestDto() {
		DeleteSchedulerRequestDto request = new DeleteSchedulerRequestDto(RequestDtoConstants.DELETE_SCHEDULER_URI);
		return request;
	}

	public static GetAllSchedulersRequestDto createGetAllSchedulersRequestDto() {
		GetAllSchedulersRequestDto request = new GetAllSchedulersRequestDto(RequestDtoConstants.GET_ALL_SCHEDULERS_REQUEST_DTO);
		return request;
	}

	public static SaveSchedulerRequestDto createSaveSchedulerRequestDto() {
		SaveSchedulerRequestDto request = new SaveSchedulerRequestDto(RequestDtoConstants.SAVE_SCHEDULER_REQUEST_URI);
		return request;
	}

	public static SaveUserRequestDto createSaveUserRequestDto() {
		SaveUserRequestDto request = new SaveUserRequestDto(RequestDtoConstants.SAVE_USER_URI);
		return request;
	}

	public static LoginUserRequestDto createLoginUserRequestDto() {
		LoginUserRequestDto request = new LoginUserRequestDto(RequestDtoConstants.LOGIN_USER_URI);
		return request;
	}

	public static GetAllProvidersRequestDto createGetAllProvidersRequestDto() {
		GetAllProvidersRequestDto request = new GetAllProvidersRequestDto(RequestDtoConstants.GET_ALL_PROVIDERS_URI);
		return request;
	}

	public static SaveProviderRequestDto createSaveProviderRequestDto() {
		SaveProviderRequestDto request = new SaveProviderRequestDto(RequestDtoConstants.SAVE_PROVIDER_REQUEST_URI);
		return request;
	}

	public static GetCategoryRequestDto createGetCategoryReqeustDto() {
		GetCategoryRequestDto request = new GetCategoryRequestDto(RequestDtoConstants.GET_CATEGORY_REQUEST_URI);
		request.setWithoutProviders(true);
		return request;
	}

	public static GetAllCategoryIdsRequestDto createGetAllCategoryIdsRequestDto() {
		GetAllCategoryIdsRequestDto request = new GetAllCategoryIdsRequestDto(RequestDtoConstants.GET_ALL_CATEGORY_IDS_REQUESTS_URI);
		return request;
	}

	public static SaveRemembererRequestDto createSaveRemembererRequestDto() {
		SaveRemembererRequestDto request = new SaveRemembererRequestDto(RequestDtoConstants.SAVE_REMEMBERER_URI);
		return request;
	}

	public static GetAllRememberersRequestDto createGetAllRemembererRequestDto() {
		GetAllRememberersRequestDto request = new GetAllRememberersRequestDto(RequestDtoConstants.GET_ALL_REMEMBERER_LIST_URI);
		return request;
	}

	public static DeleteRemembererRequestDto createDeleteRemembererRequestDto() {
		DeleteRemembererRequestDto request = new DeleteRemembererRequestDto(RequestDtoConstants.DELETE_REMEMBERER_URI);
		return request;
	}

	public static GetAllImageIdsRequestDto createGetAllImageIdsRequestDto(RequestApplication application) {
		GetAllImageIdsRequestDto request = new GetAllImageIdsRequestDto(RequestDtoConstants.GET_ALL_IMAGE_IDS_URI);
		request.setRequestApp(application);
		return request;
	}

	public static GetImageRequestDto createGetImageRequest(RequestApplication application) {
		GetImageRequestDto request = new GetImageRequestDto(RequestDtoConstants.GET_IMAGE_URI);
		request.setRequestApp(application);
		return request;
	}

}
