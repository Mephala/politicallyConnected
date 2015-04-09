package service.provider.common.dto;

import java.util.Map;

import service.provider.common.core.RequestApplication;

public abstract class AbstractRequestDto implements Validatable {
	private String requestUri;
	private RequestApplication requestApp;
	private Map<String, String> metaData;

	public void setMetaData(Map<String, String> metaData) {
		this.metaData = metaData;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public Map<String, String> getMetaData() {
		return metaData;
	}

	public RequestApplication getRequestApp() {
		return requestApp;
	}

	public void setRequestApp(RequestApplication requestApp) {
		this.requestApp = requestApp;
	}

	@Override
	public String toString() {
		return "AbstractRequestDto [requestUri=" + requestUri + ", requestApp=" + requestApp + ", metaData=" + metaData + "]";
	}

}
