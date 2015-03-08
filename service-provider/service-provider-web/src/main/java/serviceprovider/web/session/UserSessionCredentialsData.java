package serviceprovider.web.session;

class UserSessionCredentialsData implements UniqueUserSessionData {

	private final String ip;
	private final String sessionId;

	@Override
	public String getIp() {
		return ip;
	}

	@Override
	public String getSessionId() {
		return sessionId;
	}

	protected UserSessionCredentialsData(String ip, String sessionId) {
		super();
		this.ip = ip;
		this.sessionId = sessionId;
	}

}
