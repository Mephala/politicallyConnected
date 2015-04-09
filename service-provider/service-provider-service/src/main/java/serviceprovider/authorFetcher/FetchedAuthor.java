package serviceprovider.authorFetcher;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class FetchedAuthor {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String name;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<FetchedArticle> fetchedArticles;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<FetchedArticle> getFetchedArticles() {
		return fetchedArticles;
	}

	public void setFetchedArticles(Set<FetchedArticle> fetchedArticles) {
		this.fetchedArticles = fetchedArticles;
	}

	public Long getId() {
		return id;
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
		FetchedAuthor other = (FetchedAuthor) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
