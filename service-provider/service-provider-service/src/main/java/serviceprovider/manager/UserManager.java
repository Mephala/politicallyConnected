package serviceprovider.manager;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import service.provider.common.exception.MultipleUserWithSameEmailAndPassword;
import service.provider.common.exception.RequiredDataMissingException;
import serviceprovider.dao.UserDao;
import serviceprovider.model.User;

@Service
public class UserManager extends AbstractServiceManager<User> {

	@Autowired
	private UserDao userDao;

	@PostConstruct
	public void initialize() {
		initializeDAO(userDao);
	}

	/**
	 * First user with the given email and password data is returned. Throws exception if multiple users are found.
	 * 
	 * @param user
	 * @return
	 * @throws RequiredDataMissingException
	 * @throws MultipleUserWithSameEmailAndPassword
	 */
	public User getUserByPasswordAndEmail(User user) throws RequiredDataMissingException, MultipleUserWithSameEmailAndPassword {
		String email = user.getEmail();
		String password = user.getPassword();
		boolean requiredPartsValid = StringUtils.hasText(email) && StringUtils.hasText(password);
		if (!requiredPartsValid) {
			throw new RequiredDataMissingException("password", "email");
		}
		List<User> foundUsers = userDao.findUserByEmailAndPassword(email, password);
		if (CollectionUtils.isEmpty(foundUsers)) {
			return null;
		} else if (foundUsers.size() > 1) {
			throw new MultipleUserWithSameEmailAndPassword(email, password);
		} else {
			return foundUsers.iterator().next();
		}
	}
}
