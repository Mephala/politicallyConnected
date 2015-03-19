package serviceprovider.test.relationer.manager;

import static org.junit.Assert.assertTrue;

import java.awt.Image;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import mockit.MockUp;
import mockit.integration.junit4.JMockit;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.util.CollectionUtils;

import service.provider.common.core.RequestApplication;
import service.provider.common.core.ResponseStatus;
import service.provider.common.exception.DatabaseCorruptedException;
import service.provider.common.request.GetAllImageIdsRequestDto;
import service.provider.common.request.GetImageRequestDto;
import service.provider.common.request.RequestDtoFactory;
import service.provider.common.response.GetAllImageIdsResponseDto;
import service.provider.common.response.GetImageResponseDto;
import serviceprovider.Application;
import serviceprovider.controller.ImageController;
import serviceprovider.relationer.manager.ImageManager;
import serviceprovider.relationer.model.DBImage;

@RunWith(JMockit.class)
public class ImageControllerTestCase {

	@Test
	public void testGettingAllImageIds() throws DatabaseCorruptedException {
		ImageManager imageManager = ImageManager.getInstance();
		cleanUpDBBeforeTest(imageManager);
		List<Long> createdImageIds = createDummyImagesAtDB();
		ImageController imageController = new ImageController();
		GetAllImageIdsRequestDto getAllImagesRequest = RequestDtoFactory.createGetAllImageIdsRequestDto(RequestApplication.WEB);
		new MockUp<TestRequest>() {
		};
		new MockUp<TestResponse>() {
		};
		HttpServletRequest request = new TestRequest();
		HttpServletResponse response = new TestResponse();
		GetAllImageIdsResponseDto getAllImagesResponseDto = imageController.getAllImageIds(request, response, getAllImagesRequest);
		assertTrue(getAllImagesResponseDto.getResponseStatus().equals(ResponseStatus.OK));
		List<Long> responseIds = getAllImagesResponseDto.getImageIds();
		assertTrue(responseIds.size() == createdImageIds.size());
		for (Long responseId : responseIds) {
			boolean found = false;
			for (Long createdId : createdImageIds) {
				if (createdId.equals(responseId)) {
					found = true;
					break;
				}
			}
			assertTrue(found);
		}
	}

	private List<Long> createDummyImagesAtDB() {
		List<Long> createdImageIds = new ArrayList<Long>();
		for (int i = 0; i < 100; i++) {
			DBImage image = new DBImage();
			image.setApplication(Application.WEB);
			String testString = UUID.randomUUID().toString();
			image.setImageBlob(Hibernate.createBlob(testString.getBytes()));
			ImageManager.getInstance().saveModel(image);
			createdImageIds.add(image.getId());
		}
		return createdImageIds;
	}

	private void cleanUpDBBeforeTest(ImageManager imageManager) throws DatabaseCorruptedException {
		List<DBImage> imageList = imageManager.getAllModelList(RequestApplication.WEB);
		if (!CollectionUtils.isEmpty(imageList)) {
			for (DBImage dbImage : imageList) {
				imageManager.deleteModel(dbImage);
			}
		}
	}

	@Test
	public void testGettingImageData() throws IOException {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("lena.jpg");
		assertTrue(is != null);
		Image image = ImageIO.read(is);
		assertTrue(image != null);
		DBImage dbImageLena = new DBImage();
		dbImageLena.setApplication(Application.SERVICE);
		File lenaFile = new File(this.getClass().getClassLoader().getResource("lena.jpg").getPath());
		Blob lenaFileBlob = Hibernate.createBlob(FileUtils.readFileToByteArray(lenaFile));
		dbImageLena.setImageBlob(lenaFileBlob);
		ImageManager imageManager = ImageManager.getInstance();
		imageManager.saveModel(dbImageLena);
		GetImageRequestDto getImageRequest = RequestDtoFactory.createGetImageRequest(RequestApplication.WEB);
		getImageRequest.setImageId(dbImageLena.getId());
		new MockUp<TestRequest>() {
		};
		new MockUp<TestResponse>() {
		};
		HttpServletRequest request = new TestRequest();
		HttpServletResponse response = new TestResponse();
		ImageController imageController = new ImageController();
		GetImageResponseDto getImageResponse = imageController.getImage(request, response, getImageRequest);
		String lenaAsStringEncoded = getImageResponse.getImageDto().getEncodedData();
		File createdLenaFromString = new File("recreatedLena.jpg");
		byte[] decodedBytes = Base64.decodeBase64(lenaAsStringEncoded.getBytes());
		OutputStream out = new BufferedOutputStream(new FileOutputStream("recreatedLena.jpg"));
		out.write(decodedBytes);
		out.close();
		assertTrue(createdLenaFromString.length() > 0);
		int result = JOptionPane.showConfirmDialog(null, "Image olustu mu ?", "Test kontrolu", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
		assertTrue(result == 0);
		boolean deleteRecreatedLena = new File("recreatedLena.jpg").delete();
		assertTrue(deleteRecreatedLena);
	}
}
