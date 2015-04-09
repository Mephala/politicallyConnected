package serviceprovider.service.address;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import service.provider.common.dto.MahalleDto;
import service.provider.common.dto.SemtDto;

@Entity
public class Semt implements Cloneable {
	@Transient
	private Set<String> savedMahalleNames;

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "NAME")
	private String name;

	@OneToMany(cascade = CascadeType.ALL)
	private Set<Mahalle> mahalleSet;

	public Long getId() {
		return id;
	}

	protected void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected Set<Mahalle> getMahalleSet() {
		return mahalleSet;
	}

	protected void addMahalle(Mahalle mahalle) {
		this.mahalleSet.add(mahalle);
		savedMahalleNames.add(mahalle.getName());
	}

	@Override
	public String toString() {
		return "Semt [id=" + id + ", name=" + name + "]";
	}

	protected Semt(String name) {
		super();
		this.name = name;
		this.mahalleSet = new HashSet<>();
		savedMahalleNames = new HashSet<>();
	}

	protected boolean containsMahalleWithName(String mahalleName) {
		return savedMahalleNames.contains(mahalleName);
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
	public Semt clone() {
		Semt clonedSemt = new Semt(getName());
		clonedSemt.setId(id);
		for (Mahalle mahalle : mahalleSet) {
			clonedSemt.addMahalle(mahalle.clone());
		}
		return clonedSemt;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Semt other = (Semt) obj;
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

	public SemtDto convertToDto() {
		SemtDto semtDto = new SemtDto();
		semtDto.setId(id);
		semtDto.setName(name);
		List<MahalleDto> mahalleDtoList = new ArrayList<>();
		for (Mahalle mahalle : mahalleSet) {
			mahalleDtoList.add(mahalle.convertToDto());
		}
		semtDto.setMahalleList(mahalleDtoList);
		return semtDto;
	}

	protected Semt() {

	}

}
