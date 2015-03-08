package serviceprovider.manager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.provider.common.dto.ConfigurationDto;
import serviceprovider.Application;
import serviceprovider.dao.ConfigurationDAO;
import serviceprovider.model.Configuration;

@Service
public class ConfigurationManager extends AbstractServiceManager<Configuration> {

	Log logger = LogFactory.getLog(getClass());
	@Autowired
	private ConfigurationDAO configurationDAO;

	@PostConstruct
	public void initialize() {
		initializeDAO(configurationDAO);
	}

	public List<Configuration> findConfigurationByApplication(Application app) {
		return configurationDAO.findConfigurationByApplication(app);
	}

	public List<ConfigurationDto> createConfigurationDtoList(List<Configuration> allConfigurations) {
		List<ConfigurationDto> convertedConfigurations = new ArrayList<>();
		for (Configuration c : allConfigurations) {
			convertedConfigurations.add(createConfigurationDtoFromConfiguration(c));
		}
		return convertedConfigurations;
	}

	public ConfigurationDto createConfigurationDtoFromConfiguration(Configuration c) {
		return new ConfigurationDto(c.getConfigurationKey(), c.getId(), c.getConfigurationValue());
	}

	public Configuration createConfiguration(ConfigurationDto configurationDto, Application convertedApplication) {
		Configuration conf = new Configuration();
		conf.setConfigurationApp(convertedApplication);
		conf.setConfigurationKey(configurationDto.getKey());
		conf.setConfigurationValue(configurationDto.getValue());
		return conf;
	}

}
