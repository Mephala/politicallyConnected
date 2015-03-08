//package serviceprovider.unittest.bundle.category;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.Callable;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//
//import serviceprovider.manager.CategoryManager;
//import serviceprovider.model.Category;
//import serviceprovider.model.Provider;
//import serviceprovider.unittest.bundle.TestResult;
//
//@Component
//public class RunCategoryUnitTests implements Callable<TestResult> {
//
//	@Autowired
//	private CategoryManager categoryManager;
//
//	private Log logger = LogFactory.getLog(this.getClass());
//	private final String saveCategoryName = "TestCategory";
//	private final String saveChildCategory1Name = "TestChild1";
//	private final String saveChildCategory2Name = "TestChild2";
//	private final String testProviderGsm = "5356781453";
//	private final String testProviderTitle = "DenemeProvider";
//	private final String testProviderTckn = "127762543675";
//	private final String grandpaCategoryName = "Grandpa_Category";
//
//	@Override
//	public TestResult call() throws Exception {
//		TestResult testStatus = TestResult.SUCCESS;
//		logger.info("Starting category test bundle...");
//		testStatus = deleteAllCategories(testStatus);
//		testStatus = saveCategoryTest(testStatus);
//		testStatus = getAllCategory(testStatus);
//		testStatus = deleteCategory(testStatus);
//		return testStatus;
//	}
//
//	private TestResult saveCategoryTest(TestResult testStatus) {
//		if (TestResult.SUCCESS.equals(testStatus)) {
//			try {
//				Category parentCategory = new Category();
//				parentCategory.setName(saveCategoryName);
//				Category child1 = new Category();
//				child1.setName(saveChildCategory1Name);
//				child1.setParentCategory(parentCategory);
//				Category child2 = new Category();
//				child2.setName(saveChildCategory2Name);
//				child2.setParentCategory(parentCategory);
//				Set<Category> childSet = new HashSet<>();
//				childSet.add(child1);
//				childSet.add(child2);
//				parentCategory.setChildCategories(childSet);
//				Provider provider = new Provider();
//				provider.setGsm(testProviderGsm);
//				provider.setTitle(testProviderTitle);
//				provider.setTckn(testProviderTckn);
//				Set<Provider> providerSet = new HashSet<>();
//				providerSet.add(provider);
//				parentCategory.setProviders(providerSet);
//				categoryManager.saveModel(parentCategory); // All must be saved at this point.
//				logger.info("Saving category is complete...");
//			} catch (Exception e) {
//				testStatus = TestResult.FAIL;
//				e.printStackTrace();
//			}
//		}
//		return testStatus;
//	}
//
//	private TestResult getAllCategory(TestResult testStatus) {
//		if (TestResult.SUCCESS.equals(testStatus)) {
//			try {
//				List<Category> allCategories = categoryManager.getAllModelList();
//				if (CollectionUtils.isEmpty(allCategories))
//					return TestResult.FAIL;
//			} catch (Exception e) {
//				testStatus = TestResult.FAIL;
//				e.printStackTrace();
//			}
//		}
//		return testStatus;
//	}
//
//	private TestResult deleteCategory(TestResult testStatus) {
//		if (TestResult.SUCCESS.equals(testStatus)) {
//			try {
//				// Creating a parent Category for our root category.
//				Category grandpa = new Category();
//				grandpa.setName(grandpaCategoryName);
//				List<Category> allCategories = categoryManager.getAllModelList();
//				if (CollectionUtils.isEmpty(allCategories))
//					return TestResult.FAIL;
//				for (Category category : allCategories) {
//					if (saveCategoryName.equals(category.getName())) {
//						Set<Category> grandpaChilds = new HashSet<>();
//						grandpaChilds.add(category);
//						category.setParentCategory(grandpa);
//						grandpa.setChildCategories(grandpaChilds);
//						categoryManager.saveModel(grandpa);
//						Set<Category> children = category.getChildCategories();
//						categoryManager.deleteModel(category);
//						for (Category newChildren : children) {
//							if (!newChildren.getParentCategory().equals(grandpa))
//								return TestResult.FAIL;
//						}
//					}
//				}
//				testStatus = deleteAllCategories(testStatus);
//				// Deleting a single parent.
//				testStatus = saveCategoryTest(testStatus);
//				allCategories = categoryManager.getAllModelList();
//				if (CollectionUtils.isEmpty(allCategories))
//					return TestResult.FAIL;
//				for (Category category : allCategories) {
//					if (saveCategoryName.equals(category.getName())) {
//						categoryManager.deleteModel(category);
//					}
//				}
//				allCategories = categoryManager.getAllModelList();
//				if (CollectionUtils.isEmpty(allCategories))
//					return TestResult.FAIL;
//				if (allCategories.size() != 1)
//					return TestResult.FAIL;
//				if (allCategories.get(0).getParentCategory() != null)
//					return TestResult.FAIL;
//				if (!CollectionUtils.isEmpty(allCategories.get(0).getChildCategories()))
//					return TestResult.FAIL;
//				categoryManager.deleteModel(allCategories.get(0));
//				allCategories = categoryManager.getAllModelList();
//				if (!CollectionUtils.isEmpty(allCategories))
//					return TestResult.FAIL;
//			} catch (Exception e) {
//				testStatus = TestResult.FAIL;
//				e.printStackTrace();
//			}
//		}
//		return testStatus;
//	}
//
//	private TestResult deleteAllCategories(TestResult testStatus) {
//		if (TestResult.SUCCESS.equals(testStatus)) {
//			try {
//				List<Category> allCategories = categoryManager.getAllModelList();
//				for (Category category : allCategories) {
//					categoryManager.deleteModel(category);
//				}
//			} catch (Exception e) {
//				testStatus = TestResult.FAIL;
//				e.printStackTrace();
//			}
//		}
//		return testStatus;
//	}
//
//	@Override
//	public String toString() {
//		return "RunCategoryUnitTests";
//	}
//
// }
