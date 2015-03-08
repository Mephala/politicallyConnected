package serviceprovider.web.property;

import java.util.Map;

public interface PageWordingManager {

	public String getWording(String key);

	public Map<String, String> convertToMap();

}
