package serviceprovider.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import serviceprovider.Application;
import serviceprovider.model.Configuration;

@Repository
public class ConfigurationDAO extends AbstractDAO<Configuration> {

	@Override
	public void initializeModel(Configuration model) {
		// TODO Auto-generated method stub

	}

	@Override
	public Configuration findModelById(Long id) {
		Configuration r = null;
		Session session = createSessionAndBeginTransaction();
		r = (Configuration) session.get(Configuration.class, id);
		commitTransactionAndCloseSession(session);
		return r;
	}

	@Override
	public List<Configuration> getAllModelList() {
		List<Configuration> userRememberers;
		Session session = createSessionAndBeginTransaction();
		Criteria criteria = session.createCriteria(Configuration.class);
		userRememberers = criteria.list();
		commitTransactionAndCloseSession(session);
		return userRememberers;
	}

	@Override
	public void saveModel(Configuration model) {
		Session session = createSessionAndBeginTransaction();
		session.saveOrUpdate(model);
		commitTransactionAndCloseSession(session);
	}

	@Override
	public void deleteModel(Configuration model) {
		Session session = createSessionAndBeginTransaction();
		session.delete(model);
		commitTransactionAndCloseSession(session);
	}

	public List<Configuration> findConfigurationByApplication(Application app) {
		Session session = createSessionAndBeginTransaction();
		Criteria criteria = session.createCriteria(Configuration.class);
		criteria = criteria.add(Restrictions.eq("configurationApp", app));
		List<Configuration> crits = criteria.list();
		commitTransactionAndCloseSession(session);
		return crits;
	}

}
