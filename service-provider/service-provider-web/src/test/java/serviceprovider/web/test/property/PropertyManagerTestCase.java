package serviceprovider.web.test.property;

import static org.junit.Assert.assertTrue;
import mockit.integration.junit4.JMockit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import serviceprovider.web.Language;
import serviceprovider.web.property.PageWordingManager;
import serviceprovider.web.property.PropertyManager;

@RunWith(JMockit.class)
public class PropertyManagerTestCase {

	PropertyManager pm;

	@Before
	public void initialize() {
		pm = PropertyManager.getInstance();
	}

	@Test
	public void testGettingPageManager() {
		PageWordingManager tr_manager = pm.getWordingManager(Language.TR);
		PageWordingManager en_manager = pm.getWordingManager(Language.EN);
		assertTrue("test".equals(en_manager.getWording("test")));
		assertTrue("test".equals(tr_manager.getWording("test")));
	}
}
