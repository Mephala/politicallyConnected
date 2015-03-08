package serviceprovider.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import serviceprovider.model.Rememberer;
import serviceprovider.model.Scheduler;

@Repository
public class SchedulerDAO extends AbstractDAO<Scheduler> {

	@Override
	public void initializeModel(Scheduler model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Scheduler findModelById(Long id) {
		Scheduler r = null;
		Session session = createSessionAndBeginTransaction();
		r = (Scheduler) session.get(Scheduler.class, id);
		commitTransactionAndCloseSession(session);
		return r;
	}

	@Override
	public List<Scheduler> getAllModelList() {
		List<Scheduler> userRememberers;
		Session session = createSessionAndBeginTransaction();
		Criteria criteria = session.createCriteria(Scheduler.class);
		userRememberers = criteria.list();
		commitTransactionAndCloseSession(session);
		return userRememberers;
	}

	@Override
	public void saveModel(Scheduler model) {
		Session session = createSessionAndBeginTransaction();
		session.saveOrUpdate(model);
		commitTransactionAndCloseSession(session);		
	}

	@Override
	public void deleteModel(Scheduler model) {
		Session session = createSessionAndBeginTransaction();
		session.delete(model);
		commitTransactionAndCloseSession(session);		
	}

}
