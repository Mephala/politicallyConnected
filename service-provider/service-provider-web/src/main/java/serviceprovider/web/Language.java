package serviceprovider.web;

public enum Language {
	EN("english"), TR("turkish");

	private String languageName;

	private Language(String languageName) {
		this.languageName = languageName;
	}

	public String getLanguageName() {
		return languageName;
	}

}
