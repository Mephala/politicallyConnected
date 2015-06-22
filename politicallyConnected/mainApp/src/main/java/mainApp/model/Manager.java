package mainApp.model;

import java.util.Set;

public class Manager {

	private String name;
	private Set<ManagementJob> jobs;
	private Set<PoliticalJob> pJobs;

	public synchronized String getName() {
		return name;
	}

	public synchronized void setName(String name) {
		this.name = name;
	}

	public synchronized Set<ManagementJob> getJobs() {
		return jobs;
	}

	public synchronized void setJobs(Set<ManagementJob> jobs) {
		this.jobs = jobs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Manager other = (Manager) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Manager [name=" + name + ", jobs=" + jobs + "] \n";
	}

	public synchronized Set<PoliticalJob> getpJobs() {
		return pJobs;
	}

	public synchronized void setpJobs(Set<PoliticalJob> pJobs) {
		this.pJobs = pJobs;
	}

	public String getHtmlTableRow() {
		StringBuilder sb = new StringBuilder();
		sb.append("<tr>");
		sb.append("<td>" + getName() + "</td>");
		StringBuilder mJobBuilder = new StringBuilder();
		for (ManagementJob managementJob : jobs) {
			mJobBuilder.append(managementJob.getName() + "-" + managementJob.getYear());
		}
		sb.append("<td>" + mJobBuilder.toString() + "</td>");
		StringBuilder pJobBuilder = new StringBuilder();
		for (PoliticalJob pJob : this.pJobs) {
			pJobBuilder.append(pJob.getName() + "-" + pJob.getYear());
		}
		sb.append("<td>" + pJobBuilder.toString() + "</td>");
		sb.append("</tr>");
		return sb.toString();
	}
}
