package serviceprovider.web.property;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import serviceprovider.web.Language;

public class PropertyManager {
	private static PropertyManager instance;
	private Map<StaticProperty, MappedPropertyInstance> cachedMappedInstances;
	private final Log logger = LogFactory.getLog(getClass());

	private PropertyManager() {
		cachedMappedInstances = new HashMap<StaticProperty, MappedPropertyInstance>();
	}

	public static synchronized PropertyManager getInstance() {
		if (instance == null)
			instance = new PropertyManager();
		return instance;
	}

	public PageWordingManager getWordingManager(Language language) {
		PageWordingManager wordingManager = null;
		switch (language) {
		case EN:
			wordingManager = attainWordingManager(StaticProperty.ENGLISH_PAGE_MANAGER);
			break;
		case TR:
			wordingManager = attainWordingManager(StaticProperty.TURKISH_PAGE_MANAGER);
			break;
		}
		return wordingManager;
	}

	/**
	 * Search cached page manager, returns if found, caches if not.
	 */
	private PageWordingManager attainWordingManager(StaticProperty staticProperty) {
		PageWordingManager manager = cachedMappedInstances.get(staticProperty);
		if (manager == null) {
			MappedPropertyInstance mappedInstance = createMappedInstance(staticProperty);
			cachedMappedInstances.put(staticProperty, mappedInstance);
			manager = mappedInstance;
		}
		return manager;
	}

	private MappedPropertyInstance createMappedInstance(StaticProperty staticProperty) {
		Properties parsedProperties = new Properties();
		try {
			parsedProperties.load(getClass().getClassLoader().getResourceAsStream(staticProperty.getFileName()));
		} catch (IOException e) {
			logger.error("IO exception received during property mapping.", e);
		}
		return new MappedPropertyInstance(parsedProperties);
	}

}
