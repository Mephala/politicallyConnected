package service.provider.client.executor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;

import org.apache.commons.codec.binary.Base64;

import service.provider.common.core.RequestApplication;
import service.provider.common.dto.CategoryDto;
import service.provider.common.dto.CityDto;
import service.provider.common.dto.ConfigurationDto;
import service.provider.common.dto.ProviderDto;
import service.provider.common.dto.RemembererDto;
import service.provider.common.dto.SchedulerDto;
import service.provider.common.request.DeleteRemembererRequestDto;
import service.provider.common.request.DeleteSchedulerRequestDto;
import service.provider.common.request.GetAllCategoryIdsRequestDto;
import service.provider.common.request.GetAllConfigurationRequestDto;
import service.provider.common.request.GetAllImageIdsRequestDto;
import service.provider.common.request.GetAllProvidersRequestDto;
import service.provider.common.request.GetAllRememberersRequestDto;
import service.provider.common.request.GetAllSchedulersRequestDto;
import service.provider.common.request.GetAuthorsRequestDto;
import service.provider.common.request.GetCategoryRequestDto;
import service.provider.common.request.GetImageRequestDto;
import service.provider.common.request.LoginUserRequestDto;
import service.provider.common.request.RequestDtoFactory;
import service.provider.common.request.SaveCategoryRequestDto;
import service.provider.common.request.SaveConfigurationRequestDto;
import service.provider.common.request.SaveProviderRequestDto;
import service.provider.common.request.SaveRemembererRequestDto;
import service.provider.common.request.SaveSchedulerRequestDto;
import service.provider.common.request.SaveUserRequestDto;
import service.provider.common.response.DeleteRemembererResponseDto;
import service.provider.common.response.DeleteSchedulerResponseDto;
import service.provider.common.response.GetAllAuthorsResponseDto;
import service.provider.common.response.GetAllCategoryIdsResponseDto;
import service.provider.common.response.GetAllCitiesResponseDto;
import service.provider.common.response.GetAllImageIdsResponseDto;
import service.provider.common.response.GetAllProvidersResponseDto;
import service.provider.common.response.GetAllRememberersResponseDto;
import service.provider.common.response.GetAllSchedulersResponseDto;
import service.provider.common.response.GetCategoryResponseDto;
import service.provider.common.response.GetImageResponseDto;
import service.provider.common.response.LoginUserResponseDto;
import service.provider.common.response.SaveProviderResponseDto;
import service.provider.common.response.SaveSchedulerResponseDto;
import service.provider.common.response.SaveUserResponseDto;

public class TestClient {

	public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException, IOException, InvalidKeyException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, ClassNotFoundException {
		ServiceClient.initialize("http://localhost:8080/");
		// testUserSaveAndLogin();
		// ProviderTests();
		// categoryTest();
		// categoryChildhoodTest();
		// utf8Test();
		// testRememberer();
		// testBasicCategoryProcesses();
		// testSchedulerAdd();
		// testGettingAllSchedulers();
		// deleteScheduler();
		// testGettingAllAuthorsDto();
		// testGettingConfigurations();
		// cryptoTest();
		// citiesTest();
		testGettingImage();
	}

	private static void testGettingImage() throws IOException {
		GetAllImageIdsRequestDto getAllImageIds = RequestDtoFactory.createGetAllImageIdsRequestDto(RequestApplication.WEB);
		GetAllImageIdsResponseDto getAllImageResponse = ServiceClient.getAllImageIds(getAllImageIds);
		List<Long> ids = getAllImageResponse.getImageIds();
		for (Long imageId : ids) {
			GetImageRequestDto req = RequestDtoFactory.createGetImageRequest(RequestApplication.WEB);
			GetImageResponseDto resp = ServiceClient.getImage(req);
			byte[] decodedBytes = Base64.decodeBase64(resp.getImageDto().getEncodedData().getBytes());
			OutputStream out = new BufferedOutputStream(new FileOutputStream("recreatedLena.jpg"));
			out.write(decodedBytes);
			out.close();
			int result = JOptionPane.showConfirmDialog(null, "Image olustu mu ?", "Test kontrolu", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			boolean deleteRecreatedLena = new File("recreatedLena.jpg").delete();
		}

	}

	private static void citiesTest() {
		GetAllCitiesResponseDto response = ServiceClient.getAllCities(RequestDtoFactory.createGetAllCitiesRequestDto(RequestApplication.WEB));
		List<CityDto> cities = response.getCityList();
		for (CityDto cityDto : cities) {
			System.out.println(cityDto.getName());
		}

	}

	private static void cryptoTest() throws NoSuchAlgorithmException, IOException, FileNotFoundException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, ClassNotFoundException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		String gokhan = "Prosege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdiniProsege!!! prostagmaaann....Gökhan Özgözen .`11`2`1213213321 `3`13`13`13124214124.. Hanasinin hamina attiririm sikerim nezdini.";
		System.out.println(gokhan);
		byte[] digestion = md.digest(gokhan.getBytes());
		Base64 base64 = new Base64();
		String sie = base64.encodeAsString(digestion);
		System.err.println(sie);
		Key key = null;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("SecretKey.ser"));
			key = (Key) in.readObject();
			in.close();
		} catch (FileNotFoundException fnfe) {
			KeyGenerator generator = KeyGenerator.getInstance("DES");
			generator.init(new SecureRandom());
			key = generator.generateKey();
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("SecretKey.ser"));
			out.writeObject(key);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] stringBytes = gokhan.getBytes("UTF8");
		byte[] raw = cipher.doFinal(stringBytes);
		String encryptedMessage = base64.encodeToString(raw);
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decodedEncryption = base64.decode(encryptedMessage);
		byte[] decryptedStuff = cipher.doFinal(decodedEncryption);
		String decryptedMessage = new String(decryptedStuff, "UTF8");
		System.err.println("Original message :" + gokhan);
		System.err.println("Encrypted message:" + encryptedMessage);
		System.err.println("Decrypted message:" + decryptedMessage);
	}

	private static void testGettingConfigurations() {
		SaveConfigurationRequestDto saveConReq = RequestDtoFactory.createSaveConfigurationRequestDto(RequestApplication.WEB);
		ConfigurationDto confDto = new ConfigurationDto("test", null, "test");
		saveConReq.setConfigurationDto(confDto);
		System.out.println(ServiceClient.saveConfiguration(saveConReq));
		GetAllConfigurationRequestDto getAllConfigRequest = RequestDtoFactory.createGetAllConfigurationRequest(RequestApplication.WEB);
		System.out.println(ServiceClient.getAllConfigurations(getAllConfigRequest));
	}

	private static void testGettingAllAuthorsDto() {
		GetAuthorsRequestDto request = RequestDtoFactory.createGetAuthorsRequest();
		System.out.println(request);
		GetAllAuthorsResponseDto response = ServiceClient.getAllAuthors(request);
		// System.out.println(response);
	}

	private static void deleteScheduler() {
		GetAllSchedulersRequestDto request = RequestDtoFactory.createGetAllSchedulersRequestDto();
		GetAllSchedulersResponseDto response = ServiceClient.getAllSchedulers(request);
		Set<SchedulerDto> schedulers = response.getAllSchedulers();
		for (SchedulerDto schedulerDto : schedulers) {
			DeleteSchedulerRequestDto deleteReq = RequestDtoFactory.createDeleteSchedulerRequestDto();
			deleteReq.setSchedulerId(schedulerDto.getId());
			DeleteSchedulerResponseDto deleteResponse = ServiceClient.deleteScheduler(deleteReq);
			System.out.println(deleteResponse);
		}

	}

	private static void testGettingAllSchedulers() {
		GetAllSchedulersRequestDto request = RequestDtoFactory.createGetAllSchedulersRequestDto();
		GetAllSchedulersResponseDto response = ServiceClient.getAllSchedulers(request);
		System.out.println(response);

	}

	private static void testSchedulerAdd() {
		ServiceClient.initialize("http://localhost:8080");
		SchedulerDto schedulerDto = new SchedulerDto();
		SaveSchedulerRequestDto request = RequestDtoFactory.createSaveSchedulerRequestDto();
		schedulerDto.setDate(new Date());
		schedulerDto.setPriority(3);
		schedulerDto.setNote("Test");
		schedulerDto.setDone(Boolean.FALSE);
		request.setSchedulerDto(schedulerDto);
		SaveSchedulerResponseDto response = ServiceClient.saveScheduler(request);
		System.out.println(response);

	}

	private static void testBasicCategoryProcesses() {
		SaveCategoryRequestDto saveCategory = RequestDtoFactory.createSaveCategoryRequestDto();
		saveCategory.setCategoryName("GokhanDeneme");
		GetCategoryResponseDto saveCategoryResponse = ServiceClient.saveCategory(saveCategory);
		System.out.println(saveCategoryResponse.getCategoryDto());
		SaveCategoryRequestDto changeCategoryNameRequest = RequestDtoFactory.createSaveCategoryRequestDto();
		changeCategoryNameRequest.setCategoryId(saveCategoryResponse.getCategoryDto().getId());
		changeCategoryNameRequest.setCategoryName("MustafaTest");
		GetCategoryResponseDto changeCategoryNameResponse = ServiceClient.saveCategory(saveCategory);
		System.out.println(changeCategoryNameResponse.getCategoryDto());
		SaveCategoryRequestDto createParentRequest = RequestDtoFactory.createSaveCategoryRequestDto();
		createParentRequest.setCategoryName("GokhanDeneme");
		GetCategoryResponseDto createParentResponse = ServiceClient.saveCategory(createParentRequest);
		System.out.println(createParentResponse.getCategoryDto());
		SaveCategoryRequestDto setParentRequest = RequestDtoFactory.createSaveCategoryRequestDto();
		setParentRequest.setCategoryId((saveCategoryResponse.getCategoryDto().getId()));
		setParentRequest.setCategoryName(saveCategoryResponse.getCategoryDto().getName());
		setParentRequest.setParentCategoryId(createParentResponse.getCategoryDto().getId());
		GetCategoryResponseDto setParentResponse = ServiceClient.saveCategory(setParentRequest);
		System.out.println(setParentResponse.getCategoryDto());

	}

	private static void testRememberer() {
		SaveRemembererRequestDto saveRemReq = RequestDtoFactory.createSaveRemembererRequestDto();
		saveRemReq.setRemembererDto(new RemembererDto("deneme", "denettirme"));
		System.out.println(ServiceClient.saveRememberer(saveRemReq));
		GetAllRememberersRequestDto req = RequestDtoFactory.createGetAllRemembererRequestDto();
		GetAllRememberersResponseDto res = ServiceClient.getAllRemembererList(req);
		System.out.println(res);
		DeleteRemembererRequestDto deleteReq = RequestDtoFactory.createDeleteRemembererRequestDto();
		RemembererDto rDto = new RemembererDto();
		rDto.setId(5l);
		deleteReq.setRemembererDto(rDto);
		DeleteRemembererResponseDto deleteResponse = ServiceClient.deleteRememberer(deleteReq);
		System.out.println(deleteResponse);
		saveRemReq = RequestDtoFactory.createSaveRemembererRequestDto();
		RemembererDto editedRemembererDto = res.getAllRememberers().get(0);
		editedRemembererDto.setKey("Edited!");
		editedRemembererDto.setValue("Edited!");
		saveRemReq.setRemembererDto(editedRemembererDto);
		System.out.println(ServiceClient.saveRememberer(saveRemReq));

	}

	private static void utf8Test() {
		SaveCategoryRequestDto saveCategoryRequest = RequestDtoFactory.createSaveCategoryRequestDto();
		saveCategoryRequest.setCategoryName("ğüişçşiö");
		saveCategoryRequest.setPriority(8);
		GetCategoryResponseDto responsivasyon = ServiceClient.saveCategory(saveCategoryRequest);

	}

	private static void categoryChildhoodTest() {
		GetCategoryRequestDto getCat = RequestDtoFactory.createGetCategoryReqeustDto();
		getCat.setId(140l);
		CategoryDto categorim = ServiceClient.getCategory(getCat).getCategoryDto();
		SaveCategoryRequestDto saveCategoryRequest = RequestDtoFactory.createSaveCategoryRequestDto();
		saveCategoryRequest.setParentCategoryId(null);
		List<Long> childIds = new ArrayList<Long>();
		childIds.add((long) 141);
		for (long i = 142; i < 161; i++) {
			childIds.add(i);
		}
		saveCategoryRequest.setCategoryName("Daha dun bir koseden baktim maziye");
		saveCategoryRequest.setChildCategoryIds(childIds);
		GetCategoryResponseDto responsivasyon3 = ServiceClient.saveCategory(saveCategoryRequest);
		System.out.println(ServiceClient.convertToJson(saveCategoryRequest));
		System.out.println(responsivasyon3);

	}

	private static void categoryTest() {
		GetCategoryRequestDto getCategoryRequest = RequestDtoFactory.createGetCategoryReqeustDto();
		getCategoryRequest.setId(55l);
		GetCategoryResponseDto responsive = ServiceClient.getCategory(getCategoryRequest);
		System.out.println(ServiceClient.convertToJson(getCategoryRequest));
		System.out.println(responsive);
		GetAllCategoryIdsRequestDto getAllIdsReq = RequestDtoFactory.createGetAllCategoryIdsRequestDto();
		GetAllCategoryIdsResponseDto responsyon = ServiceClient.getAllCategoryIds(getAllIdsReq);
		System.out.println(ServiceClient.convertToJson(getAllIdsReq));
		System.out.println(responsyon);
		for (Long catId : responsyon.getCategoryIds()) {
			getCategoryRequest = RequestDtoFactory.createGetCategoryReqeustDto();
			getCategoryRequest.setId(catId);
			ServiceClient.getCategory(getCategoryRequest);
			getCategoryRequest.setWithoutProviders(false);
			GetCategoryResponseDto response = ServiceClient.getCategory(getCategoryRequest);
			CategoryDto responseCategory = response.getCategoryDto();
			System.out.println(responseCategory);
		}
		SaveCategoryRequestDto saveCategoryRequest = RequestDtoFactory.createSaveCategoryRequestDto();
		saveCategoryRequest.setCategoryName("Test1");
		saveCategoryRequest.setPriority(8);
		GetCategoryResponseDto responsivasyon = ServiceClient.saveCategory(saveCategoryRequest);
		CategoryDto parentCat = responsivasyon.getCategoryDto();
		System.out.println(ServiceClient.convertToJson(saveCategoryRequest));
		System.out.println(responsivasyon);
		SaveCategoryRequestDto saveCategoryRequest2 = RequestDtoFactory.createSaveCategoryRequestDto();
		saveCategoryRequest2.setCategoryName("Mevzubahis");
		saveCategoryRequest2.setPriority(8);
		saveCategoryRequest2.setParentCategoryId(parentCat.getId());
		GetCategoryResponseDto responsivasyon2 = ServiceClient.saveCategory(saveCategoryRequest2);
		System.out.println(ServiceClient.convertToJson(saveCategoryRequest2));
		System.out.println(responsivasyon2);
		CategoryDto parent3 = responsivasyon.getCategoryDto();
		CategoryDto child3 = responsivasyon2.getCategoryDto();
		SaveCategoryRequestDto saveCategoryRequest3 = RequestDtoFactory.createSaveCategoryRequestDto();
		saveCategoryRequest3.setCategoryName("Ara oglan");
		saveCategoryRequest3.setPriority(8);
		saveCategoryRequest3.setParentCategoryId(parentCat.getId());
		List<Long> childIds = new ArrayList<Long>();
		childIds.add(child3.getId());
		saveCategoryRequest3.setChildCategoryIds(childIds);
		saveCategoryRequest3.setParentCategoryId(parent3.getId());
		GetCategoryResponseDto responsivasyon3 = ServiceClient.saveCategory(saveCategoryRequest3);
		System.out.println(ServiceClient.convertToJson(saveCategoryRequest3));
		System.out.println(responsivasyon3);

	}

	protected static void ProviderTests() {
		saveProvider("325532352", "5443543543", "Busra Yasam Koclugu");
		saveProvider("125215215", "111111111", "Kabadayı Ustra Kemal");
		saveProvider("123231231", "2546646464", "Murat Berber");
		saveProvider("125116", "262324", "PVC ci Şenol");
		saveProvider("457457", "532523", "Ustabaşı Hıdır");
		saveProvider("47232352", "423272", "Mahalle Kaşarı Yeliz");
		saveProvider("21442121", "324236234", "Sucuk Ustası Hamit");
		saveProvider("21321541", "62345234", "Kuaför Şükrü");
		saveProvider("74484", "3242342", "Overlokçu Peyami");
		saveProvider("2515", "26262", "Operatör Doktor Talat Pekdemir");
		saveProvider("21566464", "32523", "Helal Dana Kesimevi");
		GetAllProvidersRequestDto getAllProvidersRequestDto = RequestDtoFactory.createGetAllProvidersRequestDto();
		GetAllProvidersResponseDto getAllResponse = ServiceClient.getAllProviders(getAllProvidersRequestDto);
		System.out.println(getAllResponse);

	}

	protected static void saveProvider(String gsm, String tckn, String title) {
		SaveProviderRequestDto saveProviderRequest = RequestDtoFactory.createSaveProviderRequestDto();
		ProviderDto providerDto = new ProviderDto();
		// Zorunlu alanlar
		providerDto.setGsm(gsm);
		providerDto.setTckn(tckn);
		providerDto.setTitle(title);
		saveProviderRequest.setProviderDto(providerDto);
		SaveProviderResponseDto response = ServiceClient.saveProvider(saveProviderRequest);
		System.out.println(response);
	}

	protected static void testUserSaveAndLogin() {
		SaveUserRequestDto request = RequestDtoFactory.createSaveUserRequestDto();
		String email = "pervaz";
		String password = "s";
		request.setEmail(email);
		request.setFirstname("muzort");
		request.setLastname("Derdesik");
		request.setUsername("piyu");
		request.setPassword(password);
		SaveUserResponseDto response = ServiceClient.saveUser(request);
		System.out.println(response);
		loginTrial("sadasd", "sdasadsda");
		loginTrial("sadasdafs", "asddsadasdsa");
		loginTrial("sadasdafs", "asddsadasdsa");
		loginTrial("sadas421241421dafs", "asddsadasds122133a");
		loginTrial(password, email);
	}

	protected static void loginTrial(String password, String email) {
		LoginUserRequestDto loginRequest = RequestDtoFactory.createLoginUserRequestDto();
		loginRequest.setEmail(email);
		loginRequest.setPassword(password);
		LoginUserResponseDto loginResponse = ServiceClient.loginUser(loginRequest);
		System.out.println(loginResponse);
	}

}
