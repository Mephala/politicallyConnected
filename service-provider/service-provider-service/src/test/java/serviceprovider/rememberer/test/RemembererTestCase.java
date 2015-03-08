package serviceprovider.rememberer.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import mockit.Deencapsulation;
import mockit.integration.junit4.JMockit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import service.provider.common.core.ResponseStatus;
import service.provider.common.dto.RemembererDto;
import service.provider.common.exception.InvalidRequestException;
import service.provider.common.request.DeleteRemembererRequestDto;
import service.provider.common.request.GetAllRememberersRequestDto;
import service.provider.common.request.RequestDtoFactory;
import service.provider.common.request.SaveRemembererRequestDto;
import service.provider.common.response.DeleteRemembererResponseDto;
import service.provider.common.response.GetAllRememberersResponseDto;
import service.provider.common.response.SaveRemembererResponseDto;
import serviceprovider.controller.RemembererController;
import serviceprovider.dao.RemembererDAO;
import serviceprovider.manager.RemembererManager;
import serviceprovider.model.Rememberer;

@RunWith(JMockit.class)
public class RemembererTestCase {

	private RemembererManager remembererManager;

	@Before
	public void initializeTest() {
		remembererManager = new RemembererManager();
		RemembererDAO remembererDAO = new RemembererDAO();
		Deencapsulation.setField(remembererManager, remembererDAO);
		remembererManager.initialize();
	}

	@Test
	public void testCreatingRemembererFromDto() throws InvalidRequestException {
		Rememberer rememberer = remembererManager.createOrFindRememberer(null);
		RemembererDto remembererDto = new RemembererDto();
		String key = "abc";
		String value = "xyz";
		remembererDto.setKey(key);
		rememberer = remembererManager.createOrFindRememberer(remembererDto);
		assertTrue("fieldset failure", rememberer.getKey().equals(key));
		remembererDto.setValue(value);
		rememberer = remembererManager.createOrFindRememberer(remembererDto);
		assertTrue("fieldset failure", rememberer.getKey().equals(key));
		assertTrue("fieldset failure", rememberer.getValue().equals(value));
		key = "üüğ.iç";
		value = "üğüiüçüiçü";
		remembererDto.setKey(key);
		rememberer = remembererManager.createOrFindRememberer(remembererDto);
		assertTrue("fieldset failure", rememberer.getKey().equals(key));
		remembererDto.setValue(value);
		rememberer = remembererManager.createOrFindRememberer(remembererDto);
		assertTrue("fieldset failure", rememberer.getKey().equals(key));
		assertTrue("fieldset failure", rememberer.getValue().equals(value));
	}

	@Test
	public void testCreatingRemembererDto() {
		Rememberer rememberer = new Rememberer();
		RemembererDto remembererDto = remembererManager.createRemembererDto(null);
		assertTrue(remembererDto == null);
		remembererDto = remembererManager.createRemembererDto(rememberer);
		assertTrue(remembererDto.getKey() == null);
		assertTrue(remembererDto.getValue() == null);
		String key = "abc";
		String value = "xyz";
		Random random = new Random();
		Long id = random.nextLong();
		rememberer.setKey(key);
		rememberer.setValue(value);
		Deencapsulation.setField(rememberer, "id", id);
		remembererDto = remembererManager.createRemembererDto(rememberer);
		assertTrue(remembererDto.getKey().equals(key));
		assertTrue(remembererDto.getValue().equals(value));
		assertTrue(remembererDto.getId().equals(id));
		key = "üüğ.iç";
		value = "üğüiüçüiçü";
		id = random.nextLong();
		rememberer.setKey(key);
		rememberer.setValue(value);
		Deencapsulation.setField(rememberer, "id", id);
		remembererDto = remembererManager.createRemembererDto(rememberer);
		assertTrue(remembererDto.getKey().equals(key));
		assertTrue(remembererDto.getValue().equals(value));
		assertTrue(remembererDto.getId().equals(id));
	}

	@Test
	public void testCreatingMassRemembererDto() {
		List<Rememberer> remembererList = new ArrayList<Rememberer>();
		Map<String, Rememberer> remembererMap = new HashMap<String, Rememberer>();
		Map<Long, Rememberer> remembererIdMap = new HashMap<Long, Rememberer>();
		Random random = new Random();
		for (int i = 0; i < 100; i++) {
			String key = UUID.randomUUID().toString();
			String value = UUID.randomUUID().toString();
			Long id = random.nextLong();
			Rememberer rememberer = new Rememberer();
			rememberer.setKey(key);
			rememberer.setValue(value);
			Deencapsulation.setField(rememberer, "id", id);
			remembererList.add(rememberer);
			remembererMap.put(key, rememberer);
			remembererMap.put(value, rememberer);
			remembererIdMap.put(id, rememberer);
		}
		List<RemembererDto> remembererDtoList = remembererManager.createRemembererDtoList(remembererList);
		for (RemembererDto remembererDto : remembererDtoList) {
			String key = remembererDto.getKey();
			String value = remembererDto.getValue();
			Rememberer keyRem = remembererMap.get(key);
			Rememberer valueRem = remembererMap.get(value);
			assertTrue(keyRem.equals(valueRem));
		}
	}

	@Test
	public void testRemembererIdAddition() {
		for (long i = 0; i < 1000; i++) {
			RemembererDto remembererWithId = new RemembererDto();
			remembererWithId.setId(i);
			Rememberer r = remembererManager.getSavedRememberer(remembererWithId);
			if (r != null) {
				assertTrue(r.getId().equals(Long.valueOf(i)));
			}
		}
	}

	@Test
	public void testSaveRemembererControllerResponse() {
		RemembererDto savedRememberer = generateRandomSavedRemembererDto();
		assertTrue(savedRememberer.getId() != null);
	}

	private RemembererDto generateRandomSavedRemembererDto() {
		SaveRemembererRequestDto saveRemReq = RequestDtoFactory.createSaveRemembererRequestDto();
		RemembererDto remDto = new RemembererDto();
		String key = UUID.randomUUID().toString();
		String value = UUID.randomUUID().toString();
		remDto.setKey(key);
		remDto.setValue(value);
		saveRemReq.setRemembererDto(remDto);
		RemembererController remController = injectRemembererControllerValues();
		SaveRemembererResponseDto response = (SaveRemembererResponseDto) remController.saveRememberer(null, null, saveRemReq);
		RemembererDto savedRememberer = response.getRemembererDto();
		assertTrue(savedRememberer.getKey().equals(key));
		assertTrue(savedRememberer.getValue().equals(value));
		return savedRememberer;
	}

	protected RemembererController injectRemembererControllerValues() {
		RemembererController remController = new RemembererController();
		Deencapsulation.setField(remController, remembererManager);
		return remController;
	}

	@Test
	public void saveDeleteGetAllRemembererDtoFunctionalityTest() {
		List<RemembererDto> allSavedRememberersBeforeTest = getAllSavedRememberersList();
		RemembererDto savedRememberer = generateRandomSavedRemembererDto();
		assertTrue(!allSavedRememberersBeforeTest.contains(savedRememberer));
		List<RemembererDto> allSavedRememberersAfterTest = getAllSavedRememberersList();
		assertTrue(allSavedRememberersAfterTest.contains(savedRememberer));
		DeleteRemembererRequestDto deleteRequest = RequestDtoFactory.createDeleteRemembererRequestDto();
		deleteRequest.setRemembererDto(savedRememberer);
		RemembererController remembererController = injectRemembererControllerValues();
		DeleteRemembererResponseDto deleteResponse = (DeleteRemembererResponseDto) remembererController.deleteRememberer(null, null, deleteRequest);
		assertTrue(ResponseStatus.OK.equals(deleteResponse.getResponseStatus()));
		List<RemembererDto> allSavedRememberersAfterDeletion = getAllSavedRememberersList();
		assertTrue(!allSavedRememberersAfterDeletion.contains(savedRememberer));
		assertTrue(allSavedRememberersAfterDeletion.equals(allSavedRememberersBeforeTest));
	}

	protected List<RemembererDto> getAllSavedRememberersList() {
		GetAllRememberersRequestDto getAllRememberers = RequestDtoFactory.createGetAllRemembererRequestDto();
		RemembererController remController = injectRemembererControllerValues();
		GetAllRememberersResponseDto getAllRememberersResponseDto = (GetAllRememberersResponseDto) remController.getAllRemembererList(null, null, getAllRememberers);
		List<RemembererDto> allSavedRememberers = getAllRememberersResponseDto.getAllRememberers();
		return allSavedRememberers;
	}
}
