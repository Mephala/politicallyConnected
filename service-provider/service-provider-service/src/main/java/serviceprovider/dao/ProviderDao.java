package serviceprovider.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import serviceprovider.model.Provider;

@Repository
public class ProviderDao extends AbstractDAO<Provider> {

	@Override
	public void initializeModel(Provider model) {
		// TODO Auto-generated method stub

	}

	@Override
	public Provider findModelById(Long id) {
		Session session = createSessionAndBeginTransaction();
		Provider provider = (Provider) session.get(Provider.class, id);
		initializeModel(provider);
		commitTransactionAndCloseSession(session);
		return provider;
	}

	@Override
	public List<Provider> getAllModelList() {
		Session session = createSessionAndBeginTransaction();
		Criteria criteria = session.createCriteria(Provider.class);
		List<Provider> providers = criteria.list();
		initializeModelList(providers);
		commitTransactionAndCloseSession(session);
		return providers;
	}

	@Override
	public void saveModel(Provider model) {
		Session session = createSessionAndBeginTransaction();
		session.save(model);
		commitTransactionAndCloseSession(session);
	}

	@Override
	public void deleteModel(Provider model) {
		Session session = createSessionAndBeginTransaction();
		session.delete(model);
		commitTransactionAndCloseSession(session);

	}

}
