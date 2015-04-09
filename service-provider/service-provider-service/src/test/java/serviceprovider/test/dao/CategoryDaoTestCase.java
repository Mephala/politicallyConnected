package serviceprovider.test.dao;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import serviceprovider.dao.CategoryDao;
import serviceprovider.model.Category;

@RunWith(MockitoJUnitRunner.class)
public class CategoryDaoTestCase {

	CategoryDao categoryDao;

	@Before
	public void initialize() {
		categoryDao = new CategoryDao();
	}

	@Test
	public void testInitializeNullCategory() {
		categoryDao.initializeModel(null);
	}

	@Test
	public void testInitializeEmptyObject() {
		Category category = new Category();
		categoryDao.initializeModel(category);
	}

	@Test
	public void saveAndDeleteCategoryTest() {
		Category category = new Category();
		category.setName("toBeDeleted");
		List<Category> allCategories = categoryDao.getAllModelList();
		assertNotNull(allCategories);
		int sizeBeforeSave = allCategories.size();
		categoryDao.saveModel(category);
		categoryDao.deleteModel(category);
		allCategories = categoryDao.getAllModelList();
		assertNotNull(allCategories);
		int sizeAfterSave = allCategories.size();
		assertTrue(sizeAfterSave == sizeBeforeSave);
	}

}
