package serviceprovider.controller;

import service.provider.common.core.ResponseStatus;
import service.provider.common.dto.Validatable;
import service.provider.common.exception.AbstractServiceException;
import service.provider.common.exception.InvalidRequestException;
import service.provider.common.response.AbstractResponseDto;

public abstract class AbstractServiceController {

	protected void validateRequest(Validatable request) throws InvalidRequestException {
		if (request == null) {
			throw new InvalidRequestException();
		}
		if (!request.isValid()) {
			throw new InvalidRequestException();
		}
	}

	protected void setMeaningfulException(AbstractResponseDto responseDto, AbstractServiceException ase) {
		responseDto.setResponseStatus(ResponseStatus.EXCEPTION);
		responseDto.setError(ase.getMessage());
		responseDto.setServiceException(ase);
	}

}
