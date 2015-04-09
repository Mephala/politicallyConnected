package serviceprovider.service.address;

import static org.junit.Assert.assertTrue;
import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class AdressServiceTestCase {

	@Test
	public void testInitializingExcel() {
		AddressService as = AddressService.getInstance();
		assertTrue(as != null);
	}

}
