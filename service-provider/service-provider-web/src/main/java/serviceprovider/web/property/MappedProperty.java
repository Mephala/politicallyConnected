package serviceprovider.web.property;

public interface MappedProperty {

	public String getProperty(String key);

	public String getPropery(String key, String defaultValue);

	public boolean hasKey(String key);

}
