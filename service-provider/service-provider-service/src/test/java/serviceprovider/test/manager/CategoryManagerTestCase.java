package serviceprovider.test.manager;

import org.junit.Before;
import org.junit.Test;

import service.provider.common.exception.DatabaseCorruptedException;
import service.provider.common.exception.InvalidRequestException;
import service.provider.common.request.SaveCategoryRequestDto;
import serviceprovider.manager.CategoryManager;
import serviceprovider.model.Category;

public class CategoryManagerTestCase {

	CategoryManager categoryManager;

	@Before
	public void initialize() {
		categoryManager = new CategoryManager();
	}

	@Test
	public void deleteModelNullCategoryTest() throws DatabaseCorruptedException {
		categoryManager.deleteModel(null);
	}

	@Test
	public void deleteModelEmptyCategoryTest() throws DatabaseCorruptedException {
		Category category = new Category();
		categoryManager.deleteModel(category);
	}

	@Test(expected = DatabaseCorruptedException.class)
	public void corruptedDataTest() throws DatabaseCorruptedException {
		Category parentCategoryWithoutChildren = new Category();
		Category testCategory = new Category();
		testCategory.setParentCategory(parentCategoryWithoutChildren);
		// trying to delete category that has a parent which doesn't have a children should produce databaseCorruptedException.
		categoryManager.deleteModel(testCategory);
	}

	@Test(expected = InvalidRequestException.class)
	public void createCategoryFromNullRequest() throws InvalidRequestException {
		categoryManager.createCategoryDto(null);
	}

	@Test(expected = InvalidRequestException.class)
	public void createCategoryFromEmptyRequest() throws InvalidRequestException {
		SaveCategoryRequestDto saveCategoryReq = new SaveCategoryRequestDto();
		categoryManager.createCategoryDto(saveCategoryReq);
	}

}
