package serviceprovider.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import serviceprovider.Application;

@Entity
public class Configuration {

	@Id
	@GeneratedValue
	private Long id;

	@Column(length = 128, nullable = false)
	private String configurationKey;

	@Column(length = 2048, nullable = false)
	private String configurationValue;

	@Column(nullable = false)
	private Application configurationApp;

	public String getConfigurationKey() {
		return configurationKey;
	}

	public void setConfigurationKey(String configurationKey) {
		this.configurationKey = configurationKey;
	}

	public String getConfigurationValue() {
		return configurationValue;
	}

	public void setConfigurationValue(String configurationValue) {
		this.configurationValue = configurationValue;
	}

	public Application getConfigurationApp() {
		return configurationApp;
	}

	public void setConfigurationApp(Application configurationApp) {
		this.configurationApp = configurationApp;
	}

	public Long getId() {
		return id;
	}

}
