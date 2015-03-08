package serviceprovider.model;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Provider {

	@Id
	@GeneratedValue
	private Long id;

	@Column(length = 256)
	private String title;

	@Column
	private BigDecimal lattitute;

	@Column
	private BigDecimal longitute;

	@ManyToOne
	private User user;

	@Column(length = 100)
	private String staticPhone;

	@Column(length = 100)
	private String gsm;

	@Column(length = 20)
	private String tckn;

	@ManyToMany
	private Set<Category> parentCategories;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getLattitute() {
		return lattitute;
	}

	public void setLattitute(BigDecimal lattitute) {
		this.lattitute = lattitute;
	}

	public BigDecimal getLongitute() {
		return longitute;
	}

	public void setLongitute(BigDecimal longitute) {
		this.longitute = longitute;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getStaticPhone() {
		return staticPhone;
	}

	public void setStaticPhone(String staticPhone) {
		this.staticPhone = staticPhone;
	}

	public String getGsm() {
		return gsm;
	}

	public void setGsm(String gsm) {
		this.gsm = gsm;
	}

	public String getTckn() {
		return tckn;
	}

	public void setTckn(String tckn) {
		this.tckn = tckn;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Provider [id=" + id + ", title=" + title + ", user=" + user + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Provider other = (Provider) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Set<Category> getParentCategories() {
		return parentCategories;
	}

	public void setParentCategories(Set<Category> parentCategories) {
		this.parentCategories = parentCategories;
	}

}
