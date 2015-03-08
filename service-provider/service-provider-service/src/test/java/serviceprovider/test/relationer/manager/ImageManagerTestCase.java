package serviceprovider.test.relationer.manager;

import static org.junit.Assert.assertTrue;

import java.util.UUID;

import mockit.integration.junit4.JMockit;

import org.hibernate.Hibernate;
import org.junit.Test;
import org.junit.runner.RunWith;

import service.provider.common.exception.DatabaseCorruptedException;
import serviceprovider.Application;
import serviceprovider.relationer.manager.ImageManager;
import serviceprovider.relationer.model.DBImage;

@RunWith(JMockit.class)
public class ImageManagerTestCase {

	private ImageManager imageManager = ImageManager.getInstance();

	@Test
	public void testSavingImage() throws DatabaseCorruptedException {
		DBImage image = createAndSaveRandomDBImage();
		assertTrue(image.getId() != null);
		imageManager.deleteModel(image);
	}

	private DBImage createAndSaveRandomDBImage() {
		DBImage image = new DBImage();
		String testString = UUID.randomUUID().toString();
		byte[] testBytes = testString.getBytes();
		image.setImageBlob(Hibernate.createBlob(testBytes));
		image.setApplication(Application.WEB);
		imageManager.saveModel(image);
		return image;
	}

}
