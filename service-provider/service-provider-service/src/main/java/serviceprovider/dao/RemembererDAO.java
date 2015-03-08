package serviceprovider.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import serviceprovider.model.Rememberer;

@Repository
public class RemembererDAO extends AbstractDAO<Rememberer> {

	@Override
	public void initializeModel(Rememberer model) {
		// TODO Auto-generated method stub

	}

	@Override
	public Rememberer findModelById(Long id) {
		Rememberer r = null;
		Session session = createSessionAndBeginTransaction();
		r = (Rememberer) session.get(Rememberer.class, id);
		commitTransactionAndCloseSession(session);
		return r;
	}

	@Override
	public List<Rememberer> getAllModelList() {
		List<Rememberer> userRememberers;
		Session session = createSessionAndBeginTransaction();
		Criteria criteria = session.createCriteria(Rememberer.class);
		userRememberers = criteria.list();
		commitTransactionAndCloseSession(session);
		return userRememberers;
	}

	public List<Rememberer> getAllRememberesByUsername(String username) {
		List<Rememberer> userRememberers;
		Session session = createSessionAndBeginTransaction();
		Criteria criteria = session.createCriteria(Rememberer.class);
		criteria = criteria.add(Restrictions.eq("username", username));
		userRememberers = criteria.list();
		commitTransactionAndCloseSession(session);
		return userRememberers;
	}

	@Override
	public void saveModel(Rememberer model) {
		Session session = createSessionAndBeginTransaction();
		session.saveOrUpdate(model);
		commitTransactionAndCloseSession(session);
	}

	@Override
	public void deleteModel(Rememberer model) {
		Session session = createSessionAndBeginTransaction();
		session.delete(model);
		commitTransactionAndCloseSession(session);
	}

}
