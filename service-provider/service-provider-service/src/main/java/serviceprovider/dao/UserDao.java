package serviceprovider.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import serviceprovider.model.User;

@Repository
public class UserDao extends AbstractDAO<User> {

	@Override
	public void initializeModel(User model) {
		// None required atm.

	}

	@Override
	public User findModelById(Long id) {
		Session session = createSessionAndBeginTransaction();
		User user = (User) session.get(User.class, id);
		initializeModel(user);
		commitTransactionAndCloseSession(session);
		return user;
	}

	@Override
	public List<User> getAllModelList() {
		List<User> allUsers;
		Session session = createSessionAndBeginTransaction();
		Criteria criteria = session.createCriteria(User.class);
		allUsers = criteria.list();
		initializeModelList(allUsers);
		commitTransactionAndCloseSession(session);
		return allUsers;
	}

	@Override
	public void saveModel(User model) {
		Session session = createSessionAndBeginTransaction();
		session.save(model);
		commitTransactionAndCloseSession(session);
	}

	@Override
	public void deleteModel(User model) {
		Session session = createSessionAndBeginTransaction();
		session.delete(model);
		commitTransactionAndCloseSession(session);
	}

	public List<User> findUserByEmailAndPassword(String email, String password) {
		List<User> users;
		Session session = createSessionAndBeginTransaction();
		Criteria criteria = session.createCriteria(User.class);
		criteria = criteria.add(Restrictions.eq("email", email));
		criteria = criteria.add(Restrictions.eq("password", password));
		users = criteria.list();
		commitTransactionAndCloseSession(session);
		return users;
	}

}
