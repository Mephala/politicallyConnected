package serviceprovider.authorFetcher;

import java.util.Date;

class Article {
	private String content;
	private final String name;
	private final Date date;

	protected Article(String content, String name, Date date) {
		super();
		this.content = content;
		this.name = name;
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public String getName() {
		return name;
	}

	public Date getDate() {
		return date;
	}

	@Override
	public String toString() {
		return "Article [name=" + name + ", date=" + date + "]";
	}

	public void setContent(String content) {
		this.content = content;
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
		Article other = (Article) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
