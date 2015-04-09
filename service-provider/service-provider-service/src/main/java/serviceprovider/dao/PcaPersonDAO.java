package serviceprovider.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.util.CollectionUtils;

import serviceprovider.model.PcaManagementJob;
import serviceprovider.model.PcaPerson;
import serviceprovider.model.PcaPoliticalJob;

public class PcaPersonDAO extends AbstractDAO<PcaPerson> {

	@Override
	public void initializeModel(PcaPerson model) {
		if (model != null) {
			List<PcaManagementJob> jobs = model.getManagementJobs();
			List<PcaPoliticalJob> pJobs = model.getPoliticalJobs();
			if (CollectionUtils.isEmpty(pJobs))
				Hibernate.initialize(pJobs);
			if (CollectionUtils.isEmpty(jobs))
				Hibernate.initialize(jobs);
		}
	}

	@Override
	public PcaPerson findModelById(Long id) {
		Session session = createSessionAndBeginTransaction();
		PcaPerson person = (PcaPerson) session.get(PcaPerson.class, id);
		initializeModel(person);
		commitTransactionAndCloseSession(session);
		return person;
	}

	@Override
	public List<PcaPerson> getAllModelList() {
		List<PcaPerson> allPersons;
		Session session = createSessionAndBeginTransaction();
		Criteria criteria = session.createCriteria(PcaPerson.class);
		allPersons = criteria.list();
		for (PcaPerson pcaPerson : allPersons) {
			initializeModel(pcaPerson);
		}
		commitTransactionAndCloseSession(session);
		return allPersons;
	}

	@Override
	public void saveModel(PcaPerson pcaPerson) {
		Session session = createSessionAndBeginTransaction();
		List<PcaManagementJob> managementJobs = pcaPerson.getManagementJobs();
		List<PcaPoliticalJob> politicalJobs = pcaPerson.getPoliticalJobs();
		if (!CollectionUtils.isEmpty(politicalJobs)) {
			for (PcaPoliticalJob pcaPoliticalJob : politicalJobs) {
				session.saveOrUpdate(pcaPoliticalJob);
			}
		}
		if (!CollectionUtils.isEmpty(managementJobs)) {
			for (PcaManagementJob managementJob : managementJobs) {
				session.saveOrUpdate(managementJob);
			}
		}
		session.saveOrUpdate(pcaPerson);
		commitTransactionAndCloseSession(session);
	}

	@Override
	public void deleteModel(PcaPerson model) {
		Session session = createSessionAndBeginTransaction();
		session.delete(model);
		commitTransactionAndCloseSession(session);
	}

}
