package serviceprovider.web.property;

enum StaticProperty {

	// Page Managers
	TURKISH_PAGE_MANAGER("turkishWordings.properties"), ENGLISH_PAGE_MANAGER("englishWordings.properties");

	private String fileName;

	protected String getFileName() {
		return this.fileName;
	}

	private StaticProperty(String fileName) {
		this.fileName = fileName;
	}

}
