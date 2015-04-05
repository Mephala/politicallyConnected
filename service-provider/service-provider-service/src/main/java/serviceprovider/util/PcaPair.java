package serviceprovider.util;

public final class PcaPair {

	private final String name;
	private final String year;

	public PcaPair(String name, String year) {
		super();
		this.name = name;
		this.year = year;
	}

	public String getName() {
		return name;
	}

	public String getYear() {
		return year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
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
		PcaPair other = (PcaPair) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PcaPair [name=" + name + ", year=" + year + "]";
	}

}
