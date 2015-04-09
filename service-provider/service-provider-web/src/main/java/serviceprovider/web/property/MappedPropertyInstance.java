package serviceprovider.web.property;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.axis.utils.StringUtils;

class MappedPropertyInstance implements MappedProperty, PageWordingManager {
	private Properties props;
	private Map<String, String> convertedMap;

	protected MappedPropertyInstance(Properties props) {
		this.props = props;
		convertedMap = new HashMap<>();
		Set<Object> keys = props.keySet();
		for (Object key : keys) {
			convertedMap.put((String) key, props.getProperty((String) key));
		}
	}

	@Override
	public String getProperty(String key) {
		return props.getProperty(key);
	}

	@Override
	public String getPropery(String key, String defaultValue) {
		String retVal = props.getProperty(key);
		if (StringUtils.isEmpty(retVal))
			retVal = defaultValue;
		return retVal;
	}

	@Override
	public boolean hasKey(String key) {
		return props.get(key) != null;
	}

	@Override
	public String getWording(String key) {
		return getProperty(key);
	}

	@Override
	public Map<String, String> convertToMap() {
		return this.convertedMap;
	}

}
