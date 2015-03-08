package serviceprovider.manager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.axis.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import service.provider.common.dto.CategoryDto;
import service.provider.common.dto.ProviderDto;
import service.provider.common.exception.DatabaseCorruptedException;
import service.provider.common.exception.InvalidRequestException;
import service.provider.common.request.SaveCategoryRequestDto;
import serviceprovider.dao.CategoryDao;
import serviceprovider.model.Category;
import serviceprovider.model.Provider;

@Service
public class CategoryManager extends AbstractServiceManager<Category> {

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private ProviderManager providerManager;

	@PostConstruct
	public void initialize() {
		initializeDAO(categoryDao);
	}

	@Override
	public void deleteModel(Category category) throws DatabaseCorruptedException {
		if (category != null) {
			// Linking parents to its children.
			Category parentCategory = category.getParentCategory();
			Set<Category> children = category.getChildCategories();
			if (parentCategory != null) {
				Set<Category> parentsChildren = parentCategory.getChildCategories();
				if (CollectionUtils.isEmpty(parentsChildren))
					throw new DatabaseCorruptedException("Category :" + category + " doesn't know that he is orphan. Parent:" + parentCategory);
				parentsChildren.addAll(children); // parent's children should not be null unless database is corrupted.
				for (Category newChild : parentsChildren) {
					if (!newChild.getParentCategory().equals(parentCategory))
						newChild.setParentCategory(parentCategory);
				}
				// Removing this child from parent's children set. ( He is now orphan )
				parentsChildren.remove(category);
				super.saveModel(parentCategory);
				// Removing connections from category to be deleted.
				category.setChildCategories(null);
				category.setParentCategory(null);
				super.deleteModel(category);
			} else if (children != null) {
				for (Category orphanChildren : children) {
					orphanChildren.setParentCategory(null);
					super.saveModel(orphanChildren);
				}
				category.setChildCategories(null);
				super.deleteModel(category);
			} else {
				if (category.getId() != null)
					super.deleteModel(category);
			}
		}
	}

	public void mergeCategory(Category category) {
		categoryDao.mergeModel(category);
	}

	public CategoryDto createCategoryDto(Category category, boolean convertWithoutProviderSet) {
		return createCategoryDto(category, convertWithoutProviderSet, false);
	}

	public CategoryDto createCategoryDto(Category category, boolean convertWithoutProviderSet, boolean convertWithoutRelatives) {
		CategoryDto categoryDto = new CategoryDto();
		if (category != null) {
			categoryDto.setId(category.getId());
			categoryDto.setName(category.getName());
			categoryDto.setPriority(category.getPriority());
			if (!convertWithoutRelatives) {
				Category parentCategory = category.getParentCategory();
				if (parentCategory != null) {
					categoryDto.setParentCategory(createCategoryDto(parentCategory, false, true));
				}
				Set<Category> children = category.getChildCategories();
				if (children != null) {
					Set<CategoryDto> childrenDtos = new HashSet<>();
					for (Category category2 : children) {
						childrenDtos.add(createCategoryDto(category2, false, true));
					}
					categoryDto.setChildCategories(childrenDtos);
				}
			}
			if (!convertWithoutProviderSet) {
				Set<ProviderDto> providerDtos = new HashSet<>();
				Set<Provider> providers = category.getProviders();
				if (!CollectionUtils.isEmpty(providers)) {
					for (Provider provider : providers) {
						providerDtos.add(providerManager.createProviderDto(provider));
					}
					categoryDto.setProviders(providerDtos);
				}
			}
		}
		return categoryDto;
	}

	public Category createCategoryDto(SaveCategoryRequestDto saveCategoryRequest) throws InvalidRequestException {
		logger.info("Creating category from saveCategory request.");
		if (saveCategoryRequest == null || StringUtils.isEmpty(saveCategoryRequest.getCategoryName()))
			throw new InvalidRequestException();
		Category category = null;
		Long categoryId = saveCategoryRequest.getCategoryId();
		if (categoryId == null)
			category = new Category();
		else
			category = findModelById(categoryId);
		fillCategoryDetailsFromRequest(saveCategoryRequest, category);
		return category;
	}

	protected void fillCategoryDetailsFromRequest(SaveCategoryRequestDto saveCategoryRequest, Category category) throws InvalidRequestException {

		try {
			String categoryName = saveCategoryRequest.getCategoryName();
			Integer priority = saveCategoryRequest.getPriority();
			Long parentId = saveCategoryRequest.getParentCategoryId();
			Category parentCategory = null;
			Set<Category> childCategories = new HashSet<>();
			if (parentId != null) {
				parentCategory = findModelById(parentId);
				if (parentCategory != null) {
					Set<Category> parentsChilds = parentCategory.getChildCategories();
					if (parentsChilds == null) {
						parentsChilds = new HashSet<>();
					}
					parentsChilds.add(category);
					parentCategory.setChildCategories(parentsChilds);
				}
			}
			List<Long> childCategoryIds = saveCategoryRequest.getChildCategoryIds();
			if (!CollectionUtils.isEmpty(childCategoryIds)) {
				for (Long childCategoryId : childCategoryIds) {
					Category childCategory = findModelById(childCategoryId);
					childCategories.add(childCategory);
					childCategory.setParentCategory(category);
				}
			}
			category.setName(categoryName);
			category.setPriority(priority);
			category.setChildCategories(childCategories);
			category.setParentCategory(parentCategory);
		} catch (Exception e) {
			logger.info("Error during creating category process. Throwing exception.");
			throw new InvalidRequestException();
		}
	}

	public void mergeModel(Category model) {
		Category mergedCategory = categoryDao.mergeModel(model);
		refreshModel(model, mergedCategory);
	}

	private void refreshModel(Category model, Category mergedCategory) {
		model.setId(mergedCategory.getId());
		if (model.getParentCategory() != null)
			model.getParentCategory().setId(mergedCategory.getParentCategory().getId());
		Set<Category> childs = model.getChildCategories();
		if (!CollectionUtils.isEmpty(childs)) {

		}
	}

}
