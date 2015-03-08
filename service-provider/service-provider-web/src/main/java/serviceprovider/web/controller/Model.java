package serviceprovider.web.controller;

public enum Model {
	HOMEPAGE("home", "/homepage"), MAIN_AUTHENTICATION_PAGE("mainAuthentication", "/showAuthenticateUser");

	private String modelName;
	private String uri;

	private Model(String modelName, String uri) {
		this.modelName = modelName;
		this.uri = uri;
	}

	public String getModelName() {
		return modelName;
	}

	public String getUri() {
		return uri;
	}

}
