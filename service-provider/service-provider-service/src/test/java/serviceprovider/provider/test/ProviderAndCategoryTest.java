package serviceprovider.provider.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import mockit.Deencapsulation;
import mockit.integration.junit4.JMockit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.util.CollectionUtils;

import service.provider.common.dto.CategoryDto;
import service.provider.common.dto.ProviderDto;
import service.provider.common.exception.DatabaseCorruptedException;
import service.provider.common.request.GetCategoryRequestDto;
import service.provider.common.request.RequestDtoFactory;
import service.provider.common.request.SaveCategoryRequestDto;
import service.provider.common.response.GetCategoryResponseDto;
import serviceprovider.controller.CategoryController;
import serviceprovider.dao.CategoryDao;
import serviceprovider.dao.ProviderDao;
import serviceprovider.manager.CategoryManager;
import serviceprovider.manager.ProviderManager;
import serviceprovider.model.Category;
import serviceprovider.model.Provider;
import serviceprovider.model.User;

@RunWith(JMockit.class)
public class ProviderAndCategoryTest {

	protected ProviderManager providerManager;
	protected CategoryManager categoryManager;
	protected CategoryController categoryController;

	@Before
	public void initialize() {
		ProviderDao providerDAO = new ProviderDao();
		providerManager = new ProviderManager();
		Deencapsulation.setField(providerManager, "providerDao", providerDAO);
		providerManager.initialize();
		CategoryDao categoryDao = new CategoryDao();
		categoryManager = new CategoryManager();
		Deencapsulation.setField(categoryManager, "categoryDao", categoryDao);
		categoryManager.initialize();
		Deencapsulation.setField(categoryManager, "providerManager", providerManager);
		categoryController = new CategoryController();
		Deencapsulation.setField(categoryController, "categoryManager", categoryManager);
	}

	@Test
	public void testCreatingProviderDtoFromNullProvider() {
		providerManager.createProviderDto(null);
	}

	@Test
	public void testCreatingProviderDtoFromEmptyProvider() {
		providerManager.createProviderDto(new Provider());
	}

	@Test
	public void testCreatingProviderDtoFromDummyProvider() throws DatabaseCorruptedException {
		Provider testProvider = createAndSaveRandomProvider();
		providerManager.saveModel(testProvider);
		List<Provider> allProvider = providerManager.getAllModelList();
		assertTrue(allProvider.contains(testProvider));
		providerManager.deleteModel(testProvider);
		allProvider = providerManager.getAllModelList();
		assertTrue(!allProvider.contains(testProvider));
	}

	protected Provider createRandomProvider() {
		Random random = new Random();
		int lattitute = random.nextInt();
		int longitute = random.nextInt();
		String gsm = UUID.randomUUID().toString();
		String staticPhone = UUID.randomUUID().toString();
		String tckn = UUID.randomUUID().toString().substring(0, 11);
		String title = UUID.randomUUID().toString();
		String email = UUID.randomUUID().toString();
		String firstname = UUID.randomUUID().toString();
		String lastname = UUID.randomUUID().toString();
		String password = UUID.randomUUID().toString();
		String username = UUID.randomUUID().toString();
		Provider testProvider = createProvider(random, gsm, lattitute, longitute, staticPhone, tckn, title, email, firstname, lastname, password, username);
		return testProvider;
	}

	protected Provider createAndSaveRandomProvider() throws DatabaseCorruptedException {
		Random random = new Random();
		String gsm = UUID.randomUUID().toString();
		int lattitute = random.nextInt();
		int longitute = random.nextInt();
		String staticPhone = UUID.randomUUID().toString();
		String tckn = UUID.randomUUID().toString().substring(0, 11);
		String title = UUID.randomUUID().toString();
		String email = UUID.randomUUID().toString();
		String firstname = UUID.randomUUID().toString();
		String lastname = UUID.randomUUID().toString();
		String password = UUID.randomUUID().toString();
		String username = UUID.randomUUID().toString();
		Provider testProvider = createProvider(random, gsm, lattitute, longitute, staticPhone, tckn, title, email, firstname, lastname, password, username);
		ProviderDto providerDto = providerManager.createProviderDto(testProvider);
		assertConversionValues(gsm, lattitute, longitute, staticPhone, tckn, title, providerDto);
		providerManager.saveModel(testProvider);
		providerDto = providerManager.createProviderDto(testProvider);
		Long testProviderId = testProvider.getId();
		assertTrue(providerDto.getId().equals(testProviderId));
		assertConversionValues(gsm, lattitute, longitute, staticPhone, tckn, title, providerDto);
		providerManager.deleteModel(testProvider);
		return testProvider;
	}

	protected Provider createProvider(Random random, String gsm, int lattitute, int longitute, String staticPhone, String tckn, String title, String email, String firstname, String lastname, String password, String username) {
		Provider testProvider = new Provider();
		Long id = random.nextLong();
		User testUser = new User();
		testUser.setEmail(email);
		testUser.setFirstname(firstname);
		testUser.setLastname(lastname);
		testUser.setPassword(password);
		testUser.setUsername(username);
		testProvider.setGsm(gsm);
		testProvider.setLattitute(new BigDecimal(lattitute));
		testProvider.setLongitute(new BigDecimal(longitute));
		testProvider.setStaticPhone(staticPhone);
		testProvider.setTckn(tckn);
		testProvider.setTitle(title);
		return testProvider;
	}

	protected void assertConversionValues(String gsm, int lattitute, int longitute, String staticPhone, String tckn, String title, ProviderDto providerDto) {
		assertTrue(providerDto.getGsm().equals(gsm));
		assertTrue(providerDto.getLattitute().equals(new BigDecimal(lattitute)));
		assertTrue(providerDto.getLongitute().equals(new BigDecimal(longitute)));
		assertTrue(providerDto.getStaticPhone().equals(staticPhone));
		assertTrue(providerDto.getTckn().equals(tckn));
		assertTrue(providerDto.getTitle().equals(title));
	}

	@Test
	public void testCreatingProviderUnderCategory() throws DatabaseCorruptedException {
		Category testCategory = createRandomTestCategoryWithOneProvider();
		categoryManager.saveModel(testCategory);
		List<Category> allCategories = categoryManager.getAllModelList();
		assertTrue(allCategories.contains(testCategory));
		categoryManager.deleteModel(testCategory);
		allCategories = categoryManager.getAllModelList();
		assertTrue(!allCategories.contains(testCategory));
	}

	@Test
	public void testCreatingMultipleProviderUnderCategory() throws DatabaseCorruptedException {
		Category testCategory = createRandomCategoryWithMultipleProviders();
		categoryManager.saveModel(testCategory);
		List<Category> allCategories = categoryManager.getAllModelList();
		assertTrue(allCategories.contains(testCategory));
		categoryManager.deleteModel(testCategory);
		allCategories = categoryManager.getAllModelList();
		assertTrue(!allCategories.contains(testCategory));
	}

	protected Category createRandomCategoryWithMultipleProviders() throws DatabaseCorruptedException {
		Category testCategory = createRandomTestCategoryWithOneProvider();
		Set<Provider> providers = testCategory.getProviders();
		for (int i = 0; i < 20; i++) {
			providers.add(createRandomProvider());
		}
		return testCategory;
	}

	@Test
	public void testConvertingCategoryDtoAndProviders() throws DatabaseCorruptedException {
		Category testCategory = createRandomCategoryWithMultipleProviders();
		categoryManager.saveModel(testCategory);
		List<Category> allCategories = categoryManager.getAllModelList();
		assertTrue(allCategories.contains(testCategory));
		categoryManager.deleteModel(testCategory);
		allCategories = categoryManager.getAllModelList();
		assertTrue(!allCategories.contains(testCategory));
		CategoryDto categoryDto = categoryManager.createCategoryDto(testCategory, false, false);
		assertTrue(categoryDto.getId().equals(testCategory.getId()));
		assertTrue(categoryDto.getName().equals(testCategory.getName()));
		assertTrue(categoryDto.getPriority().equals(testCategory.getPriority()));
		assertTrue(categoryDto.getProviders() != null);
	}

	@Test
	public void testGettingCategoryDto() throws DatabaseCorruptedException {
		Category testCategory = createRandomCategoryWithMultipleProviders();
		categoryManager.saveModel(testCategory);
		List<Category> allCategories = categoryManager.getAllModelList();
		assertTrue(allCategories.contains(testCategory));
		categoryManager.deleteModel(testCategory);
		allCategories = categoryManager.getAllModelList();
		assertTrue(!allCategories.contains(testCategory));
		CategoryDto categoryDto = categoryManager.createCategoryDto(testCategory, false, false);
		assertTrue(categoryDto.getId().equals(testCategory.getId()));
		assertTrue(categoryDto.getName().equals(testCategory.getName()));
		assertTrue(categoryDto.getPriority().equals(testCategory.getPriority()));
		assertTrue(categoryDto.getProviders() != null);
	}

	protected Category createRandomTestCategoryWithOneProvider() throws DatabaseCorruptedException {
		Provider testProvider = createRandomProvider();
		return createCategoryFromProvider(testProvider);
	}

	protected Category createTestCategoryWithOneProvider() throws DatabaseCorruptedException {
		Provider testProvider = createAndSaveRandomProvider();
		return createCategoryFromProvider(testProvider);
	}

	protected Category createCategoryFromProvider(Provider testProvider) {
		String name = UUID.randomUUID().toString();
		Integer priority = new Random().nextInt(10);
		Category testCategory = new Category();
		testCategory.setName(name);
		testCategory.setPriority(priority);
		Set<Provider> providerSet = new HashSet<>();
		providerSet.add(testProvider);
		testCategory.setProviders(providerSet);
		return testCategory;
	}

	@Test
	public void testCreatingCategoryDto() throws DatabaseCorruptedException {
		CategoryDto savedCategory = saveRandomlyGeneratedCategoryDtoFromController();
	}

	@Test
	public void testCreatingCategoryWithChildsDto() throws DatabaseCorruptedException {
		int size = categoryManager.getAllModelList().size();
		List<Long> childCategoryIds = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			CategoryDto savedCategory = saveRandomlyGeneratedCategoryDtoFromController();
			childCategoryIds.add(savedCategory.getId());
		}
		SaveCategoryRequestDto saveCategoryRequestDto = createRandomCategoryWithChildren(childCategoryIds);
		GetCategoryResponseDto saveCategoryResponse = (GetCategoryResponseDto) categoryController.saveCategory(null, null, saveCategoryRequestDto);
		int savedSize = categoryManager.getAllModelList().size();
		int expectedSize = size + 11;
		CategoryDto savedCategory = saveCategoryResponse.getCategoryDto();
		Set<CategoryDto> childCategories = savedCategory.getChildCategories();
		assertTrue(childCategories.size() == childCategoryIds.size());
		assertTrue(!CollectionUtils.isEmpty(childCategories));
		assertTrue(savedSize == expectedSize);
	}

	@Test
	public void testSavingSimpleCategoryAndGettingId() throws DatabaseCorruptedException {
		SaveCategoryRequestDto saveCategoryRequestDto = createRandomSaveCategoryRequestDto();
		GetCategoryResponseDto saveCategoryResponse = (GetCategoryResponseDto) categoryController.saveCategory(null, null, saveCategoryRequestDto);
		assertTrue(saveCategoryResponse.getCategoryDto().getId() != null);
	}

	@Test
	public void testCreatingParentCategory() throws DatabaseCorruptedException {
		List<Long> childCategoryIds = new ArrayList<>();
		CategoryDto parentCategory = createAndSaveRandomCategoryWithChildren(childCategoryIds);
		SaveCategoryRequestDto testRequest = createRandomSaveCategoryRequestDto();
		testRequest.setParentCategoryId(parentCategory.getId());
		testRequest.setChildCategoryIds(childCategoryIds);
		GetCategoryResponseDto saveMiddleCategoryResponse = (GetCategoryResponseDto) categoryController.saveCategory(null, null, testRequest);
		CategoryDto middleCategory = saveMiddleCategoryResponse.getCategoryDto();
		CategoryDto middlesParentCategory = middleCategory.getParentCategory();
		Set<CategoryDto> childCategories = middleCategory.getChildCategories();
		assertTrue(childCategories.size() == childCategoryIds.size());
		assertTrue(!CollectionUtils.isEmpty(childCategories));
		assertTrue(middlesParentCategory.equals(parentCategory));
	}

	@Test
	public void testCreatingMiddleCategory() throws DatabaseCorruptedException {
		List<Long> childCategoryIds = new ArrayList<>();
		List<Category> allCategories = categoryManager.getAllModelList();
		int sizeBefore = allCategories.size();
		CategoryDto parentCategory1 = createAndSaveRandomCategoryWithChildren(childCategoryIds);
		SaveCategoryRequestDto saveCategoryRequestDto2 = createRandomCategoryWithChildren(childCategoryIds);
		GetCategoryResponseDto saveCategoryResponse2 = (GetCategoryResponseDto) categoryController.saveCategory(null, null, saveCategoryRequestDto2);
		CategoryDto parentCategory2 = saveCategoryResponse2.getCategoryDto();
		assertTrue(!parentCategory1.equals(parentCategory2));
		Set<CategoryDto> childCategories1 = parentCategory1.getChildCategories();
		Set<CategoryDto> childCategories2 = parentCategory2.getChildCategories();
		assertTrue(childCategories1.equals(childCategories2));
		allCategories = categoryManager.getAllModelList();
		int sizeAfter = allCategories.size();
		assertTrue(sizeBefore + 12 == sizeAfter);
	}

	@Test
	public void testOrphaningCategory() throws DatabaseCorruptedException {
		List<Category> allCategories = categoryManager.getAllModelList();
		int sizeBefore = allCategories.size();
		List<Long> childCategoryIds = new ArrayList<>();
		CategoryDto parentCategory1 = createAndSaveRandomCategoryWithChildren(childCategoryIds);
		Set<CategoryDto> childCategories = parentCategory1.getChildCategories();
		int size = childCategories.size();
		Random r = new Random();
		int childIndex = r.nextInt(size);
		CategoryDto selectedChildToBeOrphaned = null;
		Iterator<CategoryDto> childIterator = childCategories.iterator();
		while (childIterator.hasNext()) {
			if (childIndex == 0) {
				selectedChildToBeOrphaned = childIterator.next();
				break;
			}
			childIndex--;
		}
		assertNotNull(selectedChildToBeOrphaned);
		assertNotNull(childCategories.contains(selectedChildToBeOrphaned));
		SaveCategoryRequestDto orphanRequest = RequestDtoFactory.createSaveCategoryRequestDto();
		orphanRequest.setCategoryId(selectedChildToBeOrphaned.getId());
		orphanRequest.setCategoryName(selectedChildToBeOrphaned.getName());
		GetCategoryResponseDto saveCategoryResponse = (GetCategoryResponseDto) categoryController.saveCategory(null, null, orphanRequest);
		CategoryDto expectedOrphanCategoryDto = saveCategoryResponse.getCategoryDto();
		assertTrue(expectedOrphanCategoryDto.equals(selectedChildToBeOrphaned));
		assertTrue(CollectionUtils.isEmpty(expectedOrphanCategoryDto.getChildCategories()));
		assertTrue(expectedOrphanCategoryDto.getParentCategory() == null);
		Long orphanCategoryId = expectedOrphanCategoryDto.getId();
		CategoryDto reFetchedOrphanCategoryDto = fetchCategoryFromId(orphanCategoryId);
		assertTrue(reFetchedOrphanCategoryDto.equals(selectedChildToBeOrphaned));
		assertTrue(CollectionUtils.isEmpty(expectedOrphanCategoryDto.getChildCategories()));
		assertTrue(expectedOrphanCategoryDto.getParentCategory() == null);
		GetCategoryRequestDto getParentCategoryRequest = RequestDtoFactory.createGetCategoryReqeustDto();
		getParentCategoryRequest.setId(parentCategory1.getId());
		GetCategoryResponseDto getParentCategoryResponse = (GetCategoryResponseDto) categoryController.getCategoryById(null, null, getParentCategoryRequest);
		CategoryDto reFetchedParentCategory = getParentCategoryResponse.getCategoryDto();
		Set<CategoryDto> reFetchedChildCategories = reFetchedParentCategory.getChildCategories();
		assertTrue(!reFetchedChildCategories.contains(reFetchedOrphanCategoryDto));
		assertTrue(reFetchedChildCategories.size() == size - 1);
		allCategories = categoryManager.getAllModelList();
		int sizeAfter = allCategories.size();
		assertTrue(sizeAfter == sizeBefore + 11);
	}

	@Test
	public void testChangingParentCategory() throws DatabaseCorruptedException {
		List<Category> allCategories = categoryManager.getAllModelList();
		int sizeBefore = allCategories.size();
		List<Long> childCategoryIds = new ArrayList<>();
		CategoryDto previousParentCategory = createAndSaveRandomCategoryWithChildren(childCategoryIds);
		Set<CategoryDto> childCategories = previousParentCategory.getChildCategories();
		int size = childCategories.size();
		Random r = new Random();
		int childIndex = r.nextInt(size);
		CategoryDto selectedChildToChangeParent = null;
		Iterator<CategoryDto> childIterator = childCategories.iterator();
		while (childIterator.hasNext()) {
			if (childIndex == 0) {
				selectedChildToChangeParent = childIterator.next();
				break;
			}
			childIndex--;
		}
		assertNotNull(selectedChildToChangeParent);
		assertNotNull(childCategories.contains(selectedChildToChangeParent));
		SaveCategoryRequestDto randomSaveCategoryRequest = createRandomSaveCategoryRequestDto();
		GetCategoryResponseDto randomSaveCategoryResponse = (GetCategoryResponseDto) categoryController.saveCategory(null, null, randomSaveCategoryRequest);
		CategoryDto nextParentCategory = randomSaveCategoryResponse.getCategoryDto();
		SaveCategoryRequestDto changeParentRequest = RequestDtoFactory.createSaveCategoryRequestDto();
		changeParentRequest.setCategoryId(selectedChildToChangeParent.getId());
		changeParentRequest.setParentCategoryId(nextParentCategory.getId());
		changeParentRequest.setCategoryName(selectedChildToChangeParent.getName());
		GetCategoryResponseDto changeParentCategoryResponse = (GetCategoryResponseDto) categoryController.saveCategory(null, null, changeParentRequest);
		assertTrue(changeParentCategoryResponse.getCategoryDto().getId().equals(selectedChildToChangeParent.getId()));
		Long previousParentCategoryId = previousParentCategory.getId();
		CategoryDto reFetchedPreviousParentCategoryDto = fetchCategoryFromId(previousParentCategoryId);
		CategoryDto reFetchedNextParentCategoryDto = fetchCategoryFromId(nextParentCategory.getId());
		CategoryDto reFetchedSelectedChildCategoryDto = fetchCategoryFromId(selectedChildToChangeParent.getId());
		Set<CategoryDto> childsOfPreviousParent = reFetchedPreviousParentCategoryDto.getChildCategories();
		Set<CategoryDto> childsOfNextParent = reFetchedNextParentCategoryDto.getChildCategories();
		allCategories = categoryManager.getAllModelList();
		int sizeAfter = allCategories.size();
		assertTrue(!reFetchedNextParentCategoryDto.equals(reFetchedPreviousParentCategoryDto));
		assertTrue(reFetchedNextParentCategoryDto.equals(reFetchedSelectedChildCategoryDto.getParentCategory()));
		assertNotNull(childsOfNextParent);
		assertNotNull(childsOfPreviousParent);
		assertTrue(!childsOfNextParent.equals(childsOfPreviousParent));
		assertTrue(childsOfPreviousParent.size() == 9);
		assertTrue(childsOfNextParent.size() == 1);
		assertTrue(childsOfNextParent.contains(reFetchedSelectedChildCategoryDto));
		assertTrue(!childsOfPreviousParent.contains(reFetchedSelectedChildCategoryDto));
		assertTrue((sizeAfter - 12) == sizeBefore);
	}

	@Test
	public void removeChildFromParent() {
		List<Category> allCategories = categoryManager.getAllModelList();
		int sizeBefore = allCategories.size();
		List<Long> childCategoryIds = new ArrayList<>();
		CategoryDto parentCategoryDto = createAndSaveRandomCategoryWithChildren(childCategoryIds);
		Set<CategoryDto> childCategories = parentCategoryDto.getChildCategories();
		int size = childCategories.size();
		Random r = new Random();
		int childIndex = r.nextInt(size);
		CategoryDto selectedChildToChangeParent = null;
		Iterator<CategoryDto> childIterator = childCategories.iterator();
		while (childIterator.hasNext()) {
			if (childIndex == 0) {
				selectedChildToChangeParent = childIterator.next();
				break;
			}
			childIndex--;
		}
		assertNotNull(selectedChildToChangeParent);
		assertNotNull(childCategories.contains(selectedChildToChangeParent));
		childCategoryIds.remove(selectedChildToChangeParent.getId());
		SaveCategoryRequestDto removeChildFromParentRequest = RequestDtoFactory.createSaveCategoryRequestDto();
		removeChildFromParentRequest.setCategoryId(parentCategoryDto.getId());
		removeChildFromParentRequest.setCategoryName(parentCategoryDto.getName());
		removeChildFromParentRequest.setChildCategoryIds(childCategoryIds);
		GetCategoryResponseDto removeChildFromParentResponse = (GetCategoryResponseDto) categoryController.saveCategory(null, null, removeChildFromParentRequest);
		CategoryDto savedNewParentCategoryDto = removeChildFromParentResponse.getCategoryDto();
		assertTrue(savedNewParentCategoryDto.equals(parentCategoryDto));
		Set<CategoryDto> newChildCategories = savedNewParentCategoryDto.getChildCategories();
		assertTrue(!newChildCategories.contains(selectedChildToChangeParent));
		CategoryDto reFetchedPreviousParentCategoryDto = fetchCategoryFromId(parentCategoryDto.getId());
		Set<CategoryDto> reFetchedChildren = reFetchedPreviousParentCategoryDto.getChildCategories();
		CategoryDto removedChild = fetchCategoryFromId(selectedChildToChangeParent.getId());
		assertTrue(reFetchedPreviousParentCategoryDto.equals(parentCategoryDto));
		assertTrue(selectedChildToChangeParent.getParentCategory() == null);
		assertTrue(!reFetchedChildren.contains(removedChild));
		allCategories = categoryManager.getAllModelList();
		int sizeAfter = allCategories.size();
		assertTrue((sizeAfter - 11) == sizeBefore);
	}

	@Test
	public void changeParentCategoryTest() throws DatabaseCorruptedException {
		List<Category> allCategories = categoryManager.getAllModelList();
		int sizeBefore = allCategories.size();
		List<Long> childCategoryIds = new ArrayList<>();
		List<Long> newChildCategoryIds = new ArrayList<>();
		CategoryDto parentCategoryDto = createAndSaveRandomCategoryWithChildren(childCategoryIds);
		Set<CategoryDto> childCategories = parentCategoryDto.getChildCategories();
		int size = childCategories.size();
		Random r = new Random();
		int childIndex = r.nextInt(size);
		CategoryDto selectedChildToChangeParent = null;
		Iterator<CategoryDto> childIterator = childCategories.iterator();
		while (childIterator.hasNext()) {
			if (childIndex == 0) {
				selectedChildToChangeParent = childIterator.next();
				break;
			}
			childIndex--;
		}
		assertNotNull(selectedChildToChangeParent);
		assertNotNull(childCategories.contains(selectedChildToChangeParent));
		CategoryDto newParentCategoryDto = createAndSaveRandomCategoryWithChildren(newChildCategoryIds);
		assertTrue(newParentCategoryDto.getId() != null);
		SaveCategoryRequestDto changeParentRequest = RequestDtoFactory.createSaveCategoryRequestDto();
		changeParentRequest.setCategoryId(selectedChildToChangeParent.getId());
		changeParentRequest.setParentCategoryId(newParentCategoryDto.getId());
		changeParentRequest.setCategoryName(selectedChildToChangeParent.getName());
		GetCategoryResponseDto changeParentResponse = (GetCategoryResponseDto) categoryController.saveCategory(null, null, changeParentRequest);
		assertTrue(changeParentResponse.getCategoryDto().equals(selectedChildToChangeParent));
		CategoryDto refetchedPreviousParent = fetchCategoryFromId(parentCategoryDto.getId());
		CategoryDto refetchedSelectedChild = fetchCategoryFromId(selectedChildToChangeParent.getId());
		CategoryDto refetchedNextParent = fetchCategoryFromId(newParentCategoryDto.getId());
		assertTrue(refetchedSelectedChild.getId().equals(selectedChildToChangeParent.getId()));
		assertTrue(refetchedPreviousParent.getId().equals(parentCategoryDto.getId()));
		assertTrue(refetchedNextParent.getId().equals(newParentCategoryDto.getId()));
		Set<CategoryDto> previousParentsChildren = refetchedPreviousParent.getChildCategories();
		Set<CategoryDto> nextParentsChildren = refetchedNextParent.getChildCategories();
		assertTrue(!CollectionUtils.isEmpty(nextParentsChildren));
		assertTrue(!CollectionUtils.isEmpty(previousParentsChildren));
		assertTrue(!previousParentsChildren.contains(refetchedSelectedChild));
		assertTrue(nextParentsChildren.contains(refetchedSelectedChild));
		assertTrue(refetchedSelectedChild.getParentCategory().equals(refetchedNextParent));
		assertTrue(previousParentsChildren.size() == 9);
		assertTrue(nextParentsChildren.size() == 11);
		allCategories = categoryManager.getAllModelList();
		int sizeAfter = allCategories.size();
		assertTrue((sizeAfter - sizeBefore) == 22);
	}

	@Test
	public void testNullingChildrenCategories() throws DatabaseCorruptedException {
		List<Category> allCategories = categoryManager.getAllModelList();
		int sizeBefore = allCategories.size();
		List<Long> childCategoryIds = new ArrayList<>();
		CategoryDto parentCategoryDto = createAndSaveRandomCategoryWithChildren(childCategoryIds);
		SaveCategoryRequestDto nullingChildrenSaveRequest = RequestDtoFactory.createSaveCategoryRequestDto();
		nullingChildrenSaveRequest.setCategoryId(parentCategoryDto.getId());
		nullingChildrenSaveRequest.setCategoryName(parentCategoryDto.getName());
		GetCategoryResponseDto nullingChildrenSaveResponse = (GetCategoryResponseDto) categoryController.saveCategory(null, null, nullingChildrenSaveRequest);
		CategoryDto savedParentCategory = nullingChildrenSaveResponse.getCategoryDto();
		Set<CategoryDto> nulledChildrenSet = savedParentCategory.getChildCategories();
		assertTrue(CollectionUtils.isEmpty(nulledChildrenSet));
		assertTrue(savedParentCategory.equals(parentCategoryDto));
		for (Long childCategoryId : childCategoryIds) {
			CategoryDto orphanChildCategoryDto = fetchCategoryFromId(childCategoryId);
			assertTrue(orphanChildCategoryDto.getParentCategory() == null);
		}
		allCategories = categoryManager.getAllModelList();
		int sizeAfter = allCategories.size();
		assertTrue(sizeAfter - sizeBefore == 11);
	}

	protected CategoryDto fetchCategoryFromId(Long previousParentCategoryId) {
		GetCategoryRequestDto getPreviousParentCategoryRequest = RequestDtoFactory.createGetCategoryReqeustDto();
		getPreviousParentCategoryRequest.setId(previousParentCategoryId);
		GetCategoryResponseDto getPreviousParentResponse = (GetCategoryResponseDto) categoryController.getCategoryById(null, null, getPreviousParentCategoryRequest);
		CategoryDto reFetchedPreviousParentCategoryDto = getPreviousParentResponse.getCategoryDto();
		return reFetchedPreviousParentCategoryDto;
	}

	protected CategoryDto createAndSaveRandomCategoryWithChildren(List<Long> childCategoryIds) {
		for (int i = 0; i < 10; i++) {
			CategoryDto savedCategory = saveRandomlyGeneratedCategoryDtoFromController();
			childCategoryIds.add(savedCategory.getId());
		}
		SaveCategoryRequestDto saveCategoryRequestDto = createRandomCategoryWithChildren(childCategoryIds);
		GetCategoryResponseDto saveCategoryResponse = (GetCategoryResponseDto) categoryController.saveCategory(null, null, saveCategoryRequestDto);
		CategoryDto parentCategory1 = saveCategoryResponse.getCategoryDto();
		return parentCategory1;
	}

	protected SaveCategoryRequestDto createRandomCategoryWithChildren(List<Long> childCategoryIds) {
		SaveCategoryRequestDto saveCategoryRequestDto = createRandomSaveCategoryRequestDto();
		saveCategoryRequestDto.setChildCategoryIds(childCategoryIds);
		return saveCategoryRequestDto;
	}

	protected CategoryDto saveRandomlyGeneratedCategoryDtoFromController() {
		SaveCategoryRequestDto saveCategoryRequestDto = createRandomSaveCategoryRequestDto();
		GetCategoryResponseDto saveCategoryResponse = (GetCategoryResponseDto) categoryController.saveCategory(null, null, saveCategoryRequestDto);
		CategoryDto savedCategory = saveCategoryResponse.getCategoryDto();
		assertTrue(savedCategory.getName().equals(saveCategoryRequestDto.getCategoryName()));
		assertTrue(savedCategory.getPriority().equals(saveCategoryRequestDto.getPriority()));
		return savedCategory;
	}

	protected SaveCategoryRequestDto createRandomSaveCategoryRequestDto() {
		SaveCategoryRequestDto saveCategoryRequestDto = RequestDtoFactory.createSaveCategoryRequestDto();
		Random random = new Random();
		int priority = random.nextInt(10);
		String categoryName = UUID.randomUUID().toString();
		saveCategoryRequestDto.setCategoryName(categoryName);
		saveCategoryRequestDto.setPriority(priority);
		return saveCategoryRequestDto;
	}
}
