package service.provider.common.response;

import service.provider.common.core.ResponseStatus;
import service.provider.common.exception.AbstractServiceException;

public class AbstractResponseDto {

	private ResponseStatus responseStatus;
	private String error;
	private AbstractServiceException serviceException;

	public AbstractResponseDto() {

	}

	public ResponseStatus getResponseStatus() {
		return responseStatus;
	}

	public String getError() {
		return error;
	}

	public void setResponseStatus(ResponseStatus responseStatus) {
		this.responseStatus = responseStatus;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Exception getServiceException() {
		return serviceException;
	}

	public void setServiceException(AbstractServiceException serviceException) {
		this.serviceException = serviceException;
	}

	@Override
	public String toString() {
		return "AbstractResponseDto [responseStatus=" + responseStatus + ", error=" + error + ", serviceException=" + serviceException + "]";
	}

}
