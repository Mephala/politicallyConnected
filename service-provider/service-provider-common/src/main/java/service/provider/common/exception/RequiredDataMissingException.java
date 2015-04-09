package service.provider.common.exception;

public class RequiredDataMissingException extends AbstractServiceException {

	private final String[] missingData;

	public RequiredDataMissingException(String... missingData) {
		super("Required parameters are missing. Required parameters:" + missingData);
		this.missingData = missingData;
	}

	public String[] getMissingData() {
		return missingData;
	}

}
