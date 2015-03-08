package serviceprovider.web.test.configuration;

import static org.junit.Assert.assertTrue;
import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

import serviceprovider.web.configuration.ConfigurationManager;

@RunWith(JMockit.class)
public class ConfigurationTestCase {

	private ConfigurationManager configurationManager = ConfigurationManager.getInstance();

	@Test
	public void ensureDevModeIsActive() {
		assertTrue(configurationManager.isDevelopmentEnvironment());
	}

}
