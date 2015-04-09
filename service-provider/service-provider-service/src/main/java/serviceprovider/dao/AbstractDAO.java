package serviceprovider.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.util.CollectionUtils;

import serviceprovider.util.HibernateUtil;

public abstract class AbstractDAO<Model> implements DaoInitializer<Model> {

	@Override
	public abstract void initializeModel(Model model);

	@Override
	public void initializeModelList(List<Model> list) {
		if (!CollectionUtils.isEmpty(list)) {
			for (Model model : list) {
				initializeModel(model);
			}
		}
	}

	public abstract Model findModelById(Long id);

	public abstract List<Model> getAllModelList();

	public abstract void saveModel(Model model);

	public abstract void deleteModel(Model model);

	protected Session createSessionAndBeginTransaction() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		return session;
	}

	protected void commitTransactionAndCloseSession(Session session) {
		session.getTransaction().commit();
		session.close();
	}

}