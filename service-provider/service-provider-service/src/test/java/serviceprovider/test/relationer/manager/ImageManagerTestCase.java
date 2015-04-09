package serviceprovider.test.relationer.manager;

import static org.junit.Assert.assertTrue;

import java.awt.Image;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.UUID;

import javax.imageio.ImageIO;

import mockit.Deencapsulation;
import mockit.integration.junit4.JMockit;

import org.apache.axis.utils.StringUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.junit.runner.RunWith;

import service.provider.common.dto.ImageDto;
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

	@Test
	public void testSerializingLena() throws UnsupportedEncodingException, SQLException, DatabaseCorruptedException {
		DBImage image = createAndSaveRandomDBImage();
		assertTrue(image.getId() != null);
		String serializedStrings = Deencapsulation.invoke(imageManager, "serializeImage", image);
		assertTrue(serializedStrings.length() > 0);
		imageManager.deleteModel(image);
	}

	@Test
	public void testSavingLena() throws IOException, SQLException {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("lena.jpg");
		assertTrue(is != null);
		Image image = ImageIO.read(is);
		assertTrue(image != null);
		DBImage dbImageLena = new DBImage();
		dbImageLena.setApplication(Application.SERVICE);
		File lenaFile = new File(this.getClass().getClassLoader().getResource("lena.jpg").getPath());
		Blob lenaFileBlob = Hibernate.createBlob(FileUtils.readFileToByteArray(lenaFile));
		dbImageLena.setImageBlob(lenaFileBlob);
		String lenaAsString = Deencapsulation.invoke(imageManager, "serializeImage", dbImageLena);
		File createdLenaFromString = new File("recreatedLena.jpg");
		byte[] decodedBytes = Base64.decodeBase64(lenaAsString.getBytes());
		OutputStream out = new BufferedOutputStream(new FileOutputStream("recreatedLena.jpg"));
		out.write(decodedBytes);
		out.close();
		assertTrue(createdLenaFromString.length() > 0);
		boolean deleteRecreatedLena = new File("recreatedLena.jpg").delete();
		assertTrue(deleteRecreatedLena);
	}

	@Test
	public void testCreatingImageDto() throws IOException, SQLException, DatabaseCorruptedException {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("lena.jpg");
		assertTrue(is != null);
		Image image = ImageIO.read(is);
		assertTrue(image != null);
		DBImage dbImageLena = new DBImage();
		dbImageLena.setApplication(Application.SERVICE);
		File lenaFile = new File(this.getClass().getClassLoader().getResource("lena.jpg").getPath());
		Blob lenaFileBlob = Hibernate.createBlob(FileUtils.readFileToByteArray(lenaFile));
		dbImageLena.setImageBlob(lenaFileBlob);
		imageManager.saveModel(dbImageLena);
		Long id = dbImageLena.getId();
		ImageDto imageDto = imageManager.createImageDto(id);
		assertTrue(!StringUtils.isEmpty(imageDto.getEncodedData()));
		assertTrue(imageDto.getId().equals(dbImageLena.getId()));
		imageManager.deleteModel(dbImageLena);
	}
}
