package mainApp.utils;

import java.util.Set;

import mainApp.model.Manager;

public class HtmlTableCreaterFromMergedManagers {

	private final Set<Manager> mergedManagerSet;

	public HtmlTableCreaterFromMergedManagers(Set<Manager> mergedManagers) {
		this.mergedManagerSet = mergedManagers;
	}

	public String getMergeDataAsHtmlTable() {
		if (mergedManagerSet == null || mergedManagerSet.size() == 0)
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append("<table style=\"width:100%\">");
		for (Manager manager : mergedManagerSet) {
			sb.append(manager.getHtmlTableRow());
		}
		sb.append("</table>");
		return sb.toString();
	}

}
