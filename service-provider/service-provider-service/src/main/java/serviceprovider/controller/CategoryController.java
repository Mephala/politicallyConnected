package serviceprovider.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import service.provider.common.core.ResponseStatus;
import service.provider.common.dto.CategoryDto;
import service.provider.common.exception.AbstractServiceException;
import service.provider.common.exception.InvalidRequestException;
import service.provider.common.request.GetAllCategoryIdsRequestDto;
import service.provider.common.request.GetCategoryRequestDto;
import service.provider.common.request.SaveCategoryRequestDto;
import service.provider.common.response.GetAllCategoryIdsResponseDto;
import service.provider.common.response.GetCategoryResponseDto;
import serviceprovider.manager.CategoryManager;
import serviceprovider.model.Category;

@Controller
public class CategoryController extends AbstractServiceController {

	private Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private CategoryManager categoryManager;

	@RequestMapping(value = "/saveCategory.do")
	@ResponseBody
	public Object saveCategory(HttpServletRequest request, HttpServletResponse response, @RequestBody final SaveCategoryRequestDto saveCategoryRequest) {
		logger.info("request is received." + saveCategoryRequest);
		GetCategoryResponseDto responseDto = new GetCategoryResponseDto();
		try {
			validateRequest(saveCategoryRequest);
			Long id = saveCategoryRequest.getCategoryId();
			if (id != null) {
				Category category = categoryManager.findModelById(id);
				Category parentCategory = category.getParentCategory();
				Set<Category> childCategories = category.getChildCategories();
				Category expectedCategory = categoryManager.createCategoryDto(saveCategoryRequest);
				Category expectedParentCategory = expectedCategory.getParentCategory();
				Set<Category> expectedChildCategories = expectedCategory.getChildCategories();
				boolean noLongerParentUnequality = (expectedParentCategory == null && parentCategory != null);
				boolean differentParentUnequality = parentCategory == null ? false : !parentCategory.equals(expectedParentCategory);
				boolean newParentAssignedUnequality = (parentCategory == null && expectedParentCategory != null);
				boolean parentCategoryUnequality = noLongerParentUnequality || newParentAssignedUnequality;
				boolean allChildrenOrphanedUnequality = (expectedChildCategories == null && childCategories != null);
				boolean newChildrenAssignedUnequality = (childCategories == null && expectedChildCategories != null);
				boolean childCategoriesUnequality = allChildrenOrphanedUnequality || newChildrenAssignedUnequality;
				boolean differentChildCategoriesUnequality = !childCategories.equals(expectedChildCategories);
				if (childCategoriesUnequality || differentChildCategoriesUnequality) {
					if (differentChildCategoriesUnequality) {
						Iterator<Category> childCategoryIterator = childCategories.iterator();
						Set<Category> noLongerChildrenSet = new HashSet<>();
						while (childCategoryIterator.hasNext()) {
							Category childCategory = childCategoryIterator.next();
							if (!expectedChildCategories.contains(childCategory))
								noLongerChildrenSet.add(childCategory);
						}
						Iterator<Category> newChildsIterator = expectedChildCategories.iterator();
						Set<Category> newChildrenSet = new HashSet<>();
						while (newChildsIterator.hasNext()) {
							Category childCategory = newChildsIterator.next();
							if (!childCategories.contains(childCategory))
								newChildrenSet.add(childCategory);
						}
						for (Category newChildren : newChildrenSet) {
							newChildren.setParentCategory(category);
						}
						for (Category noLongerChildren : noLongerChildrenSet) {
							noLongerChildren.setParentCategory(null);
							categoryManager.saveModel(noLongerChildren);
						}
						category.setChildCategories(expectedChildCategories);
					}
				}
				if (parentCategoryUnequality || differentParentUnequality) {
					if (noLongerParentUnequality) {
						Set<Category> currentParentsChilds = parentCategory.getChildCategories();
						currentParentsChilds.remove(category);
						categoryManager.saveModel(parentCategory);
						category.setParentCategory(null);
					} else if (newParentAssignedUnequality) {

					} else if (differentParentUnequality) {
						Set<Category> currentParentsChilds = parentCategory.getChildCategories();
						currentParentsChilds.remove(category);
						categoryManager.saveModel(parentCategory);
						categoryManager.saveModel(expectedParentCategory);
						category.setParentCategory(expectedParentCategory);
					}
				}
				Category categoryToBeSaved = category;
				if (category.getParentCategory() != null) {
					categoryToBeSaved = category.getParentCategory();
				}
				Set<Category> childs = category.getChildCategories();
				if (CollectionUtils.isEmpty(childs)) {
					categoryManager.saveModel(categoryToBeSaved);
				} else {
					categoryManager.mergeModel(categoryToBeSaved); // Must merge since session already has this item.
				}
				CategoryDto categoryDto = categoryManager.createCategoryDto(category, true);
				responseDto.setCategoryDto(categoryDto);
				responseDto.setResponseStatus(ResponseStatus.OK);
			} else {
				Category category = categoryManager.createCategoryDto(saveCategoryRequest);
				Category categoryToBeSaved = null;
				if (category.getParentCategory() != null) {
					categoryToBeSaved = category.getParentCategory();
				} else {
					categoryToBeSaved = category;
				}
				Set<Category> childs = category.getChildCategories();
				if (CollectionUtils.isEmpty(childs)) {
					categoryManager.saveModel(categoryToBeSaved);
				} else {
					categoryManager.mergeModel(categoryToBeSaved); // Must merge since session already has this item.
				}
				CategoryDto categoryDto = categoryManager.createCategoryDto(category, true);
				responseDto.setCategoryDto(categoryDto);
				responseDto.setResponseStatus(ResponseStatus.OK);
			}
			return responseDto;
		} catch (AbstractServiceException ase) {
			logger.info("request  encountered serviceException. Exception:" + ase);
			setMeaningfulException(responseDto, ase);
		} catch (Exception ex) {
			logger.info("request request received error. Exception :" + ex);
			responseDto.setResponseStatus(ResponseStatus.ERROR);
			responseDto.setError(ex.getMessage());
		} finally {
			logger.info("request method has been completed. Request:" + saveCategoryRequest);
		}
		return responseDto;
	}

	@RequestMapping(value = "/getAllCategoryIds.do")
	@ResponseBody
	public Object getAllCategoryIds(HttpServletRequest request, HttpServletResponse response, @RequestBody final GetAllCategoryIdsRequestDto getAllCategoryIdsRequest) {
		logger.info("request is received." + getAllCategoryIdsRequest);
		GetAllCategoryIdsResponseDto responseDto = new GetAllCategoryIdsResponseDto();
		try {
			validateRequest(getAllCategoryIdsRequest);
			List<Long> categoryIds = new ArrayList<>();
			List<Category> allCategories = categoryManager.getAllModelList();
			for (Category category : allCategories) {
				categoryIds.add(category.getId());
			}
			responseDto.setCategoryIds(categoryIds);
			responseDto.setResponseStatus(ResponseStatus.OK);
			return responseDto;
		} catch (AbstractServiceException ase) {
			logger.info("request  encountered serviceException. Exception:" + ase);
			setMeaningfulException(responseDto, ase);
		} catch (Exception ex) {
			logger.info(" request received error. Exception :" + ex);
			responseDto.setResponseStatus(ResponseStatus.ERROR);
			responseDto.setError(ex.getMessage());
		} finally {
			logger.info("request has been completed. Request:" + getAllCategoryIdsRequest);
		}
		return responseDto;
	}

	@RequestMapping(value = "/getCategory.do", method = RequestMethod.POST)
	@ResponseBody
	public Object getCategoryById(HttpServletRequest request, HttpServletResponse response, @RequestBody final GetCategoryRequestDto getCategoryRequest) {
		logger.info("Get category request is received." + getCategoryRequest);
		GetCategoryResponseDto responseDto = new GetCategoryResponseDto();
		try {
			validateRequest(getCategoryRequest);
			Long id = getCategoryRequest.getId();
			Category category = categoryManager.findModelById(id);
			boolean convertWithoutProviderSet = getCategoryRequest.isWithoutProviders();
			if (category == null) {
				throw new InvalidRequestException();
			}
			CategoryDto categoryDto = categoryManager.createCategoryDto(category, convertWithoutProviderSet, false);
			responseDto.setCategoryDto(categoryDto);
			responseDto.setResponseStatus(ResponseStatus.OK);
			return responseDto;
		} catch (AbstractServiceException ase) {
			logger.info("getCategoryRequest  encountered serviceException. Exception:" + ase);
			setMeaningfulException(responseDto, ase);
		} catch (Exception ex) {
			logger.info("getCategoryRequest request received error. Exception :" + ex);
			responseDto.setResponseStatus(ResponseStatus.ERROR);
			responseDto.setError(ex.getMessage());
		} finally {
			logger.info("getCategoryRequest method has been completed. Request:" + getCategoryRequest);
		}
		return responseDto;
	}
}
