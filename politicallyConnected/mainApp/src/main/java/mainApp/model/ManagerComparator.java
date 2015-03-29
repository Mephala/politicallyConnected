package mainApp.model;

import java.text.Collator;
import java.util.Comparator;

import mainApp.utils.Constants;

public class ManagerComparator implements Comparator<Manager> {

	public static final ManagerComparator COMPARATOR = new ManagerComparator();

	private ManagerComparator() {

	}

	public int compare(Manager o1, Manager o2) {
		Collator collator = Collator.getInstance(Constants.locale_TR);
		return collator.compare(o1.getName(), o2.getName());
	}

}
