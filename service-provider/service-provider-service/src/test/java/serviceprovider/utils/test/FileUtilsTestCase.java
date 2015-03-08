package serviceprovider.utils.test;

import static org.junit.Assert.assertTrue;
import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

import serviceprovider.util.FileUtils;

@RunWith(JMockit.class)
public class FileUtilsTestCase {

	private static final String VALID_FILE_NAME = "test.jnlp";
	private static final String INVALID_FILE_NAME = "makaroni";

	@Test
	public void testGettingInputStream() {
		assertTrue(FileUtils.getFileAsInputStream(VALID_FILE_NAME) != null);
	}

	@Test
	public void testNotGettingInputStream() {
		assertTrue(FileUtils.getFileAsInputStream(INVALID_FILE_NAME) == null);
	}
}
