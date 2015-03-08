package serviceprovider.manager;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import serviceprovider.model.User;

@Service
public class UserSessionManager {

	private Log logger = LogFactory.getLog(this.getClass());
	private Map<Long, Long> sessionTimer = new ConcurrentHashMap<Long, Long>();

	@Scheduled(fixedDelay = 1000l * 60 * 5)
	public void removeObsoleteSessionUsers() {
		logger.info("Removing obsolete session users from customly managed session");
		Date now = new Date();
		Long nowAsLong = now.getTime();
		Set<Long> idSet = sessionTimer.keySet();
		for (Long id : idSet) {
			Long lastActionTime = sessionTimer.get(id);
			Long timeLimit = 1000l * 60l * 10l;
			Long latePoint = lastActionTime + timeLimit;
			if (latePoint < nowAsLong) {
				sessionTimer.remove(id);
			}
		}

	}

	public synchronized void updateUserSession(User user) {
		Long id = user.getId();
		Date now = new Date();
		sessionTimer.put(id, now.getTime());
	}

}
