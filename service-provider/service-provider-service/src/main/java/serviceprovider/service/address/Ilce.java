package serviceprovider.service.address;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Ilce implements Cloneable {
	@Transient
	private Set<String> savedSemtNames;

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "NAME")
	private String name;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<Semt> semtSet;

	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	protected Set<Semt> getSemtSet() {
		return semtSet;
	}

	protected void addSemt(Semt semt) {
		this.semtSet.add(semt);
		savedSemtNames.add(semt.getName());
	}

	protected Ilce(String name) {
		super();
		this.name = name;
		this.semtSet = new HashSet<>();
		savedSemtNames = new HashSet<>();
	}

	protected boolean containsSemtWithName(String semtName) {
		return savedSemtNames.contains(semtName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public Ilce clone() {
		Ilce clonedIlce = new Ilce(getName());
		clonedIlce.setId(id);
		for (Semt semt : semtSet) {
			clonedIlce.addSemt(semt.clone());
		}
		return clonedIlce;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ilce other = (Ilce) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	protected void setId(Long id) {
		this.id = id;
	}

}
