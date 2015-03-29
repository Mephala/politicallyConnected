package mainApp.model;

import java.text.Collator;
import java.util.Comparator;

import mainApp.utils.Constants;

public class ManagementJobComparator implements Comparator<ManagementJob> {
	public final static ManagementJobComparator COMPARATOR = new ManagementJobComparator();

	private ManagementJobComparator() {
	}

	public int compare(ManagementJob o1, ManagementJob o2) {
		Collator collator = Collator.getInstance(Constants.locale_TR);
		return collator.compare(o1.getName(), o2.getName());
	}
}
