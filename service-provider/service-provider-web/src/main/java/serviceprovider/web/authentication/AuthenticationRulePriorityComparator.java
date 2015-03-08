package serviceprovider.web.authentication;

import java.util.Comparator;

class AuthenticationRulePriorityComparator implements Comparator<AuthenticationRule> {

	@Override
	public int compare(AuthenticationRule o1, AuthenticationRule o2) {
		Integer r1 = Integer.valueOf(o1.getPriority());
		Integer r2 = Integer.valueOf(o2.getPriority());
		return r2.compareTo(r1); // Bigger priority first.
	}

}
