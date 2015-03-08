package serviceprovider.relationer.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;

import serviceprovider.dao.AbstractDAO;
import serviceprovider.relationer.model.DBImage;

public class DBImageDAO extends AbstractDAO<DBImage> {

	private static DBImageDAO instance;

	private DBImageDAO() {

	}

	public static synchronized DBImageDAO getInstance() {
		if (instance == null)
			instance = new DBImageDAO();
		return instance;
	}

	@Override
	public void initializeModel(DBImage model) {
		if (model != null) {

		}
	}

	@Override
	public DBImage findModelById(Long id) {
		DBImage retVal = null;
		Session session = createSessionAndBeginTransaction();
		retVal = (DBImage) session.get(DBImage.class, id);
		initializeModel(retVal);
		commitTransactionAndCloseSession(session);
		return retVal;
	}

	@Override
	public List<DBImage> getAllModelList() {
		List<DBImage> images;
		Session session = createSessionAndBeginTransaction();
		Criteria criteria = session.createCriteria(DBImage.class);
		images = criteria.list();
		for (DBImage dbImage : images) {
			initializeModel(dbImage);
		}
		commitTransactionAndCloseSession(session);
		return images;
	}

	@Override
	public void saveModel(DBImage model) {
		Session session = createSessionAndBeginTransaction();
		session.saveOrUpdate(model);
		commitTransactionAndCloseSession(session);
	}

	@Override
	public void deleteModel(DBImage model) {
		Session session = createSessionAndBeginTransaction();
		session.delete(model);
		commitTransactionAndCloseSession(session);
	}
}
