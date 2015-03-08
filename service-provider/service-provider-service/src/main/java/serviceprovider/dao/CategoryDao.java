package serviceprovider.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import serviceprovider.model.Category;

@Repository
public class CategoryDao extends AbstractDAO<Category> {

	@Override
	public void initializeModel(Category model) {
		if (model != null) {
			initializeSingleCategory(model);
			if (model.getParentCategory() != null)
				initializeSingleCategory(model.getParentCategory());
			if (!CollectionUtils.isEmpty(model.getChildCategories())) {
				Set<Category> children = model.getChildCategories();
				for (Category category : children) {
					initializeSingleCategory(category);
				}
			}
		}
	}

	protected void initializeSingleCategory(Category model) {
		Hibernate.initialize(model.getChildCategories());
		Hibernate.initialize(model.getParentCategory());
		Hibernate.initialize(model.getProviders());
	}

	@Override
	public Category findModelById(Long id) {
		Session session = createSessionAndBeginTransaction();
		Category category = (Category) session.get(Category.class, id);
		if (category != null)
			initializeModel(category);
		commitTransactionAndCloseSession(session);
		return category;
	}

	@Override
	public List<Category> getAllModelList() {
		List<Category> allCategories;
		Session session = createSessionAndBeginTransaction();
		Criteria criteria = session.createCriteria(Category.class);
		allCategories = criteria.list();
		initializeModelList(allCategories);
		commitTransactionAndCloseSession(session);
		return allCategories;
	}

	@Override
	public void saveModel(Category model) {
		Session session = createSessionAndBeginTransaction();
		session.saveOrUpdate(model);
		commitTransactionAndCloseSession(session);
	}

	@Override
	public void deleteModel(Category model) {
		Session session = createSessionAndBeginTransaction();
		session.delete(model);
		commitTransactionAndCloseSession(session);
	}

	public Category mergeModel(Category model) {
		Session session = createSessionAndBeginTransaction();
		model = (Category) session.merge(model);
		commitTransactionAndCloseSession(session);
		return model;
	}

}
